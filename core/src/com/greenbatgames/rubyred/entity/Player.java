package com.greenbatgames.rubyred.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.greenbatgames.rubyred.screen.GameScreen;
import com.greenbatgames.rubyred.util.Constants;
import com.greenbatgames.rubyred.util.Utils;

/**
 * Created by Quiv on 10-08-2016.
 */
public class Player extends PhysicsBody
{
    private boolean grounded, jumped, facingRight, crouched;
    private float disableCollisionFor;
    private Vector2 spawnPosition;

    public Player(float x, float y, float width, float height, World world)
    {
        super(x, y, width, height, world);
        spawnPosition = new Vector2(x, y);

        init();
    }



    public void init()
    {
        this.body.setLinearVelocity(0f, 0f);

        this.body.setTransform(
                (spawnPosition.x - getWidth() / 2.0f) / Constants.PTM,
                (spawnPosition.y - getHeight() / 2.0f) / Constants.PTM,
                0f
        );

        jumped = true;
        facingRight = true;
        grounded = false;
        crouched = false;

        disableCollisionFor = 0.0f;
    }



    @Override
    protected void initPhysics(World world) {

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(
                getX() / Constants.PTM,
                getY() / Constants.PTM
        );
        bodyDef.fixedRotation = true;

        this.body = world.createBody(bodyDef);

        PolygonShape shape = new PolygonShape();
        shape.set(Constants.RUBY_VERTICIES_NORMAL);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;
        fixtureDef.density = Constants.RUBY_DENSITY;
        fixtureDef.restitution = 0f;
        fixtureDef.friction = 0f;

        this.body.createFixture(fixtureDef);
        this.body.setUserData(this);

        shape.dispose();
    }



    @Override
    public void update(float delta)
    {
        super.update(delta);

        disableCollisionFor -= delta;
        move();

        // Ensure our dynamic bodies are always awake and ready to be interacted with
        this.body.setAwake(true);
    }



    protected void move()
    {
        // Horizontal movement

        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {

                this.body.setLinearVelocity(
                        Constants.RUBY_MOVE_SPEED / Constants.PTM,
                        this.body.getLinearVelocity().y
                );

            this.facingRight = true;

        } else if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {

                this.body.setLinearVelocity(
                        -Constants.RUBY_MOVE_SPEED / Constants.PTM,
                        this.body.getLinearVelocity().y
                );

            this.facingRight = false;

        } else {
            if (!this.grounded)
            {
                this.body.setLinearVelocity(
                        this.body.getLinearVelocity().x * Constants.RUBY_HORIZONTAL_FALL_DAMPEN,
                        this.body.getLinearVelocity().y
                );
            } else {
                this.body.setLinearVelocity(
                        this.body.getLinearVelocity().x * Constants.RUBY_HORIZONTAL_WALK_DAMPEN,
                        this.body.getLinearVelocity().y
                );
            }
        }

        // Jumping movement

        if (Gdx.input.isKeyJustPressed(Constants.KEY_JUMP) || Gdx.input.isKeyJustPressed(Constants.KEY_JUMP_ALT))
            jump();
    }



    public void land()
    {
        grounded = true;
        jumped = false;
    }



    public void jump()
    {
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

        this.body.applyForceToCenter(0f, Constants.RUBY_JUMP_IMPULSE, true);
    }



    // TODO: Render here


    @Override
    public void draw(Batch batch, float parentAlpha)
    {
        batch.end();

        ShapeRenderer renderer = new ShapeRenderer();
        renderer.setProjectionMatrix(batch.getProjectionMatrix());

        renderer.begin(ShapeRenderer.ShapeType.Filled);

        renderer.setColor(Color.BROWN);
        renderer.rect(
                getX(),
                getY(),
                getWidth(),
                getHeight());

        renderer.end();

        batch.begin();
    }


    @Override
    public void renderShapes(ShapeRenderer renderer) {

    }



    @Override
    public void renderSprites(SpriteBatch batch) {

    }



    // TODO: Sets crouch collision, if crouching needs to be added to the game



    protected void setCrouchCollision(boolean crouching)
    {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;

        bodyDef.fixedRotation = true;

        PolygonShape shape = new PolygonShape();

        if (this.crouched) {
            shape.set(Constants.RUBY_VERTICIES_CROUCHED);
            bodyDef.position.set(
                    this.body.getPosition().x,
                    (this.body.getPosition().y -
                            (Utils.getMaxHeight(Constants.RUBY_VERTICIES_NORMAL)
                                    - Utils.getMaxHeight(Constants.RUBY_VERTICIES_CROUCHED))
                                    / 2.0f)
            );
        } else {
            shape.set(Constants.RUBY_VERTICIES_NORMAL);
            bodyDef.position.set(
                    this.body.getPosition().x,
                    (this.body.getPosition().y -
                            (Utils.getMaxHeight(Constants.RUBY_VERTICIES_CROUCHED)
                                    - Utils.getMaxHeight(Constants.RUBY_VERTICIES_NORMAL))
                                    / 2.0f)
            );
        }

        FixtureDef fixtureDef = new FixtureDef();

        fixtureDef.shape = shape;
        fixtureDef.density = Constants.RUBY_DENSITY;
        fixtureDef.restitution = 0f;
        fixtureDef.friction = 0f;

        GameScreen.instance.queueBodyToCreate(bodyDef, fixtureDef, this);
        GameScreen.instance.queueBodyToDestroy(this.body);
    }



    /*
        Getters and Setters
     */

    public boolean isCollisionDisabled() { return disableCollisionFor > 0f; }
    public void setSpawnPosition(float x, float y) { spawnPosition.set(x, y); }
}
