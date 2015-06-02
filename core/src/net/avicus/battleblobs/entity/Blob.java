package net.avicus.battleblobs.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.PolygonRegion;
import com.badlogic.gdx.graphics.g2d.PolygonSprite;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.joints.DistanceJointDef;
import net.avicus.battleblobs.Battlefield;

import java.util.ArrayList;
import java.util.List;

public class Blob extends Entity {

    private static float MINI_RADIUS = 0.01f;
    private static float MINI_COUNT_PER_UNIT = 10f;

    public Body center;
    public List<Body> border = new ArrayList<Body>();

    public final Color color;
    public float area;

    private PolygonSprite poly;
    private PolygonSpriteBatch polyBatch;
    private Texture textureSolid;

    public boolean destroyed;

    public Blob(Battlefield battlefield, float cx, float cy, float area, Color color) {
        super(battlefield);

        this.area = area;
        this.color = color;

        make(cx, cy);
    }

    public void make(float cx, float cy) {
        destroyed = false;
        DistanceJointDef jointDef = new DistanceJointDef();
        jointDef.collideConnected = false;
        jointDef.dampingRatio = 5f;
        jointDef.frequencyHz = 6f;

        float circum = (float) (Math.PI * 2 * radius());

        int count = (int) (circum * 4 * Math.pow(radius(), -0.2));

        // Center
        {
            BodyDef bodyDef = new BodyDef();
            bodyDef.type = BodyType.DynamicBody;
            bodyDef.position.set(new Vector2(cx, cy));
            CircleShape shape = new CircleShape();
            shape.setRadius(MINI_RADIUS);
            center = world.createBody(bodyDef);

            FixtureDef fd = new FixtureDef();
            fd.shape = shape;
            fd.density = 1;
            fd.friction = 0.5f;
            fd.restitution = 0.3f;

            center.createFixture(fd);
            shape.dispose();
        }

        // Circle Border
        border = new ArrayList<Body>();

        for (int i = 0; i < count; i++) {
            float angle = (float) ((2.0f * Math.PI * i) / count);
            float x = (float) (cx + radius() * Math.sin(angle));
            float y = (float) (cy + radius() * Math.cos(angle));

            BodyDef bodyDef = new BodyDef();
            bodyDef.type = BodyType.DynamicBody;
            bodyDef.position.set(new Vector2(x, y));
            CircleShape shape = new CircleShape();
            shape.setRadius(MINI_RADIUS);
            Body body = world.createBody(bodyDef);
            FixtureDef fd = new FixtureDef();
            fd.shape = shape;
            fd.density = 1;
            fd.friction = 0.5f;
            fd.restitution = 0.3f;
            body.createFixture(fd);
            shape.dispose();

            if (i == count - 1) {
                Body connect = border.get(0);
                jointDef.initialize(connect, body, connect.getPosition(), body.getPosition());
                world.createJoint(jointDef);
            }

            if (border.size() > 0) {
                Body connect = border.get(i - 1);
                jointDef.initialize(connect, body, connect.getPosition(), body.getPosition());
                world.createJoint(jointDef);
            }

            jointDef.initialize(center, body, center.getPosition(), body.getPosition());
            world.createJoint(jointDef);

            border.add(body);
        }


        // Joints
        for (int i = 0; i < border.size(); i++) {
            for (int j = 0; j < border.size(); j++) {
                Body body1 = border.get(i);
                Body body2 = border.get(j);

                if (j % MINI_COUNT_PER_UNIT != 0)
                    continue;

                if (body1 == body2)
                    continue;

                jointDef.initialize(body1, body2, body1.getPosition(), body2.getPosition());
                world.createJoint(jointDef);
            }
        }

        Pixmap pix = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pix.setColor(color);
        pix.fill();
        textureSolid = new Texture(pix);

        polyBatch = new PolygonSpriteBatch();
    }

    public void destroy() {
        destroyed = true;
        if (center != null)
            world.destroyBody(center);
        for (Body body : border)
            world.destroyBody(body);
    }

    @Override
    public void act(float delta) {
        center.setLinearDamping(15);
        for (Body body : border)
            body.setLinearDamping(15);

        float radius = radius();

        for (int i = 0; i < battlefield.entities.size(); i++) {
            Entity target = battlefield.entities.get(i);

            if (!(target instanceof Blob))
                continue;

            if (this == target)
                continue;


            Blob blob = (Blob) target;
            double dist = distance(blob);


            if (dist < radius && this.radius() > blob.radius()) {
                Vector2 velocity = center.getLinearVelocity();

                blob.destroy();
                battlefield.entities.remove(blob);

                area += blob.area;
                destroy();
                make(center.getPosition().x, center.getPosition().y);

                center.setLinearVelocity(velocity);
                for (Body body : border)
                    body.setLinearVelocity(velocity);
            }
        }
    }

    @Override
    public void draw() {
        float[] vertices = new float[border.size() * 2];
        short[] triangles = new short[border.size() * 3];

        for (int i = 0; i < border.size(); i++) {
            Body body = border.get(i);
            vertices[i * 2] = body.getPosition().x;
            vertices[i * 2 + 1] = body.getPosition().y;

            // This fixes weird, 0,0 triangle issue.
            if ((i + 2) * 2 >= vertices.length)
                continue;

            triangles[i * 3] = 0;
            triangles[i * 3 + 1] = (short) (i + 1);
            triangles[i * 3 + 2] = (short) (i + 2);
        }

        PolygonRegion polyReg = new PolygonRegion(new TextureRegion(textureSolid), vertices, triangles);
        poly = new PolygonSprite(polyReg);

        polyBatch.setProjectionMatrix(battlefield.camera.combined);

        polyBatch.begin();
        poly.draw(polyBatch);
        polyBatch.end();
    }

    @Override
    public Vector2 getPosition() {
        return center.getPosition();
    }

    public float radius() {
        return (float) Math.pow(area / (3.1415), 0.5);
    }

    @Override
    public int drawPriority() {
        return (int) area * 1000;
    }

}
