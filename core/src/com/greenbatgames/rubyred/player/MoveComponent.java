package com.greenbatgames.rubyred.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import com.badlogic.gdx.physics.box2d.World;
import com.greenbatgames.rubyred.iface.Activateable;
import com.greenbatgames.rubyred.screen.GameScreen;
import com.greenbatgames.rubyred.util.Constants;
import com.greenbatgames.rubyred.util.Enums;

/**
 * Created by Quiv on 02-11-2016.
 */

public class MoveComponent extends PlayerComponent
{
    public static final String TAG = MoveComponent.class.getSimpleName();

    private boolean grounded, jumped, facingRight;
    private float cannotJumpFor, disableCollisionFor;

    public MoveComponent(Player player) {
        super(player);
    }



    @Override
    public void init() {
        jumped = false;
        grounded = true;
        facingRight = true;
        cannotJumpFor = 0.0f;
        disableCollisionFor = 0.0f;
    }



    @Override
    public boolean update(float delta)
    {
        cannotJumpFor -= delta;
        disableCollisionFor -= delta;

        // First handle recovery time for landing
        if (!canJump()) return true;

        // Horizontal hopping movement
        Body body = player.getBody();

        // Then determine what to do based on if we're in the air or one the ground
        if (isOnGround()) {
            if (Gdx.input.isKeyPressed(Constants.KEY_RIGHT)) {
                body.applyForceToCenter(
                        Constants.PLAYER_WALK_FORCE.x,
                        Constants.PLAYER_WALK_FORCE.y,
                        true);
            }

            if (Gdx.input.isKeyPressed(Constants.KEY_LEFT)) {
                body.applyForceToCenter(
                        -Constants.PLAYER_WALK_FORCE.x,
                        Constants.PLAYER_WALK_FORCE.y,
                        true);
            }

        }

        // Set the direction facing based on x velocity, only if not recoiling
        if (player.animator().getNextLabel().compareTo("recoil") != 0) {
            if (body.getLinearVelocity().x > 0.1f)
                facingRight = true;
            else if (body.getLinearVelocity().x < -0.1f)
                facingRight = false;
        }

        return true;
    }



    public void land() {

        Gdx.app.log(TAG, "Land triggered");

        if (grounded && !jumped) {
            Gdx.app.log(TAG, "Already landed, ignored");
            return;
        }

        grounded = true;
        jumped = false;

        cannotJumpFor = Constants.PLAYER_JUMP_RECOVERY;
        // set next player.animator() values
    }



    public void jump() {
        // cannot jump if we already jumped
        if (jumped) return;

        // Otherwise, proceed with jump
        jumped = true;
        grounded = false;
    }

    /*
        Getters and Setters
     */

    public boolean canJump() {
        return cannotJumpFor <= 0f;
    }

    public boolean isCollisionDisabled() {
        return disableCollisionFor > 0f;
    }

    public boolean isOnGround() { return grounded && !jumped; }
    public boolean isInAir() { return !grounded; }
    public boolean isFacingRight() { return facingRight; }
}
