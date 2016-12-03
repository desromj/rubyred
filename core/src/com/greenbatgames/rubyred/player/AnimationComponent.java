package com.greenbatgames.rubyred.player;

import com.esotericsoftware.spine.AnimationState;
import com.greenbatgames.rubyred.asset.Assets;
import com.greenbatgames.rubyred.asset.SpineAnimationAsset;
import com.greenbatgames.rubyred.util.Enums;

import static com.greenbatgames.rubyred.util.Enums.AnimationState.*;

/**
 * Created by Quiv on 09-11-2016.
 */

public class AnimationComponent extends PlayerComponent
{
    SpineAnimationAsset asset;
    private Enums.AnimationState animationState, previousState;

    private float nextPercentRatioComplete;

    public AnimationComponent(Player player) {
        super(player);
        nextPercentRatioComplete = 1.0f;
    }



    @Override
    public boolean update(float delta) {

        // Set the player position and X orientation
        this.asset.skeleton.setPosition(
                player.getX() + player.getWidth() / 2.0f,
                player.getY());
        this.asset.skeleton.setFlipX(!player.mover().isFacingRight());

        // Update our animation if it has changed
        if (this.animationState != this.previousState) {
            asset.skeleton.setToSetupPose();
            asset.setAnimation(this.animationState);

            // Set the percentage remaining if it is not 100%
            if (this.nextPercentRatioComplete < 1f) {
                AnimationState.TrackEntry te = asset.animationState.getCurrent(0);
                te.setTime(te.getAnimation().getDuration() - te.getAnimation().getDuration() * nextPercentRatioComplete);
            }
        }

        this.previousState = this.animationState;

        // Changing animation should not break updates - return true
        return true;
    }



    @Override
    public void init() {
        asset = Assets.instance.makeAsset(player);
        animationState = IDLE;
        previousState = IDLE;
    }



    public void setNext(Enums.AnimationState state) {
        setNext(state, 1f);
    }

    /**
     * @param state
     * @param percentLeft If the animation changes, this value determines what percentage
     *                    of the new animation should be played. Passing a value of 0.35 (35%)
     *                    means that only the last 35% of the new animation will be played.
     *                    Best used for non-looping animations
     */
    public void setNext(Enums.AnimationState state, float percentLeft) {
        this.animationState = state;
        nextPercentRatioComplete = percentLeft;
    }

    public Enums.AnimationState getNext() { return animationState; }
    public String getNextLabel() { return animationState.getLabel(); }
}
