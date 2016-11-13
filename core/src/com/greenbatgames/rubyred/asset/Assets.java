package com.greenbatgames.rubyred.asset;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Disposable;
import com.esotericsoftware.spine.AnimationState;
import com.esotericsoftware.spine.AnimationStateData;
import com.esotericsoftware.spine.Skeleton;
import com.esotericsoftware.spine.SkeletonData;
import com.esotericsoftware.spine.SkeletonJson;
import com.esotericsoftware.spine.SkeletonRenderer;
import com.esotericsoftware.spine.SkeletonRendererDebug;
import com.greenbatgames.rubyred.entity.PhysicsBody;
import com.greenbatgames.rubyred.util.Enums;

/**
 * Created by Quiv on 09-11-2016.
 */

public class Assets implements Disposable, AssetErrorListener
{
    private Assets() { }

    private AssetManager assetManager;

    public static final String TAG = Assets.class.getName();
    public static final Assets instance = new Assets();

    public void init()
    {
        assetManager = new AssetManager();
        assetManager.setErrorListener(this);
        assetManager.finishLoading();
    }

    @Override
    public void error(AssetDescriptor asset, Throwable throwable)
    {
        Gdx.app.error(TAG, "Could not load asset: " + asset.fileName, throwable);
    }

    @Override
    public void dispose() {
        assetManager.dispose();
    }



    // If there are any other Spine assets to use, change this to a switch based on PhysicsBody
    public SpineAnimationAsset makeAsset(PhysicsBody pb) {
        return new RubyAssets();
    }

    // Parent class which can be rendered via the Spine runtimes
    public abstract class SpineAnimationAsset
    {
        protected SkeletonRenderer skeletonRenderer;
        protected SkeletonRendererDebug skeletonRendererDebug;
        protected TextureAtlas atlas;

        // Skeleton and animationState are public since they will need to be updated via game logic
        public AnimationState animationState;
        public Skeleton skeleton;

        // All subclasses must initialize all required Spine classes above
        public abstract void initSpine();
        // All subclasses must load animation blending (can be blank)
        public abstract void loadBlends();

        public SpineAnimationAsset()
        {
            initSpine();
            loadBlends();
        }

        public void render(Batch batch)
        {
            animationState.update(Gdx.graphics.getDeltaTime());
            animationState.apply(skeleton);
            skeleton.updateWorldTransform();

            skeletonRenderer.draw(batch, skeleton);
            // skeletonRendererDebug.draw(skeleton);
        }

        public void setAnimation(Enums.AnimationState state) {
            state.loadAnimations(animationState);
        }
    }



    public class RubyAssets extends SpineAnimationAsset
    {
        @Override
        public void initSpine() {
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
        }

        public void loadBlends() {
            AnimationStateData data = animationState.getData();

            data.setMix(Enums.AnimationState.HOP.getLabel(), Enums.AnimationState.FALL.getLabel(), 0.25f);
            data.setMix(Enums.AnimationState.HOP.getLabel(), Enums.AnimationState.HOP.getLabel(), 0.25f);
            data.setMix(Enums.AnimationState.HOP.getLabel(), Enums.AnimationState.LAND.getLabel(), 0.05f);
            data.setMix(Enums.AnimationState.HOP.getLabel(), Enums.AnimationState.CLIMB.getLabel(), 0.1f);
            data.setMix(Enums.AnimationState.HOP.getLabel(), Enums.AnimationState.RECOIL.getLabel(), 0.1f);

            data.setMix(Enums.AnimationState.FALL.getLabel(), Enums.AnimationState.LAND.getLabel(), 0.1f);
            data.setMix(Enums.AnimationState.FALL.getLabel(), Enums.AnimationState.CLIMB.getLabel(), 0.1f);
            data.setMix(Enums.AnimationState.FALL.getLabel(), Enums.AnimationState.RECOIL.getLabel(), 0.1f);

            data.setMix(Enums.AnimationState.IDLE.getLabel(), Enums.AnimationState.HOP.getLabel(), 0.1f);
            data.setMix(Enums.AnimationState.IDLE.getLabel(), Enums.AnimationState.FALL.getLabel(), 0.25f);
            data.setMix(Enums.AnimationState.IDLE.getLabel(), Enums.AnimationState.RECOIL.getLabel(), 0.1f);
            data.setMix(Enums.AnimationState.IDLE.getLabel(), Enums.AnimationState.LONG_JUMP_PREPARE.getLabel(), 0.1f);

            data.setMix(Enums.AnimationState.LAND.getLabel(), Enums.AnimationState.IDLE.getLabel(), 0.1f);
            data.setMix(Enums.AnimationState.LAND.getLabel(), Enums.AnimationState.HOP.getLabel(), 0.05f);
            data.setMix(Enums.AnimationState.LAND.getLabel(), Enums.AnimationState.RECOIL.getLabel(), 0.1f);

            data.setMix(Enums.AnimationState.LONG_JUMP_PREPARE.getLabel(), Enums.AnimationState.LONG_JUMP.getLabel(), 0.1f);
            data.setMix(Enums.AnimationState.LONG_JUMP_PREPARE.getLabel(), Enums.AnimationState.IDLE.getLabel(), 0.25f);
            data.setMix(Enums.AnimationState.LONG_JUMP_PREPARE.getLabel(), Enums.AnimationState.LAND.getLabel(), 0.25f);
            data.setMix(Enums.AnimationState.LONG_JUMP_PREPARE.getLabel(), Enums.AnimationState.RECOIL.getLabel(), 0.1f);

            data.setMix(Enums.AnimationState.LONG_JUMP_WAIT.getLabel(), Enums.AnimationState.IDLE.getLabel(), 0.25f);
            data.setMix(Enums.AnimationState.LONG_JUMP_WAIT.getLabel(), Enums.AnimationState.LAND.getLabel(), 0.25f);
            data.setMix(Enums.AnimationState.LONG_JUMP_WAIT.getLabel(), Enums.AnimationState.RECOIL.getLabel(), 0.1f);

            data.setMix(Enums.AnimationState.LONG_JUMP.getLabel(), Enums.AnimationState.FALL.getLabel(), 0.25f);
            data.setMix(Enums.AnimationState.LONG_JUMP.getLabel(), Enums.AnimationState.LAND.getLabel(), 0.1f);
            data.setMix(Enums.AnimationState.LONG_JUMP.getLabel(), Enums.AnimationState.CLIMB.getLabel(), 0.1f);
            data.setMix(Enums.AnimationState.LONG_JUMP.getLabel(), Enums.AnimationState.RECOIL.getLabel(), 0.1f);

            data.setMix(Enums.AnimationState.SPRING_JUMP_PREPARE.getLabel(), Enums.AnimationState.SPRING_JUMP.getLabel(), 0.1f);
            data.setMix(Enums.AnimationState.SPRING_JUMP_PREPARE.getLabel(), Enums.AnimationState.IDLE.getLabel(), 0.25f);
            data.setMix(Enums.AnimationState.SPRING_JUMP_PREPARE.getLabel(), Enums.AnimationState.LAND.getLabel(), 0.25f);
            data.setMix(Enums.AnimationState.SPRING_JUMP_PREPARE.getLabel(), Enums.AnimationState.RECOIL.getLabel(), 0.1f);

            data.setMix(Enums.AnimationState.SPRING_JUMP_WAIT.getLabel(), Enums.AnimationState.IDLE.getLabel(), 0.25f);
            data.setMix(Enums.AnimationState.SPRING_JUMP_WAIT.getLabel(), Enums.AnimationState.LAND.getLabel(), 0.25f);
            data.setMix(Enums.AnimationState.SPRING_JUMP_WAIT.getLabel(), Enums.AnimationState.RECOIL.getLabel(), 0.1f);

            data.setMix(Enums.AnimationState.SPRING_JUMP.getLabel(), Enums.AnimationState.FALL.getLabel(), 0.25f);
            data.setMix(Enums.AnimationState.SPRING_JUMP.getLabel(), Enums.AnimationState.LAND.getLabel(), 0.1f);
            data.setMix(Enums.AnimationState.SPRING_JUMP.getLabel(), Enums.AnimationState.CLIMB.getLabel(), 0.1f);
            data.setMix(Enums.AnimationState.SPRING_JUMP.getLabel(), Enums.AnimationState.RECOIL.getLabel(), 0.1f);

            data.setMix(Enums.AnimationState.CLIMB.getLabel(), Enums.AnimationState.IDLE.getLabel(), 0.25f);
            data.setMix(Enums.AnimationState.CLIMB.getLabel(), Enums.AnimationState.LAND.getLabel(), 0.25f);

            data.setMix(Enums.AnimationState.RECOIL.getLabel(), Enums.AnimationState.LAND.getLabel(), 0.25f);
            data.setMix(Enums.AnimationState.RECOIL.getLabel(), Enums.AnimationState.CLIMB.getLabel(), 0.25f);
        }
    }

}
