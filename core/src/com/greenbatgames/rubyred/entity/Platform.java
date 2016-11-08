package com.greenbatgames.rubyred.entity;

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
    private boolean oneWay;

    public Platform(float x, float y, float width, float height, World world, boolean oneWay)
    {
        super(x, y, width, height, world);
        this.oneWay = oneWay;
    }

    @Override
    protected void initPhysics(World world)
    {
        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(
                (getX() + getWidth() / 2.0f) / Constants.PTM,
                (getY() + getHeight() / 2.0f) / Constants.PTM);

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

    public boolean isOneWay() { return this.oneWay; }

    // All platforms allow climbing on them by default. Override this method in child classes
    // to deny the climbing ability
    public boolean allowClimbing() { return true; }
}
