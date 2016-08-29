package com.greenbatgames.rubyred.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.ScreenAdapter;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.MapLayer;
import com.badlogic.gdx.maps.MapObject;
import com.badlogic.gdx.maps.MapProperties;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapRenderer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.greenbatgames.rubyred.entity.PhysicsBody;
import com.greenbatgames.rubyred.entity.Platform;
import com.greenbatgames.rubyred.entity.Player;
import com.greenbatgames.rubyred.entity.WorldContactListener;
import com.greenbatgames.rubyred.util.ChaseCam;
import com.greenbatgames.rubyred.util.Constants;

/**
 * Created by Quiv on 10-08-2016.
 */
public class GameScreen  extends ScreenAdapter implements InputProcessor
{
    public static final GameScreen instance = new GameScreen();
    public static final String TAG = GameScreen.class.getSimpleName();

    private GameScreen() { init(); }

    World world;
    Player player;

    Viewport viewport;
    ShapeRenderer renderer;
    SpriteBatch batch;
    ChaseCam chaseCam;

    TiledMap tiledMap;
    TiledMapRenderer tiledMapRenderer;

    Array<Body> bodiesToRemove;
    Array<BodyDef> bodiesToAdd;
    Array<FixtureDef> fixturesToAdd;
    Array<PhysicsBody> userDataToAdd;
    Box2DDebugRenderer debugRenderer;

    Array<Platform> platforms;

    Matrix4 debugMatrix;


    public void init()
    {
        // Level member variables
        world = new World(new Vector2(0, Constants.GRAVITY), true);
        player = new Player(80.0f, 240.0f, Constants.RUBY_RADIUS * 2.0f, Constants.RUBY_RADIUS * 4.0f, world);

        // local Orthographic Camera for the viewport
        OrthographicCamera camera = new OrthographicCamera();
        camera.setToOrtho(false, Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT);
        viewport = new ExtendViewport(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT, camera);

        // Proceed with other instance variables
        renderer = new ShapeRenderer();
        batch = new SpriteBatch();
        chaseCam = new ChaseCam(camera, player);

        tiledMap = new TmxMapLoader().load("level-1.tmx");
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap, Constants.TILED_UNIT_SCALE);

        // Set player position to the spawn position object in the TiledMap
        for (MapLayer layer: tiledMap.getLayers()) {
            for (MapObject object : layer.getObjects()) {
                if (object.getName().compareTo("spawn-position") == 0) {
                    player.setSpawnPosition(
                            object.getProperties().get("x", Float.class),
                            object.getProperties().get("y", Float.class)
                    );
                    player.init();
                }
            }
        }

        bodiesToAdd = new Array<BodyDef>();
        fixturesToAdd = new Array<FixtureDef>();
        userDataToAdd = new Array<PhysicsBody>();
        bodiesToRemove = new Array<Body>();
        debugRenderer = new Box2DDebugRenderer();

        platforms = new Array<Platform>();

        // Other game objects
        platforms.add(new Platform(0.5f, 0.5f, 40.0f, 2.0f, world, false));
        platforms.add(new Platform(1.5f, 6.0f, 1.5f, 1.5f, world, true));

        // Finalize
        Gdx.input.setInputProcessor(this);
        world.setContactListener(new WorldContactListener());
    }



    /*
        Main render/update loops
     */

    @Override
    public void render(float delta)
    {
        // Update the physics engine with all the bodies
        world.step(
                Constants.PHYSICS_STEP_FREQ,
                Constants.PHYSICS_VEL_ITERATIONS,
                Constants.PHYSICS_POS_ITERATIONS
        );

        // Edit Physics bodies outside of world.step
        runQueuedPhysicsChanges();

        /*
            Game object updates
         */

        player.update(delta);
        chaseCam.update(delta);

        /*
            Rendering logic
         */

        // Set projection matricies
        viewport.apply();
        renderer.setProjectionMatrix(viewport.getCamera().combined);
        batch.setProjectionMatrix(viewport.getCamera().combined);
        tiledMapRenderer.setView(chaseCam.getCamera());

        // Scale the debug Matrix to box2d sizes
        debugMatrix = viewport.getCamera().combined.cpy().scale(
                Constants.PTM,
                Constants.PTM,
                0
        );

        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Render Tiled Maps
        tiledMapRenderer.render();

        // Render Shapes
        renderer.begin(ShapeRenderer.ShapeType.Filled);
        doShapeRender(renderer);
        renderer.end();

        // Render Sprites
        batch.begin();
        doSpriteRender(batch);
        batch.end();

        // Render the debug physics engine settings
        debugRenderer.render(world, debugMatrix);
    }



    /*
        Separated shape and sprite renderers
     */


    public void doShapeRender(ShapeRenderer renderer)
    {
        // Render game background
        /*
        renderer.setColor(Constants.BG_COLOR);
        renderer.rect(0, 0, Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT);
        */

        // Render platforms
        for (Platform platform: platforms)
            platform.renderShapes(renderer);

        // TODO: temp player render, until sprites are done
        player.renderShapes(renderer);
    }



    public void doSpriteRender(SpriteBatch batch)
    {
        // Render player character
        player.renderSprites(batch);
    }



    /**
     * Adds and removes physics bodies outside of world.step()
     */
    private void runQueuedPhysicsChanges()
    {
        // Add
        for (int i = 0; i < this.bodiesToAdd.size; i++)
        {
            Body body = world.createBody(this.bodiesToAdd.get(i));
            body.createFixture(fixturesToAdd.get(i));
            body.setUserData(userDataToAdd.get(i));

            userDataToAdd.get(i).setBody(body);
        }

        if (this.bodiesToAdd.size > 0) {
            this.bodiesToAdd.clear();
            this.fixturesToAdd.clear();
            this.userDataToAdd.clear();
        }

        // Remove
        for (int i = 0; i < this.bodiesToRemove.size; i++)
        {
            world.destroyBody(this.bodiesToRemove.get(i));
        }
        this.bodiesToRemove.clear();
    }



    /*
        Physics body queueing methods
     */

    public void queueBodyToDestroy(Body body)
    {
        this.bodiesToRemove.add(body);
    }

    public void queueBodyToCreate(BodyDef bodyDef, FixtureDef fixtureDef, PhysicsBody newUserData)
    {
        this.bodiesToAdd.add(bodyDef);
        this.fixturesToAdd.add(fixtureDef);
        this.userDataToAdd.add(newUserData);
    }



    /*
        Getters and Setters
     */

    public Player getPlayer() { return player; }
    public Viewport getViewport()
    {
        return viewport;
    }



    /*
        Other ScreenAdapter Overrides
     */

    @Override
    public void resize(int width, int height)
    {
        viewport.update(width, height, true);
    }



    /*
        InputProcessor Overrides
     */

    @Override
    public boolean keyDown(int keycode)
    {
        return false;
    }

    @Override
    public boolean keyUp(int keycode) {
        return false;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        return false;
    }

    @Override
    public boolean scrolled(int amount) {
        return false;
    }
}
