package com.greenbatgames.rubyred.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.greenbatgames.rubyred.screen.GameScreen;
import com.greenbatgames.rubyred.util.Constants;

/**
 * Created by Quiv on 28-09-2016.
 */

public class Bird extends PhysicsBody
{
    private boolean moveRight;
    private Vector2 velocity;

    public Bird(float x, float y, boolean moveRight) {
        super(
                x,
                y,
                Constants.BIRD_WIDTH,
                Constants.BIRD_HEIGHT,
                null);

        this.moveRight = moveRight;

        Gdx.app.log(TAG, "Bird object created");
    }

    @Override
    protected void initPhysics(World world) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.DynamicBody;
        bodyDef.position.set(
                getX() / Constants.PTM,
                getY() / Constants.PTM);

        bodyDef.fixedRotation = true;

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(
                Constants.BIRD_WIDTH / Constants.PTM,
                Constants.BIRD_HEIGHT / Constants.PTM);

        FixtureDef fixtureDef = new FixtureDef();

        fixtureDef.shape = shape;
        fixtureDef.density = Constants.BIRD_DENSITY;
        fixtureDef.restitution = 0f;
        fixtureDef.friction = 0f;

        GameScreen.instance.queueBodyToCreate(bodyDef, fixtureDef, this);
        GameScreen.instance.addActorToStage(this);

        this.velocity = new Vector2(
                (this.moveRight) ? Constants.BIRD_MOVE_SPEED : -Constants.BIRD_MOVE_SPEED,
                0f
        );
    }

    @Override
    public void act(float delta)
    {
        super.act(delta);

        velocity = velocity.scl(delta);
        Player player = GameScreen.instance.getPlayer();
        this.body.setLinearVelocity(velocity);

        // Destroy the body if we are farther away than twice the world width
        if (
            Math.abs(Vector2.dst(
                    player.getX(), player.getY(),
                    this.getX(), this.getY()))
            > Constants.WORLD_WIDTH * 2.0f)
        {
            GameScreen.instance.queueBodyToDestroy(this);

            Gdx.app.log(TAG, "Bird object destroyed");
            Gdx.app.log(TAG, "Player x,y: " + player.getX() + ", " + player.getY());
            Gdx.app.log(TAG, "Bird x,y: " + this.getX() + ", " + this.getY());
            Gdx.app.log(TAG, "World Width: " + Constants.WORLD_WIDTH * 2.0f);


        }
    }


    @Override
    public void draw(Batch batch, float parentAlpha)
    {
        batch.end();

        ShapeRenderer renderer = new ShapeRenderer();
        renderer.setProjectionMatrix(batch.getProjectionMatrix());

        renderer.begin(ShapeRenderer.ShapeType.Filled);

        renderer.setColor(Color.BLUE);
        renderer.rect(
                getX(),
                getY(),
                getWidth(),
                getHeight());

        renderer.end();

        batch.begin();
    }

}
