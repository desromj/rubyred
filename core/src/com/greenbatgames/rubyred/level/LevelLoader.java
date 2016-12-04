package com.greenbatgames.rubyred.level;

import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.objects.PolylineMapObject;
import com.greenbatgames.rubyred.entity.Checkpoint;
import com.greenbatgames.rubyred.entity.Terrain;
import com.greenbatgames.rubyred.entity.Tooltip;

/**
 * Created by Quiv on 27-10-2016.
 */

public final class LevelLoader
{
    public static final String TAG = LevelLoader.class.getSimpleName();

    private LevelLoader() {}

    public static Level loadLevel(String filename) {
        Level newLevel = new Level(filename);

        // Set player position to the spawn position object in the TiledMap
        for (MapLayer layer : newLevel.tiledMap.getLayers()) {

            if (layer.getName().compareTo("collision") == 0) {
                for (MapObject object : layer.getObjects()) {

                    MapProperties props = object.getProperties();
                    String type = props.get("type", String.class);

                    // Add terrain to the level
                    if ((object instanceof PolylineMapObject)
                        && type.compareTo("terrain") == 0) {

                        PolylineMapObject plmo = (PolylineMapObject) object;
                        float [] verts = plmo.getPolyline().getTransformedVertices();

                        // Determine the width and height, as these are not present in the Tiled program
                        float xMin = verts[0], xMax = verts[0], yMin = verts[1], yMax = verts[1];

                        for (int i = 2; i < verts.length; i += 2) {
                            if (verts[i] < xMin) xMin = verts[i];
                            if (verts[i] > xMax) xMax = verts[i];
                            if (verts[i+1] < yMin) yMin = verts[i+1];
                            if (verts[i+1] > yMax) yMax = verts[i+1];
                        }

                        Terrain terrain = new Terrain(
                                props.get("x", Float.class),
                                props.get("y", Float.class),
                                xMax - xMin,
                                yMax - yMin,
                                newLevel.getWorld(),
                                verts
                        );
                        newLevel.stage.addActor(terrain);
                    }
                }
            }

            if (layer.getName().compareTo("obstacle") == 0) {

                for (MapObject object : layer.getObjects()) {

                    MapProperties props = object.getProperties();
                    String type = props.get("type", String.class);

                    if (type.compareTo("player-spawn") == 0) {
                        newLevel.spawnPosition.set(
                            props.get("x", Float.class),
                            props.get("y", Float.class)
                        );
                    } else if (type.compareTo("tooltip") == 0) {
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
        }

        return newLevel;
    }
}
