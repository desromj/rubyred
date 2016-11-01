package com.greenbatgames.rubyred.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.greenbatgames.rubyred.util.Constants;
import com.greenbatgames.rubyred.util.Utils;

/**
 * Created by Quiv on 01-10-2016.
 */

public class Skylight extends Platform implements Initializeable
{
    private boolean broken, active;
    private float lifetime;

    private Sprite spriteNormal, spriteBroken;

    public Skylight(float x, float y, float width, float height, World world)
    {
        super(x, y, width, height, world, false);
        spriteNormal = new Sprite(new Texture(Gdx.files.internal("sprites/skylight.png")));
        spriteBroken = new Sprite(new Texture(Gdx.files.internal("sprites/skylight-broken.png")));
        init();
    }



    @Override
    public void init()
    {
        broken = false;
        active = false;
        spriteNormal.setPosition(getX(), getY());
        spriteNormal.setScale(getWidth() / spriteNormal.getWidth());
        spriteBroken.setPosition(getX(), getY());
        spriteBroken.setScale(getWidth() / spriteBroken.getWidth());
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
        Sprite toDraw = this.broken ? spriteBroken : spriteNormal;

        batch.draw(
                toDraw.getTexture(),
                getX(),
                getY(),
                getWidth() / 2.0f,
                getHeight() / 2.0f,
                getWidth(),
                getHeight(),
                1,
                1,
                0f,
                toDraw.getRegionX(),
                toDraw.getRegionY(),
                toDraw.getRegionWidth(),
                toDraw.getRegionHeight(),
                false,
                false);
    }



    public void activate()
    {
        active = true;
        Utils.playSound("sounds/glass-crack.wav", 1.0f);
    }

    public boolean isBroken() { return broken; }
}
