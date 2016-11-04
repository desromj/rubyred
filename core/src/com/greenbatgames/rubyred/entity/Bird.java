package com.greenbatgames.rubyred.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.utils.TimeUtils;
import com.greenbatgames.rubyred.screen.GameScreen;
import com.greenbatgames.rubyred.util.Constants;

/**
 * Created by Quiv on 28-09-2016.
 */

public class Bird extends Actor
{
    public static final String TAG = Bird.class.getSimpleName();

    private static final int FRAME_COLS = 6;
    private static final int FRAME_ROWS = 2;

    private Animation animation;
    private Texture spriteSheet;
    private TextureRegion [] flyFrames;
    private TextureRegion currentFrame;
    private long startTime;

    private Vector2 position, velocity;
    private float width, height;
    private boolean hitPlayer;
    private Rectangle aabb;

    public Bird(float x, float y, boolean moveRight) {

        this.startTime = TimeUtils.nanoTime();
        this.position = new Vector2(x, y);
        this.velocity = new Vector2(
                (moveRight) ? Constants.BIRD_MOVE_SPEED : -Constants.BIRD_MOVE_SPEED,
                0f
        );

        this.width = Constants.BIRD_WIDTH;
        this.height = Constants.BIRD_HEIGHT;

        this.hitPlayer = false;
        this.aabb = new Rectangle(x, y, this.width, this.height);

        initAnimation();

        GameScreen.getInstance().addActorToStage(this);
    }



    private void initAnimation()
    {
        spriteSheet = new Texture(Gdx.files.internal("sprites/birds.32x32.png"));

        TextureRegion [][] temp = TextureRegion.split(
                spriteSheet,
                spriteSheet.getWidth() / FRAME_COLS,
                spriteSheet.getHeight() / FRAME_ROWS);

        flyFrames = new TextureRegion[FRAME_COLS - 1];

        for (int i = 0; i < FRAME_COLS - 1; i++)
            flyFrames[i] = temp[1][i];

        currentFrame = flyFrames[0];
        animation = new Animation(1f / 10f, flyFrames);
    }



    @Override
    public void act(float delta)
    {
        super.act(delta);

        // Animation controls
        float elapsedTime = (TimeUtils.nanoTime() - startTime) * MathUtils.nanoToSec;
        currentFrame = animation.getKeyFrame(elapsedTime, true);

        // Rest of updates
        this.position.mulAdd(velocity, delta);

        com.greenbatgames.rubyred.player.Player player = GameScreen.getInstance().getPlayer();

        this.aabb.set(position.x, position.y, width, height);

        // If player is hit, add impact recoil
        if(!this.hitPlayer && this.aabb.overlaps(player.getBounds()))
        {
            boolean pushRight = (player.getX() > this.position.x);

            // Cancel velocity and apply knockback force
            player.getBody().setLinearVelocity(0f, 0f);
            player.getBody().applyForceToCenter(
                    (pushRight) ? Constants.BIRD_KNOCKBACK_IMPULSE.x : -Constants.BIRD_KNOCKBACK_IMPULSE.x,
                    Constants.BIRD_KNOCKBACK_IMPULSE.y,
                    true
            );
            player.jumper().jump();

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
        batch.draw(
                currentFrame.getTexture(),
                position.x,
                position.y,
                position.x + width / 2.0f,
                position.y + height / 2.0f,
                currentFrame.getRegionWidth(),
                currentFrame.getRegionHeight(),
                1f,
                1f,
                0f,
                currentFrame.getRegionX(),
                currentFrame.getRegionY(),
                currentFrame.getRegionWidth(),
                currentFrame.getRegionHeight(),
                isMovingRight() ? true : false,
                false);
    }

    private boolean isMovingRight() {
        return this.velocity.x > 0f;
    }

}
