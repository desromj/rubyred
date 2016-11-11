package com.greenbatgames.rubyred.util;

import com.esotericsoftware.spine.AnimationState;

/**
 * Created by Quiv on 09-11-2016.
 */

public class Enums
{
    private Enums() {}

    public enum AnimationState {

        IDLE("idle", true),
        HOP("hop", false),
        FALL("fall", true),
        LAND("land", false);

        String label;
        boolean looping;

        private AnimationState(String label, boolean looping) {
            this.label = label;
            this.looping = looping;
        }

        public String getLabel() { return label; }
        public boolean isLooping() {return looping; }



        // Set animation heirarchies for non-looping animations
        public void loadAnimations(com.esotericsoftware.spine.AnimationState animationState) {
            switch (this)
            {
                case IDLE:
                    animationState.setAnimation(0, label, looping);
                    break;
                case HOP:
                    animationState.setAnimation(0, label, looping);
                    animationState.addAnimation(0, FALL.getLabel(), FALL.isLooping(), 0f);
                    break;
                case FALL:
                    animationState.setAnimation(0, label, looping);
                    break;
                case LAND:
                    animationState.setAnimation(0, label, looping);
                    animationState.addAnimation(0, IDLE.getLabel(), IDLE.isLooping(), 0f);
                    break;
            }
        }
    }
}
