package com.greenbatgames.rubyred.entity;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Align;
import com.greenbatgames.rubyred.player.Player;
import com.greenbatgames.rubyred.screen.GameScreen;
import com.greenbatgames.rubyred.util.Constants;

/**
 * Created by Quiv on 12-11-2016.
 */

public class Tooltip extends Actor
{
    private String label;
    private Rectangle bounds;
    private boolean visible;

    BitmapFont font;

    public Tooltip(String label, float x, float y, float width, float height) {
        this.label = label;
        bounds = new Rectangle(x, y, width, height);
        visible = false;

        font = new BitmapFont();
        font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
        font.setColor(Constants.TOOLTIP_COLOR);
        font.getData().setScale(Constants.TOOLTIP_SCALE);
    }



    @Override
    public void act(float delta) {
        visible = (bounds.overlaps(GameScreen.getInstance().getPlayer().getBounds()));
    }



    @Override
    public void draw(Batch batch, float parentAlpha) {
        if (visible)
            font.draw(batch, label, bounds.x, bounds.y, 0, Align.center, false);
    }
}
