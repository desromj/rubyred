package com.greenbatgames.rubyred.util;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
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

    public static int KEY_RIGHT = Input.Keys.RIGHT;
    public static int KEY_RIGHT_ALT = Input.Keys.D;

    public static int KEY_LEFT = Input.Keys.LEFT;
    public static int KEY_LEFT_ALT = Input.Keys.A;

    public static int KEY_JUMP = Input.Keys.Z;
    public static int KEY_JUMP_ALT = Input.Keys.SPACE;

    public static int KEY_ATTACK = Input.Keys.X;
    public static int KEY_ATTACK_ALT = Input.Keys.E;

    public static int KEY_CROUCH = Input.Keys.SHIFT_LEFT;
    public static int KEY_CROUCH_ALT = Input.Keys.CONTROL_LEFT;

    /*
        World Aspect Ratio
     */

    public static final float WORLD_WIDTH = 320;
    public static final float WORLD_HEIGHT = WORLD_WIDTH * 3f / 4f;

    public static final Color BG_COLOR = Color.SKY;

    /*
        Physics and Tiled World Values
     */

    public static final float GRAVITY = -20f;

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

    public static final float RUBY_DENSITY = 400.0f;
    public static final float RUBY_JUMP_IMPULSE = WORLD_WIDTH * RUBY_DENSITY * 0.60f;
    public static final float RUBY_RADIUS = WORLD_WIDTH / 32.0f;

    public static final float RUBY_MOVE_SPEED = WORLD_WIDTH / 3.0f;

    public static final float RUBY_HORIZONTAL_WALK_DAMPEN = 0.80f;
    public static final float RUBY_HORIZONTAL_FALL_DAMPEN = 0.90f;

    public static final float RUBY_VERTEX_X_SCALE = RUBY_RADIUS;
    public static final float RUBY_VERTEX_Y_SCALE = RUBY_RADIUS * 2.0f;

    public static final Vector2[] RUBY_VERTICIES_NORMAL = new Vector2[] {
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
        Animation Helper Values
     */

    public static final float LAND_SPEED_THRESHOLD = RUBY_MOVE_SPEED / 20000.0f;
    public static final float RUBY_IDLE_SPEED_THRESHOLD = 25f;
    public static final float RUBY_WALK_SPEED_THRESHOLD = RUBY_MOVE_SPEED * 2f;




    /*
        Camera Controls
     */

    public static final float CHASE_CAM_MOVE_SPEED = WORLD_WIDTH * 2.0f;
    public static final float CHASE_CAM_X_LEEWAY = WORLD_WIDTH / 4f;
    public static final float CHASE_CAM_Y_LEEWAY = WORLD_WIDTH / 10f;



    /*
        Platform object values
     */

    public static final Color PLATFORM_COLOR = Color.BLUE;
    public static final float PLATFORM_EDGE_LEEWAY = WORLD_WIDTH / 60.0f;
    public static final float PLATFORM_COLLISION_LEEWAY = WORLD_WIDTH / 960.0f;

}
