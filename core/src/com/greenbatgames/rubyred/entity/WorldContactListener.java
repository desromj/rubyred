package com.greenbatgames.rubyred.entity;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.ContactImpulse;
import com.badlogic.gdx.physics.box2d.ContactListener;
import com.badlogic.gdx.physics.box2d.Manifold;
import com.greenbatgames.rubyred.util.Constants;
import com.greenbatgames.rubyred.util.Utils;

/**
 * Created by Quiv on 12-08-2016.
 */
public class WorldContactListener implements ContactListener
{
    @Override
    public void beginContact(Contact contact)
    {
        Object
                a = Utils.getUserData(contact, true),
                b = Utils.getUserData(contact, false);

        if (a instanceof Player || b instanceof Player)
        {
            Player player;
            Object other;

            if (a instanceof Player)
            {
                player = (Player) a;
                other = b;
            }
            else
            {
                player = (Player) b;
                other = a;
            }

            // Collision logic for Bug landing on other physics objects
            if (other instanceof PhysicsBody)
            {
                PhysicsBody physical = (PhysicsBody) other;

                /*
                    A collision has already happened, we just need to check:
                        - Player's position is above the other object's
                        - Player's x position is within the other's x + width
                  */
                Vector2 playerPos = player.getBody().getPosition();
                Vector2 otherPos = physical.getBody().getPosition();

                boolean landed = false;

                if (playerPos.y > otherPos.y)
                {
                    if ((playerPos.x * Constants.PTM + Constants.PLATFORM_EDGE_LEEWAY > otherPos.x * Constants.PTM - physical.getWidth() / 2.0f)
                            && (playerPos.x * Constants.PTM - Constants.PLATFORM_EDGE_LEEWAY < otherPos.x * Constants.PTM + physical.getWidth() / 2.0f))
                    {
                        landed = true;
                    }
                }

                if (landed)
                    player.land();
            }
        }
    }

    @Override
    public void endContact(Contact contact) {

    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

        Object
                a = Utils.getUserData(contact, true),
                b = Utils.getUserData(contact, false);

        // Player collision
        if (a instanceof Player || b instanceof Player)
        {
            Player player;
            Object other;

            if (a instanceof Player)
            {
                player = (Player) a;
                other = b;
            }
            else
            {
                player = (Player) b;
                other = a;
            }

            // Collision logic for one-way platforms
            if (other instanceof Platform) {

                Platform platform = (Platform) other;

                if (player.isCollisionDisabled() && platform.isOneWay()) {
                    contact.setEnabled(false);
                } else if (platform.isOneWay()) {
                    if (player.getBottom() <= platform.getTop() - Constants.PLATFORM_COLLISION_LEEWAY) {
                        contact.setEnabled(false);
                    }
                }
            }
        }
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
