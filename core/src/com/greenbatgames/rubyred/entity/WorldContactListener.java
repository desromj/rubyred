package com.greenbatgames.rubyred.entity;

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
    public void beginContact(Contact contact) {
        
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
        if (a instanceof com.greenbatgames.rubyred.player.Player || b instanceof com.greenbatgames.rubyred.player.Player)
        {
            com.greenbatgames.rubyred.player.Player player;
            Object other;

            if (a instanceof com.greenbatgames.rubyred.player.Player)
            {
                player = (com.greenbatgames.rubyred.player.Player) a;
                other = b;
            }
            else
            {
                player = (com.greenbatgames.rubyred.player.Player) b;
                other = a;
            }

            // Collision logic for one-way platforms
            if (other instanceof Platform) {

                Platform platform = (Platform) other;

                if (player.isCollisionDisabled() && platform.isOneWay()) {
                    contact.setEnabled(false);
                } else if (platform.isOneWay()) {
                   // TODO: handle this when better prepared
                }
            }

            // Collision disabling for other objects

            if (other instanceof com.greenbatgames.rubyred.iface.Activateable) {
                if (((com.greenbatgames.rubyred.iface.Activateable) other).isBroken())
                    contact.setEnabled(false);
            }
        }
    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
