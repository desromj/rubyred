package com.greenbatgames.rubyred.player;

import com.badlogic.gdx.math.Vector2;
import com.greenbatgames.rubyred.util.Constants;

/**
 * Created by Quiv on 02-11-2016.
 */

public class ClimbComponent extends PlayerComponent
{
    /** The point where the player will grab and pivot around while climbing */
    private Vector2 gripPoint;

    /** The point referring to the bottom corner of the player */
    private Vector2 basePoint;

    boolean climbing, climbingRight;

    public ClimbComponent(Player player) {
        super(player);
    }



    @Override
    public void init() {
        gripPoint = new Vector2();
        basePoint = new Vector2();
        climbing = false;
    }



    @Override
    public boolean update(float delta) {

        if (!climbing) return true;

        // If climbing, we override the speed of the player and move their
        // basePoint (bottom corner) towards the final gripPoint (grab area)
        // of the climb. Once the distance between these is negligible, stop climbing

        float newXVel, newYVel;

        // Y factor
        if (baseY() < gripY())
            newYVel = Constants.RUBY_CLIMB_SPEED.y;
        else
            newYVel = 0f;

        // X factor
        if (baseX() < gripX())
            newXVel = Constants.RUBY_CLIMB_SPEED.x;
        else
            newXVel = -Constants.RUBY_CLIMB_SPEED.x;

        // Set the new velocity and return false to break any other movement updates
        player.getBody().setLinearVelocity(newXVel, newYVel);
        return false;
    }

    /*
        Getters and Setters
     */

    public void startClimbing(Vector2 gripPoint) {
        this.gripPoint.set(gripPoint.x, gripPoint.y);

        if (gripPoint.x < player.getLeft())
            climbingRight = false;
        else
            climbingRight = true;

        // if we're climbing right, base point is bottom-left of player box
        // if we're climbing left, base point is bottom-right of player base
        basePoint.set(
                (climbingRight) ? player.getLeft() : player.getRight(),
                player.getBottom()
        );

        climbing = true;
    }

    public float gripX() { return gripPoint.x; }
    public float gripY() { return gripPoint.y; }
    public float baseX() { return basePoint.x; }
    public float baseY() { return basePoint.y; }
}
