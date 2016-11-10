package com.greenbatgames.rubyred.player;

import com.badlogic.gdx.Gdx;
import com.esotericsoftware.spine.Animation;
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

        for (AnimationState.TrackEntry e: asset.animationState.getTracks())
            Gdx.app.log("AnimComp", "Track: " + e.getAnimation().getName());

        Gdx.app.log("AnimComp", "========= done =========");

        float playbackSpeed = getAnimationPlaybackSpeed(asset.animationState.getCurrent(0).getAnimation());
        asset.animationState.getCurrent(0).setTimeScale(playbackSpeed);

        // Set the player position and X orientation
        this.asset.skeleton.setPosition(
                player.getX() + player.getWidth() / 2.0f,
                player.getY());
        this.asset.skeleton.setFlipX(!player.facingRight);

        // Determine next animation state and save previous animation state
        this.animationState = nextAnimationState();

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



    public Enums.AnimationState nextAnimationState() {

        if (player.jumper().isInAir()) {
            return animationState;
        } else {
            return Enums.AnimationState.IDLE;
        }
    }



    // Animations which have to be sped up
    private float getAnimationPlaybackSpeed(Animation anim)
    {
        // Hopping at 4x speed
        if (anim.getName().startsWith("hop"))
            return 4.0f;

        return 1.0f;
    }



    public void setNext(Enums.AnimationState state) {
        this.animationState = state;
    }
}
