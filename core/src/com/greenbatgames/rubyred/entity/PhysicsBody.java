package com.greenbatgames.rubyred.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.Shape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.greenbatgames.rubyred.util.Constants;

/**
 * Created by Quiv on 10-08-2016.
 */
public abstract class PhysicsBody extends Actor
{
    public static final String TAG = PhysicsBody.class.getSimpleName();

    private Vector2 position, lastPosition;
    private float width, height;
    private boolean atRest;

    protected Body body;

    public PhysicsBody(float x, float y, float width, float height, World world)
    {
        this.position = new Vector2(x, y);
        this.lastPosition = new Vector2(x, y);

        this.width = width;
        this.height = height;

        this.atRest = false;
        this.body = null;

        initPhysics(world);
    }

    protected abstract void initPhysics(World world);
    public abstract void renderShapes(ShapeRenderer renderer);
    public abstract void renderSprites(SpriteBatch batch);

    public void update(float delta)
    {
        // Update our last position for the next frame
        lastPosition.set(position.x, position.y);

        // Cling this object's position to the physics body
        position.set(
                (body.getPosition().x * Constants.PTM) - width / 2.0f,
                (body.getPosition().y * Constants.PTM) - height / 2.0f
        );

        // Check position last frame compared to this frame in update, within a variance.
        if (position.dst(lastPosition) < Constants.WOBBLE_ROOM)
            atRest = true;
        else
            atRest = false;
    }



    /*
        Getters and Setters
     */



    public void setBody(Body body) { this.body = body; }
    public Body getBody() { return body; }

    public boolean isAtRest() { return atRest; }

    // Width and Height use Body bounds from below
    public float getWidth() { return width; }
    public float getHeight() { return height; }

    public float getX() { return position.x; }
    public float getY() { return position.y; }
    public float getLastX() { return lastPosition.x; }
    public float getLastY() { return lastPosition.y; }

    /*
        Get Bounds based on Body Shape
     */
    public float getBottom()
    {
        Shape shape = this.body.getFixtureList().first().getShape();

        PolygonShape poly = (PolygonShape) shape;
        float lowest = 0f;
        Vector2 vert = new Vector2();

        for (int i = 0; i < poly.getVertexCount(); i++)
        {
            poly.getVertex(i, vert);

            if (i == 0 || vert.y < lowest)
                lowest = vert.y;
        }

        return position.y + lowest * Constants.PTM;
    }



    public float getTop()
    {
        Shape shape = this.body.getFixtureList().first().getShape();

        PolygonShape poly = (PolygonShape) shape;
        float highest = 0f;
        Vector2 vert = new Vector2();

        for (int i = 0; i < poly.getVertexCount(); i++)
        {
            poly.getVertex(i, vert);

            if (i == 0 || vert.y > highest)
                highest = vert.y;
        }

        return position.y + highest * Constants.PTM;
    }



    public float getLeft()
    {
        Shape shape = this.body.getFixtureList().first().getShape();

        PolygonShape poly = (PolygonShape) shape;
        float leftest = 0f;
        Vector2 vert = new Vector2();

        for (int i = 0; i < poly.getVertexCount(); i++)
        {
            poly.getVertex(i, vert);

            if (i == 0 || vert.x < leftest)
                leftest = vert.x;
        }

        return position.y + leftest * Constants.PTM;
    }



    public float getRight()
    {
        Shape shape = this.body.getFixtureList().first().getShape();

        PolygonShape poly = (PolygonShape) shape;
        float rightest = 0f;
        Vector2 vert = new Vector2();

        for (int i = 0; i < poly.getVertexCount(); i++)
        {
            poly.getVertex(i, vert);

            if (i == 0 || vert.x > rightest)
                rightest = vert.x;
        }

        return position.y + rightest * Constants.PTM;
    }
}
