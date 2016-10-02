package com.greenbatgames.rubyred;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.greenbatgames.rubyred.screen.GameScreen;
import com.greenbatgames.rubyred.screen.StartScreen;

public class RubyGame extends Game {
	@Override
	public void create () {

		setScreen(new StartScreen(
				"GBG-logo-shaded.png",
				"Ruby Red",
				"Tile Map and Physics Platform Testing",
                0.8f
		));

		// setScreen(GameScreen.getInstance());
	}
}
