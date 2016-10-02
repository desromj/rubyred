package com.greenbatgames.rubyred;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.greenbatgames.rubyred.screen.GameScreen;

public class RubyGame extends Game {
	@Override
	public void create () {
		setScreen(GameScreen.getInstance());
	}
}
