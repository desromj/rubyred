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
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.greenbatgames.rubyred.screen.GameScreen;
import com.greenbatgames.rubyred.util.Constants;

/**
 * Created by Quiv on 28-09-2016.
 */

public class Bird extends Actor
{
    private Vector2 position, velocity;
    private float width, height;
    private boolean hitPlayer;

    public Bird(float x, float y, boolean moveRight) {

        this.position = new Vector2(x, y);
        this.velocity = new Vector2(
                (moveRight) ? Constants.BIRD_MOVE_SPEED : -Constants.BIRD_MOVE_SPEED,
                0f
        );

        this.width = Constants.BIRD_WIDTH;
        this.height = Constants.BIRD_HEIGHT;

        this.hitPlayer = false;

        GameScreen.instance.addActorToStage(this);
    }

    @Override
    public void act(float delta)
    {
        super.act(delta);

        this.position.mulAdd(velocity, delta);

        Player player = GameScreen.instance.getPlayer();

        // If player is hit, add impact recoil
        if(
                !this.hitPlayer
                && Math.abs(player.getX() - position.x) < Constants.RUBY_RADIUS + width / 2.0f
                && Math.abs(player.getY() - position.y) < (Constants.RUBY_RADIUS * 2.0f) + height / 2.0f
                )
        {
            boolean pushRight = (player.getX() > this.position.x);

            // Cancel velocity and apply knockback force
            player.getBody().setLinearVelocity(0f, 0f);
            player.getBody().applyForceToCenter(
                    (pushRight) ? Constants.BIRD_KNOCKBACK_IMPULSE.x : -Constants.BIRD_KNOCKBACK_IMPULSE.x,
                    Constants.BIRD_KNOCKBACK_IMPULSE.y,
                    true
            );
            this.hitPlayer = true;
        }

        // If bird is out of range of the player (2 screen widths), destroy it
        if (
            Math.abs(Vector2.dst(
                    player.getX(), player.getY(),
                    position.x, position.y))
            > Constants.WORLD_WIDTH * 1.25f)
        {
            this.remove();
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
                this.position.x - this.width / 2.0f,
                this.position.y - this.height / 2.0f,
                this.width,
                this.height);

        renderer.end();

        batch.begin();
    }

}
