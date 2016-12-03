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
    public static int KEY_LEFT = Input.Keys.LEFT;
    public static int KEY_JUMP = Input.Keys.Z;
    public static int KEY_ATTACK = Input.Keys.X;

    /*
        World Aspect Ratio
     */

    public static final float WORLD_WIDTH = 800;
    public static final float WORLD_HEIGHT = WORLD_WIDTH * 4f / 5f;

    public static final float HUD_MARGIN = WORLD_WIDTH / 80f;

    /*
        Physics and Tiled World Values
     */

    public static final float GRAVITY = -9.81f;
    public static final float KILL_PLANE_Y = -100f;

    // Pixels-to-metres conversion
    public static final float PTM = 50f;

    public static final float PHYSICS_STEP_FREQ = 1f / 60f;
    public static final int PHYSICS_VEL_ITERATIONS = 6;
    public static final int PHYSICS_POS_ITERATIONS = 6;

    /*
        Player Values
     */

    // Physics values
    public static final float PLAYER_DENSITY = 400.0f;
    public static final float PLAYER_RADIUS = WORLD_WIDTH / 30.0f;

    // Climbing values
    public static final float PLAYER_CLIMB_TIME = 1.0f;
    public static final float PLAYER_MAX_CLIMB_RATIO = 1f;
    public static final float PLAYER_MIN_CLIMB_RATIO = 0.75f;

    // Time before allowing another jump after landing
    public static final float PLAYER_JUMP_RECOVERY = 0.25f;

    /*
        Camera Controls
     */

    public static final float CHASE_CAM_MOVE_SPEED = WORLD_WIDTH * 2.0f;
    public static final float CHASE_CAM_X_LEEWAY = WORLD_WIDTH / 4f;
    public static final float CHASE_CAM_Y_LEEWAY = WORLD_WIDTH / 10f;

    /*
        Font values
     */

    public static final Color TOOLTIP_COLOR = Color.BLACK;
    public static final float TOOLTIP_SCALE = 1.0f;

    public static final float CHECKPOINT_TEXT_ONSCREEN_TIME = 4f;
}
