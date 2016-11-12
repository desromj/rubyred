package com.greenbatgames.rubyred.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import com.greenbatgames.rubyred.RubyGame;
import com.greenbatgames.rubyred.util.Constants;

public class HtmlLauncher extends GwtApplication {

        @Override
        public GwtApplicationConfiguration getConfig () {
                return new GwtApplicationConfiguration(
                        (int) Constants.WORLD_WIDTH,
                        (int) Constants.WORLD_HEIGHT);
        }

        @Override
        public ApplicationListener createApplicationListener () {
                return new RubyGame();
        }
}