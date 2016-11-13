package com.greenbatgames.rubyred.util;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

/**
 * Created by Quiv on 10-08-2016.
 */
public class Constants
{

    private Constants() {}

    /*
        User-Configurable Options
     */

    public static float VOLUME_EFFECTS = 1.0f;
    public static float VOLUME_MUSIC = 1.0f;

    // Controls

    public static int KEY_RESTART = Input.Keys.R;

    public static int KEY_RIGHT = Input.Keys.RIGHT;
    public static int KEY_RIGHT_ALT = Input.Keys.D;

    public static int KEY_LEFT = Input.Keys.LEFT;
    public static int KEY_LEFT_ALT = Input.Keys.A;

    public static int KEY_DOWN = Input.Keys.DOWN;
    public static int KEY_DOWN_ALT = Input.Keys.S;

    public static int KEY_JUMP = Input.Keys.Z;
    public static int KEY_JUMP_ALT = Input.Keys.SPACE;

    public static int KEY_ATTACK = Input.Keys.X;
    public static int KEY_ATTACK_ALT = Input.Keys.E;

    public static int KEY_SPRING = Input.Keys.DOWN;
    public static int KEY_LONG = Input.Keys.SHIFT_LEFT;
    public static int KEY_WALK = Input.Keys.CONTROL_LEFT;

    /*
        World Aspect Ratio
     */

    public static final float WORLD_WIDTH = 640;
    public static final float WORLD_HEIGHT = WORLD_WIDTH * 3f / 4f;

    /*
        Physics and Tiled World Values
     */

    public static final float GRAVITY = -32f;
    public static final float KILL_PLANE_Y = -100f;

    // Pixels-to-metres conversion
    public static final float PTM = 40f;

    // Number of pixels per tile
    public static final float TILED_TILE_SIZE = 32f;
    public static final float TILED_UNIT_SCALE = TILED_TILE_SIZE / PTM;

    public static final float WOBBLE_ROOM = WORLD_WIDTH / 16000f;
    public static final float DISABLE_COLLISION_FOR_PLATFORM = 0.25f;
    public static final float PHYSICS_STEP_FREQ = 1f / 60f;
    public static final int PHYSICS_VEL_ITERATIONS = 6;
    public static final int PHYSICS_POS_ITERATIONS = 6;

    /*
        Ruby Player Values
     */

    public static final int RUBY_STARTING_LIVES = 3;

    public static final float RUBY_DENSITY = 400.0f;
    public static final float RUBY_JUMP_IMPULSE = WORLD_WIDTH * RUBY_DENSITY * 2.5f;
    public static final float RUBY_SPRING_JUMP_IMPULSE = RUBY_JUMP_IMPULSE * 1.25f;
    public static final float RUBY_LONG_JUMP_IMPULSE = RUBY_JUMP_IMPULSE * 2.0f;
    public static final float RUBY_RADIUS = WORLD_WIDTH / 30.0f;

    // Give a bit of leeway between when Ruby lands and when she can jump again
    // 0.1 second accounts for minimum player reaction time
    public static final float RUBY_JUMP_RECOVERY = 0.04f;

    public static final float RUBY_HOP_ANGLE_RIGHT = 45.0f;
    public static final float RUBY_HOP_ANGLE_LEFT = 180.0f - RUBY_HOP_ANGLE_RIGHT;
    public static final float RUBY_LONG_JUMP_ANGLE_RIGHT = 20.0f;
    public static final float RUBY_LONG_JUMP_ANGLE_LEFT = 180.0f - RUBY_LONG_JUMP_ANGLE_RIGHT;

    public static final float RUBY_MOVE_SPEED = WORLD_WIDTH / 54.0f;
    public static final float RUBY_MAX_HORIZ_HOP_SPEED = MathUtils.degreesToRadians * 45.0f * RUBY_MOVE_SPEED;

    // Climbing values
    public static final float RUBY_CLIMB_TIME = 1.8f;
    public static final float RUBY_MAX_CLIMB_RATIO = 1f;
    public static final float RUBY_MIN_CLIMB_RATIO = 0.35f;

    public static final float RUBY_HORIZONTAL_WALK_DAMPEN = 0.90f;
    public static final float RUBY_HORIZONTAL_FALL_DAMPEN = 0.96f;

    public static final float RUBY_VERTEX_X_SCALE = RUBY_RADIUS;
    public static final float RUBY_VERTEX_Y_SCALE = RUBY_RADIUS * 2.0f;

    public static final Vector2[] RUBY_VERTICES_NORMAL = new Vector2[] {
            new Vector2(0.90f * RUBY_VERTEX_X_SCALE / PTM, 0.67f * RUBY_VERTEX_Y_SCALE / PTM),
            new Vector2(0.33f * RUBY_VERTEX_X_SCALE / PTM, 1.00f * RUBY_VERTEX_Y_SCALE / PTM),
            new Vector2(-0.33f * RUBY_VERTEX_X_SCALE / PTM, 1.00f * RUBY_VERTEX_Y_SCALE / PTM),
            new Vector2(-0.90f * RUBY_VERTEX_X_SCALE / PTM, 0.67f * RUBY_VERTEX_Y_SCALE / PTM),
            new Vector2(-0.90f * RUBY_VERTEX_X_SCALE / PTM, -0.67f * RUBY_VERTEX_Y_SCALE / PTM),
            new Vector2(-0.33f * RUBY_VERTEX_X_SCALE / PTM, -1.00f * RUBY_VERTEX_Y_SCALE / PTM),
            new Vector2(0.33f * RUBY_VERTEX_X_SCALE / PTM, -1.00f * RUBY_VERTEX_Y_SCALE / PTM),
            new Vector2(0.90f * RUBY_VERTEX_X_SCALE / PTM, -0.67f * RUBY_VERTEX_Y_SCALE / PTM)
    };

    public static final Vector2[] RUBY_VERTICIES_CROUCHED = new Vector2[] {
            new Vector2(1.20f * RUBY_VERTEX_X_SCALE / PTM, 0.33f * RUBY_VERTEX_Y_SCALE / PTM),
            new Vector2(0.75f * RUBY_VERTEX_X_SCALE / PTM, 0.50f * RUBY_VERTEX_Y_SCALE / PTM),
            new Vector2(-0.75f * RUBY_VERTEX_X_SCALE / PTM, 0.50f * RUBY_VERTEX_Y_SCALE / PTM),
            new Vector2(-1.20f * RUBY_VERTEX_X_SCALE / PTM, 0.33f * RUBY_VERTEX_Y_SCALE / PTM),
            new Vector2(-1.20f * RUBY_VERTEX_X_SCALE / PTM, -0.33f * RUBY_VERTEX_Y_SCALE / PTM),
            new Vector2(-0.75f * RUBY_VERTEX_X_SCALE / PTM, -0.50f * RUBY_VERTEX_Y_SCALE / PTM),
            new Vector2(0.75f * RUBY_VERTEX_X_SCALE / PTM, -0.50f * RUBY_VERTEX_Y_SCALE / PTM),
            new Vector2(1.20f * RUBY_VERTEX_X_SCALE / PTM, -0.33f * RUBY_VERTEX_Y_SCALE / PTM)
    };



    /*
        Camera Controls
     */

    public static final float CHASE_CAM_MOVE_SPEED = WORLD_WIDTH * 2.0f;
    public static final float CHASE_CAM_X_LEEWAY = WORLD_WIDTH / 4f;
    public static final float CHASE_CAM_Y_LEEWAY = WORLD_WIDTH / 10f;



    /*
        Platform object values
     */

    public static final float PLATFORM_EDGE_LEEWAY = WORLD_WIDTH / 160.0f;
    public static final float PLATFORM_COLLISION_LEEWAY = WORLD_WIDTH / 16.0f;

    /*
        Bird object values
     */

    public static final float BIRD_SPAWN_DELAY = 4f;

    public static final float BIRD_WIDTH = WORLD_WIDTH / 25.0f;
    public static final float BIRD_HEIGHT = WORLD_WIDTH / 75.0f;
    public static final float BIRD_MOVE_SPEED = WORLD_WIDTH / 2.0f;
    public static final Vector2 BIRD_KNOCKBACK_IMPULSE = new Vector2(1f, 1f).scl(RUBY_JUMP_IMPULSE * 0.8f);

    /*
        Skylight values
     */

    public static final float SKYLIGHT_LIFETIME = 2.0f;
    public static final float DROP_PLATFORM_LIFETIME = 3.0f;
    public static final float DROP_PLATFORM_RESET_TIME = 1.5f;
    public static final float DROP_PLATFORM_FALL_DURATION = 0.75f;

    /*
        Finish Flag values
     */

    public static final float FINISH_FLAG_WIDTH = TILED_TILE_SIZE;
    public static final float FINISH_FLAG_HEIGHT = TILED_TILE_SIZE * 4.0f;

    /*
        Font values
     */

    public static final Color TOOLTIP_COLOR = Color.BLACK;
    public static final float TOOLTIP_SCALE = 1.0f;
}
