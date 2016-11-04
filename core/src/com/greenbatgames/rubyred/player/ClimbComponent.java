package com.greenbatgames.rubyred.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.TimeUtils;
import com.greenbatgames.rubyred.util.Constants;

/**
 * Created by Quiv on 02-11-2016.
 */

public class ClimbComponent extends PlayerComponent
{
    public static final String TAG = ClimbComponent.class.getSimpleName();

    /** The point where the player will grab and pivot around while climbing */
    private Vector2 gripPoint;

    /** The point referring to the bottom corner of the player */
    private Vector2 basePoint;

    private long climbStartTime;

    boolean climbing, climbingRight;

    public ClimbComponent(Player player) {
        super(player);
    }



    @Override
    public void init() {
        gripPoint = new Vector2();
        basePoint = new Vector2();
        climbing = false;
        climbingRight = true;
        climbStartTime = 0;
    }



    @Override
    public boolean update(float delta) {

        // Return true if we're not climbing or have been climbing for too long already
        if ((TimeUtils.nanosToMillis(TimeUtils.nanoTime() - climbStartTime) / 1000f) > Constants.RUBY_MAX_CLIMB_TIME) {
            climbing = false;
        }

        if (!climbing) return true;

        // If climbing, we override the speed of the player and move their
        // basePoint (bottom corner) towards the final gripPoint (grab area)
        // of the climb. Once the distance between these is negligible, stop climbing

        // if we're climbing right, base point is bottom-left of player box
        // if we're climbing left, base point is bottom-right of player base
        basePoint.set(
                (climbingRight) ? player.getLeft() : player.getRight(),
                player.getBottom()
        );

        // Handle Y factor first, then X (left, then right)
        if (baseY() < gripY() + Constants.PLATFORM_EDGE_LEEWAY) {
            player.getBody().setLinearVelocity(
                    0f,
                    Constants.RUBY_CLIMB_SPEED.y
            );
        } else if (baseX() < gripX()) {
            player.getBody().setLinearVelocity(
                    Constants.RUBY_CLIMB_SPEED.x,
                    0f
            );
        } else if (baseX() > gripX()) {
            player.getBody().setLinearVelocity(
                    -Constants.RUBY_CLIMB_SPEED.x,
                    0f
            );
        }

        // Check for completion of the climb
        Gdx.app.log(TAG, "Grip Point, Base Point: (" + gripX() + ", " + gripY() + ") - (" + baseX() + ", " + baseY() + ")");

        if (Vector2.dst(baseX(), baseY(), gripX(), gripY()) < Constants.PLATFORM_EDGE_LEEWAY * 2f) {
            Gdx.app.log(TAG, "Stop Climbing Triggered");
            Gdx.app.log(TAG, "Distance: " + Vector2.dst(baseX(), baseY(), gripX(), gripY()));
            climbing = false;
            player.getBody().setLinearVelocity(0f, 0f);
        }

        //  return false to break any other movement updates if still climbing
        return !climbing;
    }

    /*
        Getters and Setters
     */

    public void startClimbing(Vector2 gripPoint) {
        // return immediately if we are already climbing
        if (climbing) return;

        climbStartTime = TimeUtils.nanoTime();

        Gdx.app.log(TAG, "Start Climbing Triggered");

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

    public boolean isClimbing() { return climbing; }
    public float gripX() { return gripPoint.x; }
    public float gripY() { return gripPoint.y; }
    public float baseX() { return basePoint.x; }
    public float baseY() { return basePoint.y; }
}
