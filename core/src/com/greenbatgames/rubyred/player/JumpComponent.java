package com.greenbatgames.rubyred.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.physics.box2d.Body;
import com.greenbatgames.rubyred.entity.Initializeable;
import com.greenbatgames.rubyred.util.Constants;

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
    public void update(float delta)
    {
        cannotJumpFor -= delta;
        disableCollisionFor -= delta;

        // First handle recovery time for landing
        if (!canJump()) return;

        // Horizontal hopping movement
        Body body = player.getBody();

        if (isOnGround()) {
            if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {

                jump();
                body.setLinearVelocity(
                        Constants.RUBY_MOVE_SPEED * MathUtils.cos(MathUtils.degreesToRadians * Constants.RUBY_HOP_ANGLE_RIGHT),
                        Constants.RUBY_MOVE_SPEED * MathUtils.sin(MathUtils.degreesToRadians * Constants.RUBY_HOP_ANGLE_RIGHT)
                );

                player.facingRight = true;
                return;

            } else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {

                jump();
                body.setLinearVelocity(
                        Constants.RUBY_MOVE_SPEED * MathUtils.cos(MathUtils.degreesToRadians * Constants.RUBY_HOP_ANGLE_LEFT),
                        Constants.RUBY_MOVE_SPEED * MathUtils.sin(MathUtils.degreesToRadians * Constants.RUBY_HOP_ANGLE_LEFT)
                );

                player.facingRight = false;
                return;
            }
        } else {
            if (isInAir())
            {

                if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
                    body.setLinearVelocity(
                            MathUtils.clamp(
                                    body.getLinearVelocity().x + Constants.RUBY_MOVE_SPEED * Gdx.graphics.getDeltaTime(),
                                    -Constants.RUBY_MAX_HORIZ_HOP_SPEED,
                                    Constants.RUBY_MAX_HORIZ_HOP_SPEED),
                            body.getLinearVelocity().y
                    );
                } else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
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

            if (Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT)) {
                body.applyForceToCenter(0f, Constants.RUBY_SPRING_JUMP_IMPULSE, true);
            } else if (Gdx.input.isKeyPressed(Input.Keys.CONTROL_LEFT)) {
                float angle;

                if (player.facingRight)
                    angle = MathUtils.degreesToRadians * Constants.RUBY_LONG_JUMP_ANGLE_RIGHT;
                else
                    angle = MathUtils.degreesToRadians * Constants.RUBY_LONG_JUMP_ANGLE_LEFT;

                body.applyForceToCenter(
                        Constants.RUBY_LONG_JUMP_IMPULSE * MathUtils.cos(angle),
                        Constants.RUBY_LONG_JUMP_IMPULSE * MathUtils.sin(angle),
                        true);
            } else {
                body.applyForceToCenter(0f, Constants.RUBY_JUMP_IMPULSE, true);
            }
        }
    }



    public void land() {
        grounded = true;
        jumped = false;

        cannotJumpFor = Constants.RUBY_JUMP_RECOVERY;
    }



    public void jump() {
        // cannot jump if we already jumped
        if (jumped)
            return;

        // If jumping down through a platform, disable collision and return
        if (grounded && Gdx.input.isKeyPressed(Input.Keys.DOWN))
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
