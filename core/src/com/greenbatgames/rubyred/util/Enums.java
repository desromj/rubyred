package com.greenbatgames.rubyred.util;

/**
 * Created by Quiv on 09-11-2016.
 */

public class Enums
{
    private Enums() {}

    public enum AnimationState {

        IDLE("idle", true),
        HOPPING("hop", false);

        String label;
        boolean looping;

        private AnimationState(String label, boolean looping) {
            this.label = label;
            this.looping = looping;
        }

        public String getLabel() { return label; }
        public boolean isLooping() {return looping; }
    }
}
