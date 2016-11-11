package com.greenbatgames.rubyred.player;

import com.greenbatgames.rubyred.asset.Assets;
import com.greenbatgames.rubyred.util.Enums;

/**
 * Created by Quiv on 09-11-2016.
 */

public class AnimationComponent extends PlayerComponent
{
    Assets.SpineAnimationAsset asset;
    private Enums.AnimationState animationState, previousState;

    public AnimationComponent(Player player) {
        super(player);
    }



    @Override
    public boolean update(float delta) {

        // Set the player position and X orientation
        this.asset.skeleton.setPosition(
                player.getX() + player.getWidth() / 2.0f,
                player.getY());
        this.asset.skeleton.setFlipX(!player.facingRight);

        // Update our animation if it has changed
        if (this.animationState != this.previousState) {
            asset.skeleton.setToSetupPose();
            asset.setAnimation(this.animationState);
        }

        this.previousState = this.animationState;

        // Changing animation should not break updates - return true
        return true;
    }



    @Override
    public void init() {
        asset = Assets.instance.makeAsset(player);
        animationState = Enums.AnimationState.IDLE;
        previousState = Enums.AnimationState.IDLE;
    }



    public void setNext(Enums.AnimationState state) {
        this.animationState = state;
    }
    public String getNextLabel() { return animationState.getLabel(); }
}
