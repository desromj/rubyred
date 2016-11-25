package com.greenbatgames.rubyred.player;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Fixture;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.RayCastCallback;
import com.badlogic.gdx.physics.box2d.World;
import com.greenbatgames.rubyred.entity.Activateable;
import com.greenbatgames.rubyred.entity.Initializeable;
import com.greenbatgames.rubyred.entity.PhysicsBody;
import com.greenbatgames.rubyred.screen.GameScreen;
import com.greenbatgames.rubyred.util.Constants;

/**
 * Created by Quiv on 10-08-2016.
 */
public class Player extends PhysicsBody implements Initializeable
{
    private Vector2 spawnPosition;
    private Rectangle bounds;
    private int lives;

    private MoveComponent mover;
    private ClimbComponent climber;
    private AnimationComponent animator;

    boolean facingRight;

    public Player(float x, float y, float width, float height, World world)
    {
        super(x, y, width, height, world);

        spawnPosition = new Vector2(x, y);
        bounds = new Rectangle(x, y, width, height);
        lives = Constants.RUBY_STARTING_LIVES;

        mover = new MoveComponent(this);
        climber = new ClimbComponent(this);
        animator = new AnimationComponent(this);

        init();
    }


    @Override
    public void init()
    {
        this.body.setLinearVelocity(0f, 0f);

        this.body.setTransform(
                (spawnPosition.x + getWidth() / 2.0f) / Constants.PTM,
                (spawnPosition.y + getHeight() / 2.0f) / Constants.PTM,
                0f
        );

        mover.init();
        climber.init();
        animator.init();

        facingRight = true;
    }



    @Override
    protected void initPhysics(World world) {

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(
                (getX() + getWidth() / 2.0f) / Constants.PTM,
                (getY() + getHeight() / 2.0f) / Constants.PTM
        );
        bodyDef.fixedRotation = true;

        this.body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.set(Constants.RUBY_VERTICES_NORMAL);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = Constants.RUBY_DENSITY;
        fixtureDef.restitution = 0f;
        fixtureDef.friction = 1f;

        this.body.createFixture(fixtureDef);
        this.body.setUserData(this);

        shape.dispose();
    }



    @Override
    public void act(float delta)
    {
        super.act(delta);

        // Run Component updates in sequence, and break through
        // any later updates if we receive a returned request to
        do {
            if (!animator.update(delta)) break;
            if (!climber.update(delta)) break;
            if (!mover.update(delta)) break;
        } while (false);

        // Handle collisions to objects via raycasts
        handleCollisions();

        // Set the direction facing based on x velocity, only if not recoiling
        if (animator.getNextLabel().compareTo("recoil") != 0) {
            if (body.getLinearVelocity().x > 0.1f)
                facingRight = true;
            else if (body.getLinearVelocity().x < -0.1f)
                facingRight = false;
        }

        // Ensure our dynamic bodies are always awake and ready to be interacted with
        this.body.setAwake(true);
    }



    // Use box2d raycasting to check collision with the ground
    private void handleCollisions() {
        // Only check for landing on the way down, while we're airborne
        if (body.getLinearVelocity().y >= 0 || Player.this.mover.isOnGround()) return;

        World world = GameScreen.currentLevel().getWorld();
        float bot = getBottom() / Constants.PTM;

        // Ray trace from bottom of player to just below the bottom
        Vector2 rayFromRight = new Vector2(
                getRight() / Constants.PTM,
                bot);

        Vector2 rayToRight = new Vector2(
                getRight() / Constants.PTM,
                bot - bot * 0.02f);

        Vector2 rayFromLeft = new Vector2(
                getLeft() / Constants.PTM,
                bot);

        Vector2 rayToLeft = new Vector2(
                getLeft() / Constants.PTM,
                bot - bot * 0.02f);

        // Do left and right ray casts for landing
        world.rayCast(makeRayCastCallback(), rayFromRight, rayToRight);
        world.rayCast(makeRayCastCallback(), rayFromLeft, rayToLeft);
    }



    // Raycast collision handling
    private RayCastCallback makeRayCastCallback() {

        return new RayCastCallback() {
            @Override
            public float reportRayFixture(Fixture fixture, Vector2 point, Vector2 normal, float fraction) {

                Gdx.app.log(TAG, "Raycast triggered.");

                Object userData = fixture.getBody().getUserData();

                // Ignore intersections with ourself
                if (userData == Player.this) {
                    Gdx.app.log(TAG, "Ignoring User Data");
                    return 1;
                }

                // Cause the player to land
                Player.this.mover.land();

                // Activate any activateable objects the player lands on
                if (userData instanceof Activateable)
                    ((Activateable) userData).activate();

                return 0;
            }
        };
    }



    @Override
    public void draw(Batch batch, float parentAlpha) {
        animator.asset.render(batch);
    }



    /*
        Getters and Setters
     */

    public boolean isCollisionDisabled() { return mover.isCollisionDisabled(); }
    public void setSpawnPosition(float x, float y) { spawnPosition.set(x, y); }
    public void loseLife() { lives--; }
    public boolean isOutOfLives() { return lives <= 0; }
    public int getLives() { return lives; }

    public MoveComponent mover() { return mover; }
    public ClimbComponent climber() { return climber; }
    public AnimationComponent animator() { return animator; }

    public boolean isMoveButtonHeld() {
        return Gdx.input.isKeyPressed(Constants.KEY_JUMP)
                || Gdx.input.isKeyPressed(Constants.KEY_JUMP_ALT);
    }

    public boolean isClimbButtonHeld() {
        return Gdx.input.isKeyPressed(Constants.KEY_ATTACK)
                || Gdx.input.isKeyPressed(Constants.KEY_ATTACK_ALT);
    }

    public Rectangle getBounds() {
        bounds.set(this.getX(), this.getY(), this.getWidth(), this.getHeight());
        return bounds;
    }
}
