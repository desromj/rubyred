package com.greenbatgames.rubyred.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.greenbatgames.rubyred.util.Constants;

/**
 * Created by Quiv on 01-10-2016.
 */

public class DropPlatform extends Platform
{
    private boolean broken, active;
    private float lifetime;

    public DropPlatform(float x, float y, float width, float height, World world)
    {
        super(x, y, width, height, world, true);
        broken = false;
        active = false;
        lifetime = Constants.DROP_PLATFORM_LIFETIME;
    }



    @Override
    public void act(float delta)
    {
        if (!active)
            return;

        lifetime -= delta;

        // Check what to do depending on if the platform is currently broken or not

        if (lifetime < 0f) {
            if (broken) {
                recover();
            } else {
                decay();
            }
        }
    }



    public void decay()
    {
        broken = true;
        lifetime = Constants.DROP_PLATFORM_RESET_TIME;
    }



    public void recover()
    {
        broken = false;
        active = false;
        lifetime = Constants.DROP_PLATFORM_LIFETIME;
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



    public void activate() { active = true; }
    public boolean isBroken() { return broken; }
}
