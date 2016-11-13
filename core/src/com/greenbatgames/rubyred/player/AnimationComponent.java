package com.greenbatgames.rubyred.player;

import com.esotericsoftware.spine.AnimationState;
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
        return update(delta, 1f);
    }

    /**
     * @param delta
     * @param percentLeft If the animation changes, this value determines what percentage
     *                    of the new animation should be played. Passing a value of 0.35 (35%)
     *                    means that only the last 35% of the new animation will be played.
     *                    Best used for non-looping animations
     * @return
     */
    public boolean update(float delta, float percentLeft) {

        // Set the player position and X orientation
        this.asset.skeleton.setPosition(
                player.getX() + player.getWidth() / 2.0f,
                player.getY());
        this.asset.skeleton.setFlipX(!player.facingRight);

        // Update our animation if it has changed
        if (this.animationState != this.previousState) {
            asset.skeleton.setToSetupPose();
            asset.setAnimation(this.animationState);

            // Set the percentage remaining if it is not 100%
            if (percentLeft < 1f) {
                AnimationState.TrackEntry te = asset.animationState.getCurrent(0);
                te.setTime(te.getAnimation().getDuration() - te.getAnimation().getDuration() * percentLeft);
            }
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

    public void setNext(Enums.AnimationState state, float percentLeft) {
        this.animationState = state;

    }

    public String getNextLabel() { return animationState.getLabel(); }
}
