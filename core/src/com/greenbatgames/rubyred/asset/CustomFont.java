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

    public static BitmapFont makeFont(int fontSize, int borderWidth) {

        BitmapFont font;

        FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("fonts/Bombing.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();

        parameter.size = fontSize;
        parameter.borderWidth = borderWidth;

        font = generator.generateFont(parameter);
        generator.dispose();

        return font;
    }
}
