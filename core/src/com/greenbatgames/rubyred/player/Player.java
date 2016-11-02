package com.greenbatgames.rubyred.player;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.greenbatgames.rubyred.entity.Initializeable;
import com.greenbatgames.rubyred.entity.PhysicsBody;
import com.greenbatgames.rubyred.util.Constants;

/**
 * Created by Quiv on 10-08-2016.
 */
public class Player extends PhysicsBody implements Initializeable
{
    private Vector2 spawnPosition;
    private Rectangle bounds;
    private int lives;

    private JumpComponent jumper;
    private ClimbComponent climber;

    boolean facingRight;

    public Player(float x, float y, float width, float height, World world)
    {
        super(x, y, width, height, world);
        spawnPosition = new Vector2(x, y);
        bounds = new Rectangle(x, y, width, height);
        lives = Constants.RUBY_STARTING_LIVES;

        jumper = new JumpComponent(this);
        climber = new ClimbComponent(this);

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

        jumper.init();
        climber.init();

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
            if (!climber.update(delta)) break;
            if (!jumper.update(delta)) break;
        } while (false);

        // Ensure our dynamic bodies are always awake and ready to be interacted with
        this.body.setAwake(true);
    }



    // JumpComponent accessors
    public void land() { jumper.land(); }
    public void jump() { jumper.jump(); }



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



    /*
        Getters and Setters
     */

    public boolean isCollisionDisabled() { return jumper.isCollisionDisabled(); }
    public void setSpawnPosition(float x, float y) { spawnPosition.set(x, y); }
    public void loseLife() { lives--; }
    public boolean isOutOfLives() { return lives <= 0; }
    public JumpComponent jumper() { return jumper; }
    public ClimbComponent climber() { return climber; }

    public Rectangle getBounds()
    {
        bounds.set(this.getX(), this.getY(), this.getWidth(), this.getHeight());
        return bounds;
    }
}
