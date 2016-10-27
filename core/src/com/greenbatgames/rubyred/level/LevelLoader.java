package com.greenbatgames.rubyred.level;

import com.badlogic.gdx.files.FileHandle;

/**
 * Created by Quiv on 27-10-2016.
 */

public final class LevelLoader
{
    private LevelLoader() {}

    public static Level loadLevel(String level) {
        return new Level(level);
    }
}
