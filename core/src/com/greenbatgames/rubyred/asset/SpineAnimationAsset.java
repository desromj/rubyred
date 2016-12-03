package com.greenbatgames.rubyred.asset;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.esotericsoftware.spine.AnimationState;
import com.esotericsoftware.spine.Skeleton;
import com.esotericsoftware.spine.SkeletonRenderer;
import com.esotericsoftware.spine.SkeletonRendererDebug;
import com.greenbatgames.rubyred.util.Enums;

/**
 * Created by Quiv on 02-12-2016.
 */

// Parent class which can be rendered via the Spine runtimes
public abstract class SpineAnimationAsset {

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
