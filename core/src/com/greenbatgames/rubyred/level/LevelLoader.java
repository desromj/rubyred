package com.greenbatgames.rubyred.level;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.greenbatgames.rubyred.entity.Checkpoint;
import com.greenbatgames.rubyred.entity.Tooltip;

/**
 * Created by Quiv on 27-10-2016.
 */

public final class LevelLoader
{
    private LevelLoader() {}

    public static Level loadLevel(String filename) {
        Level newLevel = new Level(filename);

        // Set player position to the spawn position object in the TiledMap
        for (MapLayer layer : newLevel.tiledMap.getLayers()) {

            if (layer.getName().compareTo("collision") == 0) {
                for (MapObject object : layer.getObjects()) {

                    MapProperties props = object.getProperties();
                    String type = props.get("type", String.class);
                }
            }

            if (layer.getName().compareTo("obstacle") == 0) {

                for (MapObject object : layer.getObjects()) {

                    MapProperties props = object.getProperties();
                    String type = props.get("type", String.class);

                    if (type.compareTo("tooltip") == 0) {
                        Tooltip tt = new Tooltip(
                                props.get("label", String.class),
                                props.get("x", Float.class),
                                props.get("y", Float.class),
                                props.get("width", Float.class),
                                props.get("height", Float.class)
                        );
                        newLevel.stage.addActor(tt);
                    } else if (type.compareTo("checkpoint") == 0) {
                        Checkpoint cp = new Checkpoint(
                                props.containsKey("label")
                                        ? props.get("label", String.class)
                                        : "Checkpoint Reached",
                                props.get("x", Float.class),
                                props.get("y", Float.class),
                                props.get("width", Float.class),
                                props.get("height", Float.class)
                        );
                        newLevel.checkpoints.add(cp);
                        newLevel.stage.addActor(cp);
                    }
                }
            }

            if (layer.getName().compareTo("spawn") == 0) {
                for (MapObject object : layer.getObjects()) {
                    if (object.getName().compareTo("spawn-position") == 0) {
                        newLevel.setPlayerSpawnPosition(
                                object.getProperties().get("x", Float.class),
                                object.getProperties().get("y", Float.class)
                        );

                        newLevel.player.init();
                    }
                }
            }
        }

        return newLevel;
    }
}
