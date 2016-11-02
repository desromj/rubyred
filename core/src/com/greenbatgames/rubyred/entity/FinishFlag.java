package com.greenbatgames.rubyred.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.greenbatgames.rubyred.screen.GameScreen;
import com.greenbatgames.rubyred.util.Constants;

/**
 * Created by Quiv on 01-11-2016.
 */

public class FinishFlag extends Actor
{
    private Rectangle bounds;
    private Vector2 position;
    private Sprite sprite;

    public FinishFlag(float x, float y)
    {
        this.position = new Vector2(x, y);
        sprite = new Sprite(new Texture(Gdx.files.internal("sprites/finish-flag.png")));
        bounds = new Rectangle(x, y, Constants.FINISH_FLAG_WIDTH, Constants.FINISH_FLAG_HEIGHT);
    }

    @Override
    public void draw(Batch batch, float parentAlpha)
    {
        batch.draw(
                sprite.getTexture(),
                position.x,
                position.y,
                Constants.FINISH_FLAG_WIDTH / 2.0f,
                Constants.FINISH_FLAG_HEIGHT / 2.0f,
                Constants.FINISH_FLAG_WIDTH,
                Constants.FINISH_FLAG_HEIGHT,
                1,
                1,
                0f,
                sprite.getRegionX(),
                sprite.getRegionY(),
                sprite.getRegionWidth(),
                sprite.getRegionHeight(),
                false,
                false);
    }

    public Rectangle getBounds() { return this.bounds; }
}