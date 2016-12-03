package com.greenbatgames.rubyred.util;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Contact;
import com.badlogic.gdx.physics.box2d.Fixture;

/**
 * Created by Quiv on 10-08-2016.
 */
public class Utils
{
    private Utils() {}


    /**
     * Reads the user data from the contact and returns it as an Object
     * This is the user data stored in the Body object, as one Body
     * may contain multiple fixtures each with their own user data
     *
     * @param contact
     * @param first
     */
    public static Object getUserData(Contact contact, boolean first)
    {
        if (first)
            return contact.getFixtureA().getBody().getUserData();
        return contact.getFixtureB().getBody().getUserData();
    }



    /**
     * Creates a ParticleEffect object given the path, imagePath, and scale.
     *
     * @param filePath Gdx.files.internal path to the file defining the particle effect
     * @param imagesPath Gdx.files.internal path to the images the filePath file uses
     * @param scale Scale to apply to the ParticleEffect
     * @return The created and started ParticleEffect
     */
    public static ParticleEffect makeParticleEffect(
            String filePath,
            String imagesPath,
            float scale
    )
    {
        ParticleEffect effect = new ParticleEffect();
        effect.load(Gdx.files.internal(filePath), Gdx.files.internal(imagesPath));
        effect.scaleEffect(scale);

        return effect;
    }



    /**
     * @param filename The filename of the internal-path sound effect to play, including extension
     * @param volume Ratio of volume to play the effect at
     * @return The Sound object created and currently being played, in case it needs to be edited further
     */
    public static Sound playSound(String filename, float volume)
    {
        Sound sound = Gdx.audio.newSound(Gdx.files.internal(filename));
        sound.play(volume * Constants.VOLUME_EFFECTS);
        return sound;
    }



    /**
     * @param filename The filename of the internal-path sound effect to play, including extension
     * @param volume Ratio of volume to play the effect at
     * @return The Sound object created and currently being played, in case it needs to be edited further
     */
    public static Sound playMusic(String filename, float volume)
    {
        // TODO: Change this to actually play music instead of sound effects
        Sound sound = Gdx.audio.newSound(Gdx.files.internal(filename));
        sound.play(volume * Constants.VOLUME_MUSIC);
        return sound;
    }



    public static float getMassRatio(Body first, Body second, boolean getFirst)
    {
        float firstRatio = first.getMass() / (first.getMass() + second.getMass());
        float secondRatio = second.getMass() / (first.getMass() + second.getMass());

        if (getFirst)
            return firstRatio;
        return secondRatio;
    }
}
