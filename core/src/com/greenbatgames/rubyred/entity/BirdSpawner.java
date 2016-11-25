package com.greenbatgames.rubyred.entity;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.greenbatgames.rubyred.screen.GameScreen;
import com.greenbatgames.rubyred.util.Constants;

/**
 * Created by Quiv on 28-09-2016.
 */

public class BirdSpawner extends Actor
{
    private Vector2 position;
    private float spawnDelay;

    public BirdSpawner(float x, float y)
    {
        this.position = new Vector2(x, y);
        this.spawnDelay = 0.0f;
    }



    @Override
    public void act(float delta)
    {
        com.greenbatgames.rubyred.player.Player player = GameScreen.currentLevel().getPlayer();

        // Skip spawn if it is too far offscreen
        float dist = Math.abs(Vector2.dst(
                player.getX(), player.getY(),
                position.x, position.y));

        if (dist > Constants.WORLD_WIDTH * 1.5f || dist < Constants.WORLD_WIDTH * 0.6f)
        {
            return;
        }

        // Otherwise proceed with spawning as normal
        spawnDelay -= delta;

        if (spawnDelay < 0.0f)
        {
            boolean moveRight;

            if (player.getX() < position.x)
                moveRight = false;
            else
                moveRight = true;

            spawnBird(moveRight);
            spawnDelay = Constants.BIRD_SPAWN_DELAY;
        }
    }



    private void spawnBird(boolean moveRight)
    {
        new Bird(
                this.position.x,
                this.position.y,
                moveRight
        );
    }
}
