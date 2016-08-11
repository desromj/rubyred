package com.greenbatgames.rubyred.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.greenbatgames.rubyred.RubyGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		config.height = 720;
		config.width = 960;

		new LwjglApplication(new RubyGame(), config);
	}
}
