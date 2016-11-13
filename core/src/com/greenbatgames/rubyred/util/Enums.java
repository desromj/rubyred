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
        LAND("land", false),
        CLIMB("climb", false),
        RECOIL("recoil", true),

        LONG_JUMP("long-jump", false),
        LONG_JUMP_PREPARE("long-jump-prepare", false),
        LONG_JUMP_WAIT("long-jump-wait", true),

        SPRING_JUMP("spring-jump", false),
        SPRING_JUMP_PREPARE("spring-jump-prepare", false),
        SPRING_JUMP_WAIT("spring-jump-wait", true);

        String label;
        boolean looping;

        private AnimationState(String label, boolean looping) {
            this.label = label;
            this.looping = looping;
        }

        public String getLabel() { return label; }
        public boolean isLooping() {return looping; }



        // Set animation hierarchies for non-looping animations
        public void loadAnimations(com.esotericsoftware.spine.AnimationState animationState) {

            // All animations load their base animations by default
            animationState.setAnimation(0, label, looping);

            /*
                Specialized controls per animation afterwards
                NOTE: EVERY non-looping animation should conclude with a looping animation
            */
            switch (this)
            {
                case HOP:
                    animationState.addAnimation(0, FALL.getLabel(), FALL.isLooping(), 0f);
                    break;

                case LAND:
                    animationState.addAnimation(0, IDLE.getLabel(), IDLE.isLooping(), 0f);
                    break;

                case CLIMB:
                    animationState.addAnimation(0, IDLE.getLabel(), IDLE.isLooping(), 0f);
                    break;

                case LONG_JUMP_PREPARE:
                    animationState.addAnimation(0, LONG_JUMP_WAIT.label, LONG_JUMP_WAIT.isLooping(), 0f);
                    break;

                case LONG_JUMP:
                    animationState.addAnimation(0, FALL.getLabel(), FALL.isLooping(), 0f);
                    break;

                case SPRING_JUMP_PREPARE:
                    animationState.addAnimation(0, SPRING_JUMP_WAIT.label, SPRING_JUMP_WAIT.isLooping(), 0f);
                    break;

                case SPRING_JUMP:
                    animationState.addAnimation(0, FALL.getLabel(), FALL.isLooping(), 0f);
                    break;
            }
        }
    }
}
