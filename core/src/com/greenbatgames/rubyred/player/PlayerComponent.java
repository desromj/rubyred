package com.greenbatgames.rubyred.player;

import com.greenbatgames.rubyred.iface.Initializeable;

/**
 * Created by Quiv on 02-11-2016.
 */

public abstract class PlayerComponent implements Initializeable
{
    protected Player player;

    public PlayerComponent(Player player) {
        this.player = player;
        init();
    }

    /**
     * Standard update method, altered to return a status flag
     *
     * @param delta delta time since last update
     * @return true if the update loop should keep going in the sequence of Component updates
     */
    public abstract boolean update(float delta);
}
