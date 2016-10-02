package com.greenbatgames.rubyred.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.greenbatgames.rubyred.util.Constants;
import com.greenbatgames.rubyred.util.Utils;

/**
 * Created by Quiv on 01-10-2016.
 */

public class Skylight extends Platform
{
    private boolean broken, active;
    private float lifetime;

    public Skylight(float x, float y, float width, float height, World world)
    {
        super(x, y, width, height, world, false);
        broken = false;
        active = false;
        lifetime = Constants.SKYLIGHT_LIFETIME;
    }



    @Override
    public void act(float delta)
    {
        // No action if we're broken or deactivated
        if (!active || broken)
            return;

        lifetime -= delta;

        // Only update if we're NOT broken with a positive lifetime
        if (lifetime < 0.0f) {
            broken = true;
            Utils.playSound("sounds/glass-shatter.wav", 1.0f);
        }
    }



    @Override
    public void draw(Batch batch, float parentAlpha)
    {
        batch.end();

        ShapeRenderer renderer = new ShapeRenderer();
        renderer.setProjectionMatrix(batch.getProjectionMatrix());

        renderer.begin(ShapeRenderer.ShapeType.Filled);

        if (broken)
            renderer.setColor(Color.RED);
        else
            renderer.setColor(Color.WHITE);

        renderer.rect(
                getX(),
                getY(),
                getWidth(),
                getHeight());

        renderer.end();

        batch.begin();
    }



    public void activate()
    {
        active = true;
        Utils.playSound("sounds/glass-crack.wav", 1.0f);
    }

    public boolean isBroken() { return broken; }
}
