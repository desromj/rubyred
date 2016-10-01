package com.greenbatgames.rubyred.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.greenbatgames.rubyred.util.Constants;

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
        lifetime = Constants.SKYLIGHT_LIFETIME;
    }



    @Override
    public void act(float delta)
    {
        // No action if we're broken or deactivated
        if (!active || broken)
            return;

        // Only update if we're NOT broken with a positive lifetime
        if (!broken && lifetime < 0.0f)
        {
            broken = true;
            return;
        }

        // If we're still alive, decrement the lifetime
        lifetime -= delta;
    }



    @Override
    public void draw(Batch batch, float parentAlpha)
    {
        batch.end();

        ShapeRenderer renderer = new ShapeRenderer();
        renderer.setProjectionMatrix(batch.getProjectionMatrix());

        renderer.begin(ShapeRenderer.ShapeType.Filled);

        renderer.setColor(Color.WHITE);
        renderer.rect(
                getX(),
                getY(),
                getWidth(),
                getHeight());

        renderer.end();

        batch.begin();
    }



    public void activate() { active = true; }
    public boolean isBroken() { return broken; }
}
