package com.greenbatgames.rubyred.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;
import com.greenbatgames.rubyred.util.Constants;
import com.greenbatgames.rubyred.util.Enums;

/**
 * Created by Quiv on 02-11-2016.
 */

public class JumpComponent extends PlayerComponent
{
    private boolean grounded, jumped;
    private float cannotJumpFor, disableCollisionFor;

    public JumpComponent(Player player) {
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

        if (isOnGround()) {
            if (Gdx.input.isKeyPressed(Constants.KEY_RIGHT) || Gdx.input.isKeyPressed(Constants.KEY_RIGHT_ALT)) {

                jump();
                body.setLinearVelocity(
                        Constants.RUBY_MOVE_SPEED * MathUtils.cos(MathUtils.degreesToRadians * Constants.RUBY_HOP_ANGLE_RIGHT),
                        Constants.RUBY_MOVE_SPEED * MathUtils.sin(MathUtils.degreesToRadians * Constants.RUBY_HOP_ANGLE_RIGHT)
                );
                player.animator().setNext(Enums.AnimationState.HOP);

                return true;

            } else if (Gdx.input.isKeyPressed(Constants.KEY_LEFT) || Gdx.input.isKeyPressed(Constants.KEY_LEFT_ALT)) {

                jump();
                body.setLinearVelocity(
                        Constants.RUBY_MOVE_SPEED * MathUtils.cos(MathUtils.degreesToRadians * Constants.RUBY_HOP_ANGLE_LEFT),
                        Constants.RUBY_MOVE_SPEED * MathUtils.sin(MathUtils.degreesToRadians * Constants.RUBY_HOP_ANGLE_LEFT)
                );
                player.animator().setNext(Enums.AnimationState.HOP);

                return true;
            }
        } else {
            if (isInAir())
            {

                if (Gdx.input.isKeyPressed(Constants.KEY_RIGHT) || Gdx.input.isKeyPressed(Constants.KEY_RIGHT_ALT)) {
                    body.setLinearVelocity(
                            MathUtils.clamp(
                                    body.getLinearVelocity().x + Constants.RUBY_MOVE_SPEED * Gdx.graphics.getDeltaTime(),
                                    -Constants.RUBY_MAX_HORIZ_HOP_SPEED,
                                    Constants.RUBY_MAX_HORIZ_HOP_SPEED),
                            body.getLinearVelocity().y
                    );
                } else if (Gdx.input.isKeyPressed(Constants.KEY_LEFT) || Gdx.input.isKeyPressed(Constants.KEY_LEFT_ALT)) {
                    body.setLinearVelocity(
                            MathUtils.clamp(
                                    body.getLinearVelocity().x - Constants.RUBY_MOVE_SPEED * Gdx.graphics.getDeltaTime(),
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

            if (Gdx.input.isKeyPressed(Constants.KEY_SPRING)) {
                body.applyForceToCenter(0f, Constants.RUBY_SPRING_JUMP_IMPULSE, true);
                player.animator().setNext(Enums.AnimationState.HOP);        // TODO: set to Spring Jump animation
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

                player.animator().setNext(Enums.AnimationState.HOP);        // TODO: Set to Long Jump animation
            } else {
                body.applyForceToCenter(0f, Constants.RUBY_JUMP_IMPULSE, true);

                player.animator().setNext(Enums.AnimationState.HOP);        // TODO: Set to normal jump animation
            }
        }

        return true;
    }



    public void land() {
        grounded = true;
        jumped = false;

        cannotJumpFor = Constants.RUBY_JUMP_RECOVERY;
    }



    public void jump() {
        // cannot jump if we already jumped
        if (jumped) return;

        // If jumping down through a platform, disable collision and return
        if (grounded && (Gdx.input.isKeyPressed(Constants.KEY_DOWN) || Gdx.input.isKeyPressed(Constants.KEY_DOWN_ALT)))
        {
            this.disableCollisionFor = Constants.DISABLE_COLLISION_FOR_PLATFORM;
            return;
        }

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
