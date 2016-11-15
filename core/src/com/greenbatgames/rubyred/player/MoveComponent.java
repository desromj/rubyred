package com.greenbatgames.rubyred.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;
import com.greenbatgames.rubyred.util.Constants;
import com.greenbatgames.rubyred.util.Enums;

/**
 * Created by Quiv on 02-11-2016.
 */

public class MoveComponent extends PlayerComponent
{
    private boolean grounded, jumped;
    private float cannotJumpFor, disableCollisionFor;

    public MoveComponent(Player player) {
        super(player);
    }



    @Override
    public void init() {
        jumped = true;
        grounded = false;
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

        // First check if we should be falling right now
        if (isOnGround() && body.getLinearVelocity().y < -Constants.RUBY_HOP_SPEED / 20f) {
            jump();
            player.animator().setNext(Enums.AnimationState.FALL);
        }

        // Then determine what to do based on if we're in the air or one the ground
        if (isOnGround()) {

            // Special jumping animation preparation
            if (Gdx.input.isKeyPressed(Constants.KEY_LONG)) {
                player.animator().setNext(Enums.AnimationState.LONG_JUMP_PREPARE);
            } else if (Gdx.input.isKeyPressed(Constants.KEY_SPRING)) {
                player.animator().setNext(Enums.AnimationState.SPRING_JUMP_PREPARE);
            } else {
                player.animator().setNext(Enums.AnimationState.LAND);
            }

            // Normal movement controls
            if (Gdx.input.isKeyPressed(Constants.KEY_RIGHT) || Gdx.input.isKeyPressed(Constants.KEY_RIGHT_ALT)) {

                // Check for hopping first, then walking
                if (Gdx.input.isKeyPressed(Constants.KEY_HOP)) {
                    jump();
                    body.setLinearVelocity(
                            Constants.RUBY_HOP_SPEED * MathUtils.cos(MathUtils.degreesToRadians * Constants.RUBY_HOP_ANGLE_RIGHT),
                            Constants.RUBY_HOP_SPEED * MathUtils.sin(MathUtils.degreesToRadians * Constants.RUBY_HOP_ANGLE_RIGHT)
                    );
                    player.animator().setNext(Enums.AnimationState.HOP);

                    return true;
                } else {
                    body.setLinearVelocity(
                            Constants.RUBY_WALK_SPEED,
                            body.getLinearVelocity().y
                    );
                    player.animator().setNext(Enums.AnimationState.WALK);
                }

            } else if (Gdx.input.isKeyPressed(Constants.KEY_LEFT) || Gdx.input.isKeyPressed(Constants.KEY_LEFT_ALT)) {

                // Check for hopping first, then walking
                if (Gdx.input.isKeyPressed(Constants.KEY_HOP)) {
                    jump();
                    body.setLinearVelocity(
                            Constants.RUBY_HOP_SPEED * MathUtils.cos(MathUtils.degreesToRadians * Constants.RUBY_HOP_ANGLE_LEFT),
                            Constants.RUBY_HOP_SPEED * MathUtils.sin(MathUtils.degreesToRadians * Constants.RUBY_HOP_ANGLE_LEFT)
                    );
                    player.animator().setNext(Enums.AnimationState.HOP);

                    return true;
                } else {
                    body.setLinearVelocity(
                            -Constants.RUBY_WALK_SPEED,
                            body.getLinearVelocity().y
                    );
                    player.animator().setNext(Enums.AnimationState.WALK);
                }
            }
        } else {
            if (isInAir())
            {
                if (Gdx.input.isKeyPressed(Constants.KEY_RIGHT) || Gdx.input.isKeyPressed(Constants.KEY_RIGHT_ALT)) {
                    body.setLinearVelocity(
                            MathUtils.clamp(
                                    body.getLinearVelocity().x + Constants.RUBY_HOP_SPEED * Gdx.graphics.getDeltaTime(),
                                    -Constants.RUBY_MAX_HORIZ_HOP_SPEED,
                                    Constants.RUBY_MAX_HORIZ_HOP_SPEED),
                            body.getLinearVelocity().y
                    );
                } else if (Gdx.input.isKeyPressed(Constants.KEY_LEFT) || Gdx.input.isKeyPressed(Constants.KEY_LEFT_ALT)) {
                    body.setLinearVelocity(
                            MathUtils.clamp(
                                    body.getLinearVelocity().x - Constants.RUBY_HOP_SPEED * Gdx.graphics.getDeltaTime(),
                                    -Constants.RUBY_MAX_HORIZ_HOP_SPEED,
                                    Constants.RUBY_MAX_HORIZ_HOP_SPEED),
                            body.getLinearVelocity().y
                    );
                } else {
                    body.setLinearVelocity(
                            body.getLinearVelocity().x * Constants.RUBY_HORIZONTAL_FALL_DAMPEN,
                            body.getLinearVelocity().y
                    );
                }

            } else {
                body.setLinearVelocity(
                        body.getLinearVelocity().x * Constants.RUBY_HORIZONTAL_WALK_DAMPEN,
                        body.getLinearVelocity().y
                );
            }
        }

        // Special jumping movement
        if (isOnGround() && (Gdx.input.isKeyJustPressed(Constants.KEY_JUMP) || Gdx.input.isKeyJustPressed(Constants.KEY_JUMP_ALT))) {
            jump();

            // Spring Jumps
            if (Gdx.input.isKeyPressed(Constants.KEY_SPRING)) {
                body.applyForceToCenter(0f, Constants.RUBY_SPRING_JUMP_IMPULSE, true);
                player.animator().setNext(Enums.AnimationState.SPRING_JUMP);    // Spring Jump animation

            // Long Jumps
            } else if (Gdx.input.isKeyPressed(Constants.KEY_LONG)) {
                float angle;

                if (player.facingRight)
                    angle = MathUtils.degreesToRadians * Constants.RUBY_LONG_JUMP_ANGLE_RIGHT;
                else
                    angle = MathUtils.degreesToRadians * Constants.RUBY_LONG_JUMP_ANGLE_LEFT;

                body.applyForceToCenter(
                        Constants.RUBY_LONG_JUMP_IMPULSE * MathUtils.cos(angle),
                        Constants.RUBY_LONG_JUMP_IMPULSE * MathUtils.sin(angle),
                        true);

                player.animator().setNext(Enums.AnimationState.LONG_JUMP);  // Long jump animation

            // Standard Jumps
            } else {
                body.applyForceToCenter(0f, Constants.RUBY_JUMP_IMPULSE, true);

                player.animator().setNext(Enums.AnimationState.HOP);        // Normal jump animation
            }
        }

        return true;
    }



    public void land() {

        Gdx.app.log("MoveComp", "land triggered");

        grounded = true;
        jumped = false;

        cannotJumpFor = Constants.RUBY_JUMP_RECOVERY;
        player.animator().setNext(Enums.AnimationState.LAND);
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
}
