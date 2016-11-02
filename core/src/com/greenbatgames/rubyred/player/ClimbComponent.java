package com.greenbatgames.rubyred.player;

import com.badlogic.gdx.math.Vector2;
import com.greenbatgames.rubyred.entity.Initializeable;

/**
 * Created by Quiv on 02-11-2016.
 */

public class ClimbComponent implements Initializeable
{
    private Player player;

    private boolean climbing;
    private Vector2 gripPoint;

    public ClimbComponent(Player player) {
        this.player = player;
        init();
    }

    @Override
    public void init() {
        climbing = false;
        gripPoint = new Vector2();
    }



    public void update(float delta) {

    }

    /*
        Getters and Setters
     */

    public void setGripPoint(float x, float y) {
        gripPoint.set(x, y);
    }

    public void setGripPoint(Vector2 vector2) {
        gripPoint.set(vector2.x, vector2.y);
    }

    public float gripX() { return gripPoint.x; }
    public float gripY() { return gripPoint.y; }
}
