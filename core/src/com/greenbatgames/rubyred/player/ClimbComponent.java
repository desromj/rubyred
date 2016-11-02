package com.greenbatgames.rubyred.player;

import com.badlogic.gdx.math.Vector2;

/**
 * Created by Quiv on 02-11-2016.
 */

public class ClimbComponent extends PlayerComponent
{
    private Vector2 gripPoint;

    boolean climbing;

    public ClimbComponent(Player player) {
        super(player);
    }



    @Override
    public void init() {
        gripPoint = new Vector2();
        climbing = false;
    }



    @Override
    public boolean update(float delta) {

        if (!climbing) return true;



        return false;
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

    public void startClimbing() { climbing = true; }
    public float gripX() { return gripPoint.x; }
    public float gripY() { return gripPoint.y; }
}
