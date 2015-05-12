package net.avicus.battleblobs.stages;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import net.avicus.battleblobs.utils.WorldUtils;

import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class GameStage extends Stage {

    // This will be our viewport measurements while working with the debug renderer
    private static final int VIEWPORT_WIDTH = 9;
    private static final int VIEWPORT_HEIGHT = 16;

    private World world;
    private Body ground;
    private Body runner;

    private final float TIME_STEP = 1 / 300f;
    private float accumulator = 0f;

    private OrthographicCamera camera;
    private Box2DDebugRenderer renderer;

    public GameStage() {
        world = WorldUtils.createWorld();
        ground = WorldUtils.createGround(world);
        runner = WorldUtils.createRunner(world);
        renderer = new Box2DDebugRenderer();
        setupCamera();

        // random force
        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {

            Random random = new Random();

            @Override
            public void run() {
                float randomForce = random.nextFloat() * 3 + 1;
                float randomPoint = random.nextInt(5) - 2.5f;
                runner.applyLinearImpulse(0, randomForce, runner.getPosition().x + randomPoint, runner.getPosition().y, true);
            }
        }, 0, 2000);
    }

    private void setupCamera() {
        camera = new OrthographicCamera(VIEWPORT_WIDTH, VIEWPORT_HEIGHT);
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0f);
        camera.update();
    }

    @Override
    public void act(float delta) {
        super.act(delta);

        // Fixed timestep
        accumulator += delta;

        while (accumulator >= delta) {
            world.step(TIME_STEP, 6, 2);
            accumulator -= TIME_STEP;
        }


        //TODO: Implement interpolation

    }

    @Override
    public void draw() {
        super.draw();
        renderer.render(world, camera.combined);
    }

}