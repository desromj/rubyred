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

    // Physics values (do not coorespond to actual possible values as we are working in 2D, not 3D)
    public static final float PLAYER_RADIUS = WORLD_WIDTH / 40.0f;      // 20 pixels, 0.40m with a PTM of 50

    public static final float PLAYER_MASS = 100f;        // mass of 100 kg = 220 lbs
    public static final float PLAYER_VOLUME = 2.90f;     // volume 2.90 m^2, based on body fixtures
    public static final float PLAYER_DENSITY = PLAYER_MASS / PLAYER_VOLUME;     // about 34.45 kg/m^2

    public static final float PLAYER_MAX_WALK_SPEED = 2.78f;        // 2.78 m/s = 10 km/h
    public static final float PLAYER_MAX_RUN_SPEED = 12.5f;         // 12.5 m/s = 45 km/h

    public static final float PLAYER_GROUND_FRICTION = 1f;
    public static final float GROUND_FRICTION_OFFSET_FORCE =
            PLAYER_GROUND_FRICTION * PLAYER_MASS * -GRAVITY;         // Ff = uFn, Fn = mg; Ff = umg

    public static final float PLAYER_WALK_ACCELERATION = 5f;        // about 0.55s to full speed from standing
    public static final float PLAYER_RUN_ACCELERATION = 10f;        // about 1.2s to full speed from standing

    public static final Vector2 PLAYER_WALK_FORCE = new Vector2(PLAYER_MASS * PLAYER_WALK_ACCELERATION + GROUND_FRICTION_OFFSET_FORCE, 0);
    public static final Vector2 PLAYER_RUN_FORCE = new Vector2(PLAYER_MASS * PLAYER_RUN_ACCELERATION + GROUND_FRICTION_OFFSET_FORCE, 0);

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
