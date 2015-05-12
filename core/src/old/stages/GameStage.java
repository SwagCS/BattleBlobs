package old.stages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.PolygonRegion;
import com.badlogic.gdx.graphics.g2d.PolygonSprite;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.joints.DistanceJointDef;
import com.badlogic.gdx.scenes.scene2d.Stage;
import old.Constants2;
import old.utils.WorldUtils;

import java.util.ArrayList;
import java.util.List;

public class GameStage extends Stage {

    Mesh mesh;

    // This will be our viewport measurements while working with the debug renderer
    private static final int VIEWPORT_WIDTH = 9;
    private static final int VIEWPORT_HEIGHT = 16;


    private World world;
    private Body ground;
    private Body center;
    private List<Body> bodies = new ArrayList<Body>();

    private final float TIME_STEP = 1 / 300f;
    private float accumulator = 0f;

    private OrthographicCamera camera;
    private Box2DDebugRenderer renderer;

    ShapeRenderer shaper = new ShapeRenderer(200);

    public GameStage() {
        world = WorldUtils.createWorld();
        renderer = new Box2DDebugRenderer();
        setupCamera();

        ground = WorldUtils.createGround(world);

        double cx = Constants2.GROUND_X + 5;
        double cy = Constants2.GROUND_Y + 5;

        DistanceJointDef jointDef = new DistanceJointDef();
        jointDef.collideConnected = false;
        jointDef.dampingRatio = 1f;
        jointDef.frequencyHz = 3f;

        int count = 30;
        float radius = 2.4f;

        {
            BodyDef bodyDef = new BodyDef();
            bodyDef.type = BodyType.DynamicBody;
            bodyDef.position.set(new Vector2((float) cx, (float) cy));
            CircleShape shape = new CircleShape();
            shape.setRadius(1f);
            Body body = world.createBody(bodyDef);
            body.createFixture(shape, 1.0f);
        }

        {
            BodyDef bodyDef = new BodyDef();
            bodyDef.type = BodyType.DynamicBody;
            bodyDef.position.set(new Vector2((float) cx, (float) cy));
            CircleShape shape = new CircleShape();
            shape.setRadius(0.01f);
            center = world.createBody(bodyDef);

            FixtureDef fd = new FixtureDef();
            fd.shape = shape;
            fd.density = 1;
            fd.friction = 0.5f;
            fd.restitution = 0.3f;

            center.createFixture(fd);
            shape.dispose();
        }

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

            {
                jointDef.initialize(center, body, center.getPosition(), body.getPosition());
                world.createJoint(jointDef);
            }

            bodies.add(body);
        }

        for (int i = 0; i < bodies.size(); i++) {
            for (int j = 0; j < bodies.size(); j++) {
                Body body1 = bodies.get(i);
                Body body2 = bodies.get(j);

                if (j % 4 != 0)
                    continue;

                if (body1 == body2)
                    continue;

                jointDef.initialize(body1, body2, body1.getPosition(), body2.getPosition());
                world.createJoint(jointDef);
            }
        }

        mesh = new Mesh(true, 4, 0,
                new VertexAttribute(VertexAttributes.Usage.Position, 3, "a_position")
        );
        mesh.setVertices(new float[] { -0.5f, -0.5f, 0,
                0.5f, -0.5f, 0,
                -0.5f, 0.5f, 0,
                0.5f, 0.5f, 0 });



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


        camera.position.set(center.getPosition().x, center.getPosition().y, 0f);
        camera.update();

        Vector3 push = new Vector3();


        if (Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            push.x -= 0.7f;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            push.x += 0.7f;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.UP)) {
            push.y += 0.7f;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            push.y -= 0.7f;
        }

        if (Gdx.input.isTouched()) {
            Vector3 click = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            camera.unproject(click);
            System.out.println(click);


            Vector3 from = new Vector3(center.getPosition().x, center.getPosition().y, 0);
            push = click.cpy().sub(from).nor();
        }


        for (Body body : bodies) {
            body.setLinearDamping(1f);
            body.applyForce(push.x * 5, push.y * 5, body.getPosition().x, body.getPosition().y, true);
        }


        //TODO: Implement interpolation

    }

    @Override
    public void draw() {
        super.draw();
        renderer.render(world, camera.combined);

        float[] vertices = new float[bodies.size() * 2];
        for (int i = 0; i < bodies.size(); i += 1) {
            vertices[i * 2] = bodies.get(i).getPosition().x;
            vertices[i * 2 + 1] = bodies.get(i).getPosition().y;
        }

        PolygonSprite poly;
        PolygonSpriteBatch polyBatch = new PolygonSpriteBatch(); // To assign at the beginning
        Texture textureSolid;

        Pixmap pix = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pix.setColor(Color.RED); // DE is red, AD is green and BE is blue.
        pix.fill();
        textureSolid = new Texture(pix);
        PolygonRegion polyReg = new PolygonRegion(new TextureRegion(textureSolid), vertices, new short[] {
                0, 1, 2,         // Two triangles using vertex indices.
                0, 2, 3,         // Take care of the counter-clockwise direction.
                0, 3, 4,
                0, 4, 5,
                0, 5, 6,
                0, 6, 7,
                0, 7, 8,
                0, 8, 9,
                0, 9, 10,
                0, 10, 11,
                0, 11, 12,
                0, 12, 13
        });
        poly = new PolygonSprite(polyReg);
        polyBatch = new PolygonSpriteBatch();
        polyBatch.setProjectionMatrix(camera.combined);

        polyBatch.begin();
        poly.draw(polyBatch);
        polyBatch.end();

//
//        camera.update();
//        shaper.setProjectionMatrix(camera.combined);
//        shaper.begin(ShapeRenderer.ShapeType.Filled);
//        shaper.setColor(1, 1, 0, 1);
//
//        if (bodies.size() == 0)
//            return;
//
//        float[] vertices = new float[bodies.size() * 2];
//        for (int i = 0; i < bodies.size(); i += 1) {
//            vertices[i * 2] = bodies.get(i).getPosition().x;
//            vertices[i * 2 + 1] = bodies.get(i).getPosition().y;
//        }
//        shaper.polygon(vertices);
        shaper.end();
    }

}