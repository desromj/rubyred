package com.greenbatgames.rubyred.player;

import com.greenbatgames.rubyred.entity.Initializeable;

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

    public abstract void update(float delta);
}
