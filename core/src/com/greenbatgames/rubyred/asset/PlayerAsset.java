package com.greenbatgames.rubyred.asset;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.esotericsoftware.spine.AnimationState;
import com.esotericsoftware.spine.AnimationStateData;
import com.esotericsoftware.spine.Skeleton;
import com.esotericsoftware.spine.SkeletonData;
import com.esotericsoftware.spine.SkeletonJson;
import com.esotericsoftware.spine.SkeletonRenderer;
import com.esotericsoftware.spine.SkeletonRendererDebug;

/**
 * Created by Quiv on 02-12-2016.
 */

public class PlayerAsset extends SpineAnimationAsset
{
    Sprite sprite;

    public PlayerAsset() {
        super();

        sprite = new Sprite(new Texture(Gdx.files.internal("sprites/placeholder/robot.png")));
        sprite.setScale(2.0f);
    }

    @Override
    public void initSpine() {
        /*
            TODO: Un-comment when Spine assets are ready
        skeletonRenderer = new SkeletonRenderer();
        skeletonRenderer.setPremultipliedAlpha(true);       // Alpha blending to reduce outlines

        skeletonRendererDebug = new SkeletonRendererDebug();
        skeletonRendererDebug.setBoundingBoxes(false);
        skeletonRendererDebug.setRegionAttachments(false);

        atlas = new TextureAtlas(Gdx.files.internal("sprites/ruby/rubyred.atlas"));
        SkeletonJson json = new SkeletonJson(atlas);        // load stateless skeleton JSON data
        json.setScale(0.045f);                               // set skeleton scale from Spine

        // Read the JSON data and create the skeleton
        SkeletonData skeletonData = json.readSkeletonData(Gdx.files.internal("sprites/ruby/skeleton.json"));
        skeleton = new Skeleton(skeletonData);

        // init animation
        AnimationStateData stateData = new AnimationStateData(skeletonData);
        animationState = new AnimationState(stateData);
        animationState.setAnimation(0, "idle", true);
        */
    }

    public void loadBlends() {
        // TODO: Add more blends here when more animations become available

        // AnimationStateData data = animationState.getData();
    }

    @Override
    public void render(Batch batch) {
        // TODO: Remove method and replace with Spine animation graphics when ready

        sprite.draw(batch);
    }

    // TODO: Remove when Spine assets take over
    public Sprite getSprite() { return sprite; }
}
