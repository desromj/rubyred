package com.greenbatgames.rubyred.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.World;
import com.greenbatgames.rubyred.util.Constants;

/**
 * Created by Quiv on 01-10-2016.
 */

public class DropPlatform extends Platform implements Initializeable
{
    private boolean broken, active;
    private float lifetime;
    private float rotation;

    private Sprite sprite;

    public DropPlatform(float x, float y, float width, float height, World world)
    {
        super(x, y, width, height, world, true);
        sprite = new Sprite(new Texture(Gdx.files.internal("sprites/drop-platform.png")));
        init();
    }



    @Override
    public void init()
    {
        broken = false;
        active = false;
        rotation = 0f;
        sprite.setPosition(getX(), getY());
        sprite.setScale(getWidth() / sprite.getWidth());
        lifetime = Constants.DROP_PLATFORM_LIFETIME;
    }



    @Override
    public void act(float delta)
    {
        if (!active)
            return;

        lifetime -= delta;

        // Check what to do depending on if the platform is currently broken or not

        if (lifetime <= Constants.DROP_PLATFORM_FALL_DURATION) {
            float ratio = lifetime / Constants.DROP_PLATFORM_FALL_DURATION;
            if (broken) {
                ratio = MathUtils.clamp(ratio * 2f, 0, 1);
                rotation = Interpolation.circleIn.apply(0f, -90f, ratio);
            } else {
                rotation = Interpolation.circleIn.apply(-90f, 0f, ratio);
            }
        }

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
        batch.draw(
                sprite.getTexture(),
                getX(),
                getY(),
                getHeight() / 2.0f,
                getHeight() / 2.0f,
                getWidth(),
                getHeight(),
                1,
                1,
                rotation,
                sprite.getRegionX(),
                sprite.getRegionY(),
                sprite.getRegionWidth(),
                sprite.getRegionHeight(),
                false,
                false);
    }



    public void activate() { active = true; }
    public boolean isBroken() { return broken; }
}
