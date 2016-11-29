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

    private boolean grounded, jumped;
    private float cannotJumpFor, disableCollisionFor;
    private Vector2 rayFrom, rayTo;

    public MoveComponent(Player player) {
        super(player);
    }



    @Override
    public void init() {
        jumped = false;
        grounded = true;
        cannotJumpFor = 0.0f;
        disableCollisionFor = 0.0f;
        rayFrom = new Vector2();
        rayTo = new Vector2();
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

        // First check if we should be stationary right now
        if (player.getY() == player.getLastY() && body.getLinearVelocity().y == 0f) {
            land();
            player.animator().setNext(Enums.AnimationState.LAND);
        }
        // Then check if we should be falling right now
        else if (isOnGround() && body.getLinearVelocity().y < -Constants.RUBY_HOP_SPEED / 20f) {
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

        handleCollision();
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

        cannotJumpFor = Constants.RUBY_JUMP_RECOVERY;
        player.animator().setNext(Enums.AnimationState.LAND);
    }




    // Use box2d raycasting to check collision with the ground
    private void handleCollision() {
        // Only check for landing on the way down, while we're airborne
        if (player.getBody().getLinearVelocity().y >= 0 || isOnGround()) return;

        World world = GameScreen.currentLevel().getWorld();
        float under = player.getBottom() / Constants.PTM;
        float editRatio = 0.02f;           // Percent to affect above/below bottom to account for physics wobbles

        // Ray trace from bottom middle of player to just below the bottom middle
        rayFrom.set(
                (player.getX() + player.getWidth() / 2.0f) / Constants.PTM,
                under + under * editRatio);

        rayTo.set(
                rayFrom.x,
                under - under * editRatio);

        // Do left and right ray casts for landing
        world.rayCast(makeRayCastCallback(), rayFrom, rayTo);

        // If we collided, return. Otherwise, check the left and right edges for collision as well
        if (isOnGround()) return;

        // Left edge
        rayFrom.set(
                player.getLeft() / Constants.PTM,
                under + under * editRatio);

        rayTo.set(
                rayFrom.x,
                under - under * editRatio);

        world.rayCast(makeRayCastCallback(), rayFrom, rayTo);

        if (isOnGround()) return;

        // Right edge
        rayFrom.set(
                player.getRight() / Constants.PTM,
                under + under * editRatio);

        rayTo.set(
                rayFrom.x,
                under - under * editRatio);

        world.rayCast(makeRayCastCallback(), rayFrom, rayTo);
    }



    // Raycast collision handling
    private RayCastCallback makeRayCastCallback() {

        return new RayCastCallback() {
            @Override
            public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) {

                Gdx.app.log(TAG, "Moving raycast triggered.");

                Object userData = fixture.getBody().getUserData();

                // Ignore intersections with ourself
                if (userData == player) {
                    Gdx.app.log(TAG, "Ignoring User Data");
                    return 1;
                }

                // Cause the player to land
                land();

                // Activate any activateable objects the player lands on
                if (userData instanceof Activateable)
                    ((Activateable) userData).activate();

                return 0;
            }
        };
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
