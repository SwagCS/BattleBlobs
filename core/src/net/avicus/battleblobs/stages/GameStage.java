package net.avicus.battleblobs.stages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.joints.DistanceJointDef;
import com.badlogic.gdx.scenes.scene2d.Stage;
import net.avicus.battleblobs.Constants;
import net.avicus.battleblobs.utils.WorldUtils;

import java.util.*;

public class GameStage extends Stage {

    // This will be our viewport measurements while working with the debug renderer
    private static final int VIEWPORT_WIDTH = 9;
    private static final int VIEWPORT_HEIGHT = 16;

    private World world;
    private Body ground;
    private List<Body> bodies = new ArrayList<Body>();

    private final float TIME_STEP = 1 / 300f;
    private float accumulator = 0f;

    private OrthographicCamera camera;
    private Box2DDebugRenderer renderer;

    public GameStage() {
        world = WorldUtils.createWorld();
        renderer = new Box2DDebugRenderer();
        setupCamera();

        ground = WorldUtils.createGround(world);

        double cx = Constants.GROUND_X + 5;
        double cy = Constants.GROUND_Y + 5;

        DistanceJointDef jointDef = new DistanceJointDef();
        jointDef.collideConnected = false;
        jointDef.dampingRatio = 1f;
        jointDef.frequencyHz = 5f;

        int count = 50;
        float radius = 1.5f;

        for (int i = 0; i < count; i++) {
            float angle = (float) ((2.0f * Math.PI * i) / count);
            float x = (float) (cx + radius * Math.sin(angle));
            float y = (float) (cy + radius * Math.cos(angle));

            BodyDef bodyDef = new BodyDef();
            bodyDef.type = BodyType.DynamicBody;
            bodyDef.position.set(new Vector2(x, y));
            CircleShape shape = new CircleShape();
            shape.setRadius(0.05f);
            Body body = world.createBody(bodyDef);

            FixtureDef fd = new FixtureDef();
            fd.shape = shape;
            fd.density = 1;
            fd.friction = 0.5f;
            fd.restitution = 0.3f;

            body.createFixture(fd);
            shape.dispose();

            if (i == count - 1) {
                Body connect = bodies.get(0);
                jointDef.initialize(connect, body, connect.getPosition(), body.getPosition());
                world.createJoint(jointDef);
            }

            if (bodies.size() > 0) {
                Body connect = bodies.get(i - 1);
                jointDef.initialize(connect, body, connect.getPosition(), body.getPosition());
                world.createJoint(jointDef);
            }

            bodies.add(body);
        }

        for (int i = 0; i < bodies.size() / 2; i++) {
            for (int j = i + 2; j < bodies.size(); j += 4) {
                Body body1 = bodies.get(i);
                Body body2 = bodies.get(j);
                jointDef.initialize(body1, body2, body1.getPosition(), body2.getPosition());
                world.createJoint(jointDef);
            }
        }


        // runner = WorldUtils.createRunner(world);

//        // random force
//        Timer timer = new Timer();
//        timer.scheduleAtFixedRate(new TimerTask() {
//
//            Random random = new Random();
//
//            @Override
//            public void run() {
//                float randomForce = random.nextFloat() * 1 + 1;
//                float randomPoint = random.nextInt(5) - 2.5f;
//                for (Body body : bodies)
//                    body.applyLinearImpulse(0, randomForce, body.getPosition().x + randomPoint, body.getPosition().y, true);
//            }
//        }, 0, 2000);
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

        if (Gdx.input.isTouched()) {
            Vector3 click = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(click);
            System.out.println(click);


            for (Body body : bodies) {
                Vector3 from = new Vector3(body.getPosition().x, body.getPosition().y, 0);
                Vector3 push = click.cpy().sub(from).nor();
                body.setLinearVelocity(push.x * 5, push.y * 5);
            }
        }


        //TODO: Implement interpolation

    }

    @Override
    public void draw() {
        super.draw();
        renderer.render(world, camera.combined);
    }

}