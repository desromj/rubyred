package com.greenbatgames.rubyred.level;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
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
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.greenbatgames.rubyred.RubyGame;
import com.greenbatgames.rubyred.entity.Checkpoint;
import com.greenbatgames.rubyred.iface.Initializeable;
import com.greenbatgames.rubyred.entity.PhysicsBody;
import com.greenbatgames.rubyred.entity.Tooltip;
import com.greenbatgames.rubyred.player.Player;
import com.greenbatgames.rubyred.entity.WorldContactListener;
import com.greenbatgames.rubyred.screen.StartScreen;
import com.greenbatgames.rubyred.util.ChaseCam;
import com.greenbatgames.rubyred.util.Constants;
import com.greenbatgames.rubyred.util.PlayerHUD;

/**
 * Created by Quiv on 27-10-2016.
 */

public class Level implements Initializeable
{
    public static final String TAG = Level.class.getSimpleName();
    String resource;

    World world;
    Player player;
    Vector2 spawnPosition;
    PlayerHUD hud;

    Array<Checkpoint> checkpoints;
    Checkpoint currentCheckpoint;

    Stage stage;

    ChaseCam chaseCam;

    TiledMap tiledMap;
    TiledMapRenderer tiledMapRenderer;

    Array<Body> bodiesToRemove;
    Array<BodyDef> bodiesToAdd;
    Array<FixtureDef> fixturesToAdd;
    Array<PhysicsBody> userDataToAdd;
    Box2DDebugRenderer debugRenderer;

    Matrix4 debugMatrix;



    public Level(String resource) {
        this.resource = resource;

        // Ensure certain objects are instantiated - usually overwritten in init() as well
        spawnPosition = new Vector2();

        checkpoints = new Array<Checkpoint>();
        bodiesToAdd = new Array<BodyDef>();
        fixturesToAdd = new Array<FixtureDef>();
        userDataToAdd = new Array<PhysicsBody>();
        bodiesToRemove = new Array<Body>();
        debugRenderer = new Box2DDebugRenderer();

        init();
    }



    @Override
    public void init()
    {
        // First initialize the physics world
        world = new World(new Vector2(0, Constants.GRAVITY), true);

        // Set the Scene2D stage
        OrthographicCamera camera = new OrthographicCamera();
        camera.setToOrtho(false, Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT);
        stage = new Stage(new ExtendViewport(Constants.WORLD_WIDTH, Constants.WORLD_HEIGHT, camera));

        // Clear previous queued changes
        currentCheckpoint = null;
        spawnPosition.set(0f, 0f);
        checkpoints.clear();
        bodiesToAdd.clear();
        fixturesToAdd.clear();
        userDataToAdd.clear();
        bodiesToRemove.clear();

        // Load data from the tile map and populate the physics world and Scene2D stage
        tiledMap = new TmxMapLoader().load(resource);
        tiledMapRenderer = new OrthogonalTiledMapRenderer(tiledMap);

        // Initialize the Player, using the loaded Spawn Position
        player = new Player(
                spawnPosition.x,
                spawnPosition.y,
                Constants.PLAYER_RADIUS * 2.0f,
                Constants.PLAYER_RADIUS * 4.0f,
                world);

        // Camera and HUD to track the Player
        chaseCam = new ChaseCam(camera, player);
        hud = new PlayerHUD(stage.getViewport());

        // Set the contact listener
        world.setContactListener(new WorldContactListener());

        // Finally, add actors to stage so they can be updated
        addActors();
    }



    /**
     * Add all actors to the stage for this level
     */
    private void addActors()
    {
        stage.addActor(player);
        stage.addActor(chaseCam);
        stage.addActor(hud);
    }



    /*
        Main render/update loops
     */

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

        // Update and re-initialize if required
        stage.act(delta);

        if (player.getY() <= Constants.KILL_PLANE_Y) {
            reinitializeAllActors();

            if (currentCheckpoint != null)
                player.setPosition(currentCheckpoint.x(), currentCheckpoint.y());
        }

        if (Gdx.input.isKeyPressed(Constants.KEY_RESTART)) {
            RubyGame.setScreen(StartScreen.class);
        }

        /*
            Rendering logic
         */

        // Set projection matricies
        stage.getViewport().apply();
        tiledMapRenderer.setView(chaseCam.getCamera());

        // Scale the debug Matrix to box2d sizes
        debugMatrix = chaseCam.getCamera().combined.cpy().scale(
                Constants.PTM,
                Constants.PTM,
                0
        );

        Gdx.gl.glClearColor(1, 1, 1, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Render Tiled Maps
        tiledMapRenderer.render();

        // Render Stage and actors
        stage.draw();

        // Render the debug physics engine settings
        debugRenderer.render(world, debugMatrix);
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
            world.destroyBody(this.bodiesToRemove.get(i));

        this.bodiesToRemove.clear();
    }



    /*
        Physics body queueing methods
     */

    public void queueBodyToDestroy(PhysicsBody body)
    {
        this.bodiesToRemove.add(body.getBody());

        for (Actor actor: stage.getActors())
            if (actor == body)
                actor.remove();
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
        return stage.getViewport();
    }
    public ChaseCam getChaseCam() { return chaseCam; }
    public World getWorld() { return world; }

    // TODO: Determine accurate win/lose conditions
    public boolean hasWon() {
        return false;
    }

    public boolean hasLost() {
        return false;
    }

    public boolean isCurrentCheckPoint(Checkpoint cp) {
        if (checkpoints.size > 0)
            return cp == currentCheckpoint;
        return false;
    }

    public void setCurrentCheckpoint(Checkpoint cp) {
        currentCheckpoint = cp;
    }

    public void setPlayerSpawnPosition(Vector2 newPos) { spawnPosition.set(newPos.x, newPos.y); }
    public void setPlayerSpawnPosition(float x, float y) { spawnPosition.set(x, y); }

    /*
        Adders and Removers
     */

    public void addActorToStage(Actor actor) { stage.addActor(actor); }

    private void reinitializeAllActors() {
        for (Actor actor: stage.getActors())
            if (actor instanceof Initializeable)
                ((Initializeable) actor).init();
    }

    public void spawnPlayer() {
        Gdx.app.log(TAG, "Spawning player at: (" + spawnPosition.x + ", " + spawnPosition.y + ")");
        player.setPosition(spawnPosition.x, spawnPosition.y);
    }
}
