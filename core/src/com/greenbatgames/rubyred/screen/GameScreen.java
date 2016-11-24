package com.greenbatgames.rubyred.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.greenbatgames.rubyred.RubyGame;
import com.greenbatgames.rubyred.entity.Checkpoint;
import com.greenbatgames.rubyred.entity.PhysicsBody;
import com.greenbatgames.rubyred.player.Player;
import com.greenbatgames.rubyred.level.Level;
import com.greenbatgames.rubyred.level.LevelLoader;
import com.greenbatgames.rubyred.util.ChaseCam;

/**
 * Created by Quiv on 10-08-2016.
 */
public class GameScreen  extends ScreenAdapter implements InputProcessor
{
    public static final String TAG = GameScreen.class.getSimpleName();
    private static GameScreen instance = null;

    private Level level;

    private GameScreen() {}

    public static final GameScreen getInstance()
    {
        if (instance == null) {
            instance = new GameScreen();
            instance.init();
        }

        return instance;
    }

    public static final Level currentLevel() { return instance.level; }




    public void init() {
        level = LevelLoader.loadLevel("level-1.tmx");
        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void render(float delta) {

        level.render(delta);

        if (level.hasWon()) {
            Gdx.app.log(TAG, "You have Won!");
            RubyGame.setScreen(StartScreen.class);
        } else if (level.hasLost()) {
            Gdx.app.log(TAG, "You have Lost!");
            RubyGame.setScreen(StartScreen.class);
        }
    }



    /*
        ScreenAdapter Overrides
     */

    @Override
    public void resize(int width, int height)
    {
        level.getViewport().update(width, height, true);
    }

    /*
        InputProcessor Overrides
     */

    @Override
    public boolean keyDown(int keycode)
    {
        if (keycode == Input.Keys.ESCAPE)
            Gdx.app.exit();

        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
