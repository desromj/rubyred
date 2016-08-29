package com.greenbatgames.rubyred.entity;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.greenbatgames.rubyred.util.Constants;

/**
 * Created by Quiv on 11-08-2016.
 */
public class Platform extends PhysicsBody
{
    private float left, right, top, bottom;
    private boolean oneWay;

    public Platform(float x, float y, float width, float height, World world, boolean oneWay)
    {
        super(x, y, width, height, world);

        this.left = x - width / 2.0f;
        this.right = x + width / 2.0f;
        this.bottom = y - height / 2.0f;
        this.top = y +  height / 2.0f;

        this.oneWay = oneWay;
    }

    @Override
    protected void initPhysics(World world)
    {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(
                getX() / Constants.PTM,
                getY() / Constants.PTM);

        PolygonShape shape = new PolygonShape();
        shape.setAsBox(
                (getWidth() / 2.0f) / Constants.PTM,
                (getHeight() / 2.0f) / Constants.PTM);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;

        this.body = world.createBody(bodyDef);
        this.body.createFixture(fixtureDef);
        this.body.setUserData(this);

        shape.dispose();
    }

    @Override
    public void renderShapes(ShapeRenderer renderer)
    {
        renderer.setColor(Constants.PLATFORM_COLOR);
        renderer.rect(
                this.left,
                this.bottom,
                getWidth(),
                getHeight()
        );
    }

    @Override
    public void renderSprites(SpriteBatch batch) {}

    public boolean isOneWay() { return this.oneWay; }
}
