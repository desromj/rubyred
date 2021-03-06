package com.greenbatgames.rubyred.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import com.greenbatgames.rubyred.iface.Activateable;
import com.greenbatgames.rubyred.screen.GameScreen;
import com.greenbatgames.rubyred.util.Constants;
import com.greenbatgames.rubyred.util.Enums;

/**
 * Created by Quiv on 02-11-2016.
 */

public class ClimbComponent extends PlayerComponent
{
    public static final String TAG = ClimbComponent.class.getSimpleName();

    /** The point where the player will grab and pivot around while climbing */
    private Vector2 gripPoint;

    /** Remaining climb time */
    private float climbTimeLeft;

    boolean climbing, climbingRight;

    public ClimbComponent(Player player) {
        super(player);
    }



    @Override
    public void init() {
        gripPoint = new Vector2();
        climbing = false;
        climbingRight = true;

        climbTimeLeft = 0;
    }



    @Override
    public boolean update(float delta) {

        if (!climbing)
            handleCollision();

        climbTimeLeft -= delta;

        // Return true if we're not climbing or have been climbing for too long already
        if (climbTimeLeft <= 0f)
            climbing = false;

        if (!climbing) return true;

        /*
            If climbing, we set the player's position to its ultimate final
            position, then play the animation for climbing which is centred
            around a base point. This base point is the ledge being climbed on.

            if we're climbing right, base point is bottom-left of player box
            if we're climbing left, base point is bottom-right of player base
        */

        // Set the player position to new position based on if we're climbing left or right
        player.setPosition(
                (climbingRight) ? gripX() : gripX() - player.getWidth(),
                gripY()
        );

        // set velocity to 0, in case we clip into the ledge area
        player.getBody().setLinearVelocity(0f, 0f);

        // return false to break any other movement updates if still climbing
        handleCollision();
        return !climbing;
    }



    private void handleCollision() {

        // Determine scenarios in which we just return immediately
        if (!player.isClimbButtonHeld()) return;

    }




    /*
        Getters and Setters
     */

    public void startClimbing(Vector2 gripPoint) {

        // return immediately if we are already climbing
        if (climbing) return;
        this.gripPoint.set(gripPoint.x, gripPoint.y);

        // Determine if we're climbing left or right
        if (gripPoint.x < player.getLeft())
            climbingRight = false;
        else
            climbingRight = true;

        // Add gripPoint offset of 1/2 player width based on if we're climbing left or right
        this.gripPoint.x += (climbingRight) ? -player.getWidth() / 2f : player.getWidth() / 2f;

        Gdx.app.log(TAG, "Start Climbing Triggered");

        /*
            To start a new climb, determine the start time of the animation based on how high
            up the ledge the player grabs on.
            Top of player hitbox =        RUBY_CLIMB_TIME * RUBY_MAX_CLIMB_RATIO
            Bottom of player hitbox =     RUBY_CLIMB_TIME * RUBY_MIN_CLIMB_RATIO

            calculation for the timer ratio:

                    top of player hitbox - grip point Y
            100% -  -----------------------------------
                              player height

            The top of the player hitbox must also contain the range of the gripY point
        */
        float timeRatio = 1f - (player.getTop() - gripY()) / player.getHeight();
        timeRatio = MathUtils.clamp(
                timeRatio,
                Constants.PLAYER_MIN_CLIMB_RATIO,
                Constants.PLAYER_MAX_CLIMB_RATIO);
        this.climbTimeLeft = Constants.PLAYER_CLIMB_TIME * timeRatio;

        if (gripPoint.x < player.getLeft())
            climbingRight = false;
        else
            climbingRight = true;

        climbing = true;
        player.mover().land();

        // TODO: Uncomment when climbing ready
        // player.animator().setNext(Enums.AnimationState.CLIMB, timeRatio);
    }

    /*
        Getters and Setters
     */

    public void cancelClimb() { climbing = false; }
    public boolean isClimbing() { return climbing; }
    public float getClimbTimeLeft() { return this.climbTimeLeft; }

    public float gripX() { return gripPoint.x; }
    public float gripY() { return gripPoint.y; }
}
