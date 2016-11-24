package com.greenbatgames.rubyred.entity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Align;
import com.greenbatgames.rubyred.player.Player;
import com.greenbatgames.rubyred.screen.GameScreen;
import com.greenbatgames.rubyred.util.Constants;

/**
 * Created by Quiv on 14-11-2016.
 */

public class Checkpoint extends Actor
{
    private String label;
    private Rectangle bounds;
    private boolean triggered;
    private Vector2 current, startPosition, finishPosition;
    private float activeTime;

    BitmapFont font;

    public Checkpoint(String label, float x, float y, float width, float height) {
        this.label = label;
        bounds = new Rectangle(x, y, width, height);
        triggered = false;

        current = new Vector2(x + width / 2f, y);
        startPosition = new Vector2(x + width / 2f, y);
        finishPosition = new Vector2(x + width / 2f, y + Constants.RUBY_RADIUS * 4f);
        activeTime = -1;

        font = new BitmapFont();
        font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        font.setColor(Constants.TOOLTIP_COLOR);
        font.getData().setScale(Constants.TOOLTIP_SCALE);
    }



    @Override
    public void act(float delta) {

        activeTime -= delta;

        if (activeTime <= 0)
            triggered = false;

        if (triggered) {
            current.set(
                    current.x,
                    startPosition.interpolate(
                            finishPosition,
                            (Constants.CHECKPOINT_TEXT_ONSCREEN_TIME - activeTime)
                                    / Constants.CHECKPOINT_TEXT_ONSCREEN_TIME,
                            Interpolation.circleOut
                    ).y
            );
            return;
        }

        if (bounds.overlaps(GameScreen.currentLevel().getPlayer().getBounds())
                && !GameScreen.currentLevel().isCurrentCheckPoint(this)) {
            triggered = true;
            activeTime = Constants.CHECKPOINT_TEXT_ONSCREEN_TIME;
            GameScreen.currentLevel().setCurrentCheckpoint(this);
        }
    }



    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (triggered) {
            font.draw(batch, label, current.x, current.y, 0, Align.top, false);
        }
    }



    public float x() { return bounds.x; }
    public float y() { return bounds.y; }
}
