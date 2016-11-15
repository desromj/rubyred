package com.greenbatgames.rubyred.util;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.greenbatgames.rubyred.asset.CustomFont;
import com.greenbatgames.rubyred.screen.GameScreen;

/**
 * Created by Quiv on 15-11-2016.
 */

public class RubyHUD extends Actor
{
    private Viewport viewport;
    private BitmapFont font;

    public RubyHUD(Viewport viewport) {
        this.viewport = viewport;

        font = CustomFont.makeFont();
    }



    @Override
    public void draw(Batch batch, float parentAlpha) {

        font.draw(
                batch,
                "Lives: " + GameScreen.getInstance().getPlayer().getLives(),
                GameScreen.getInstance().getChaseCam().getLeft() + Constants.HUD_MARGIN,
                GameScreen.getInstance().getChaseCam().getTop() - Constants.HUD_MARGIN,
                0f,
                Align.topLeft,
                false
        );
    }
}
