package com.greenbatgames.rubyred.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.ChainShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.greenbatgames.rubyred.util.Constants;

/**
 * Created by Quiv on 02-12-2016.
 */

public class Terrain extends PhysicsBody
{
    public static final String TAG = Terrain.class.getSimpleName();

    public Terrain(float x, float y, float width, float height, World world, float [] verticies) {
        super(x, y, width, height, world);
        makeShape(verticies, world);
    }

    @Override
    protected void initPhysics(World world) { }



    // Physics initialized here, as in the overridden initPhysics is not currently viable
    private void makeShape(float [] verticies, World world) {

        Gdx.app.log(TAG, "Spawned PolyLine Terrain!");

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyDef.BodyType.StaticBody;
        bodyDef.position.set(
                getX() / Constants.PTM,
                (getY() - getHeight()) / Constants.PTM);

        ChainShape shape = new ChainShape();

        // Edit the vertices to be considered in the Box2D engine
        for (int i = 0; i < verticies.length; i++)
            verticies[i] /= Constants.PTM;

        shape.createChain(verticies);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = shape;

        this.body = world.createBody(bodyDef);
        this.body.createFixture(fixtureDef);
        this.body.setUserData(this);

        Gdx.app.log(TAG, "Terrain pos: (" + getX() + ", " + getY() + ")");
        Gdx.app.log(TAG, "Terrain w/h: (" + getWidth() + ", " + getHeight() + ")");
        Gdx.app.log(TAG, "Terrain body pos: (" + body.getPosition().x + ", " + body.getPosition().y + ")");

        shape.dispose();
    }
}
