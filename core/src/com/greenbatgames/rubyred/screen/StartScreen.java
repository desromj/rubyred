package com.greenbatgames.rubyred.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.greenbatgames.rubyred.util.Constants;

/**
 * Created by Quiv on 02-10-2016.
 */

public class StartScreen extends ScreenAdapter
{
    public static final String TAG = StartScreen.class.getSimpleName();



    Viewport viewport;
    Texture logo;
    Sprite logoSprite;

    String title, subtitle;

    SpriteBatch batch;
    ShapeRenderer renderer;
    BitmapFont font;

    float fadeTime, elapsedTime, scale;
    long startTime;
    boolean finished;

    public StartScreen(String logoPath, String title, String subtitle, float fadeTime)
    {
        viewport = new FitViewport(
                Constants.WORLD_WIDTH,
                Constants.WORLD_HEIGHT);

        logo = new Texture(Gdx.files.internal(logoPath));
        logoSprite = new Sprite(logo);
        this.title = title;
        this.subtitle = subtitle;
        this.fadeTime = fadeTime;
        scale = 0.2f;
        batch = new SpriteBatch();
        renderer = new ShapeRenderer();
        font = new BitmapFont();

        startTime = TimeUtils.nanoTime();
        elapsedTime = startTime;
        finished = false;

        font.getRegion().getTexture().setFilter(Texture.TextureFilter.Linear, Texture.TextureFilter.Linear);
    }



    @Override
    public void render(float delta)
    {
        // increment elapsed Time
        elapsedTime = TimeUtils.nanosToMillis(TimeUtils.nanoTime() - startTime) / 1000f;
        scale += delta * delta * fadeTime * 0.5f;

        /*
            10 periods of fade time:
                1 = nothing
                2 = fade in the logo
                3-4 = full alpha of the logo
                5 = fade out the logo
                6 = nothing
                7 = fade in the titles
                8-9 = full alphs of the titles
                10 = fade out the titles
                11 = 0 alpha
          */
        int period = (int) (elapsedTime / fadeTime);
        float alpha;
        finished = period > 11;

        switch (period)
        {
            case 2:
            case 7:
                alpha = (elapsedTime % fadeTime) / fadeTime;
                break;
            case 5:
            case 10:
                alpha = 1f - ((elapsedTime % fadeTime) / fadeTime);
                break;
            case 3:
            case 4:
            case 8:
            case 9:
                alpha = 1f;
                break;
            default:
                alpha = 0f;
                break;
        }

        // Move to rendering
        viewport.apply();

        // Start with a black background
        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        renderer.begin(ShapeRenderer.ShapeType.Filled);
        renderer.setColor(Color.BLACK);
        renderer.rect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        renderer.end();

        // Move on to sprites and fonts
        batch.setProjectionMatrix(viewport.getCamera().combined);
        batch.begin();

        batch.setColor(1, 1, 1, alpha);

        // Check what to render

        // logo
        if (period >= 2 && period <= 5) {

            batch.draw(
                    logoSprite,
                    (viewport.getWorldWidth() / 2.0f) - (logoSprite.getWidth() * scale / 2.0f),
                    (viewport.getWorldHeight() / 2.0f) - (logoSprite.getHeight() * scale / 2.0f),
                    0f,
                    0f,
                    logoSprite.getWidth(),
                    logoSprite.getHeight(),
                    scale,
                    scale,
                    0f);
        }
        // titles
        else if (period >= 7 && period <= 10) {

            font.getData().setScale(2.5f);
            font.setColor(1, 1, 1, alpha);

            font.draw(
                    batch,
                    title,
                    0f,
                    viewport.getWorldHeight() * 10.0f / 16.0f,
                    viewport.getWorldWidth(),
                    Align.center,
                    false
            );

            font.getData().setScale(1.0f);
            font.draw(
                    batch,
                    subtitle,
                    0f,
                    viewport.getWorldHeight() * 7.0f / 16.0f,
                    viewport.getWorldWidth(),
                    Align.center,
                    true
            );

        }
        // nothing
        else {

        }

        batch.end();
    }






    public boolean isFinished()
    {
        return this.finished;
    }



    @Override
    public void resize(int width, int height)
    {
        viewport.update(width, height, true);
    }



    @Override
    public void dispose()
    {
        logo.dispose();
        batch.dispose();
        font.dispose();
    }

}
