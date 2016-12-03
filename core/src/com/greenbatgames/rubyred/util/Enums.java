package com.greenbatgames.rubyred.util;

import com.esotericsoftware.spine.AnimationState;

/**
 * Created by Quiv on 09-11-2016.
 */

public class Enums
{
    private Enums() {}

    public enum AnimationState {
        IDLE("idle", true);

        private String label;
        private boolean looping;

        private AnimationState(String label, boolean looping) {
            this.label = label;
            this.looping = looping;
        }

        public String getLabel() { return label; }
        public boolean isLooping() { return looping; }



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

            }
        }
    }
}
