package com.greenbatgames.rubyred.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.greenbatgames.rubyred.entity.PhysicsBody;

/**
 * Created by Quiv on 10-08-2016.
 */
public class ChaseCam
{
    PhysicsBody target;
    OrthographicCamera camera;
    Boolean following;

    public ChaseCam(OrthographicCamera camera, PhysicsBody target)
    {
        this.target = target;
        this.camera = camera;
        this.following = true;
    }

    public void update(float delta)
    {
        if (Gdx.input.isKeyJustPressed(Input.Keys.SPACE))
        {
            if (!following)
                centreOnTarget();

            following = !following;
        }

        if (following) {
            float
                    xLeeway = Constants.CHASE_CAM_X_LEEWAY / 2.0f,
                    yLeeway = Constants.CHASE_CAM_Y_LEEWAY / 2.0f;

            if (camera.position.x > target.getPosition().x
                    && camera.position.x - target.getPosition().x > xLeeway)
                camera.position.x = target.getPosition().x + xLeeway;

            if (camera.position.x < target.getPosition().x
                    && target.getPosition().x - camera.position.x > xLeeway)
                camera.position.x = target.getPosition().x - xLeeway;

            if (camera.position.y > target.getPosition().y
                    && camera.position.y - target.getPosition().y > yLeeway)
                camera.position.y = target.getPosition().y + yLeeway;

            if (camera.position.y < target.getPosition().y
                    && target.getPosition().y - camera.position.y > yLeeway)
                camera.position.y = target.getPosition().y - yLeeway;

        } else {
            if (Gdx.input.isKeyPressed(Input.Keys.A)) {
                camera.position.x -= delta * Constants.CHASE_CAM_MOVE_SPEED;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.D)) {
                camera.position.x += delta * Constants.CHASE_CAM_MOVE_SPEED;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.W)) {
                camera.position.y += delta * Constants.CHASE_CAM_MOVE_SPEED;
            }
            if (Gdx.input.isKeyPressed(Input.Keys.S)) {
                camera.position.y -= delta * Constants.CHASE_CAM_MOVE_SPEED;
            }
        }
    }



    private void centreOnTarget()
    {
        camera.position.x = target.getPosition().x;
        camera.position.y = target.getPosition().y;
    }



    public float getX() {return camera.position.x - camera.viewportWidth / 2.0f;}
    public float getY() {return camera.position.y - camera.viewportHeight / 2.0f;}
    public float getWidth() {return camera.viewportWidth;}
    public float getHeight() {return camera.viewportHeight;}
    public OrthographicCamera getCamera() {return camera;}
}
