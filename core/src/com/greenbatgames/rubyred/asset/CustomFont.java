package com.greenbatgames.rubyred.asset;

import com.badlogic.gdx.Application;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.greenbatgames.rubyred.util.Constants;

/**
 * Created by Quiv on 15-11-2016.
 */

public class CustomFont
{
    private CustomFont() {}

    public static BitmapFont makeFont() {

        BitmapFont font;
        Application.ApplicationType type = Gdx.app.getType();

        // Use normal bitmaps if we're in HTML5, otherwise load truetype
        if (type == Application.ApplicationType.WebGL) {
            font = new BitmapFont();
            font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
            font.getData().setScale(Constants.GUI_FONT_SCALE);
            font.setColor(Constants.GUI_FONT_COLOR);
        } else {
            FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Bombing.ttf"));
            FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

            parameter.size = (int) (Constants.WORLD_WIDTH / 12f);
            parameter.borderWidth = 2;

            font = generator.generateFont(parameter);
            generator.dispose();
        }

        return font;
    }
}
