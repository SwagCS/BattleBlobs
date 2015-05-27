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
import com.badlogic.gdx.physics.box2d.CircleShape;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.badlogic.gdx.physics.box2d.joints.DistanceJointDef;
import net.avicus.battleblobs.BattleBlobs;
import net.avicus.battleblobs.Battlefield;
import net.avicus.battleblobs.utils.ControlUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Alex on 5/27/2015.
 */
public class Dot extends Entity {

    private static float MINI_RADIUS = 0.01f;
    private static float MINI_COUNT_PER_UNIT = 10f;

    public Body center;
    public List<Body> border = new ArrayList<Body>();

    public final Color color;
    public float volume;

    public Dot(Battlefield battlefield, float cx, float cy, float volume, Color color) {
        super(battlefield);

        this.volume = volume;
        this.color = color;

        make(cx, cy);
    }

    public void make(float cx, float cy) {
        DistanceJointDef jointDef = new DistanceJointDef();
        jointDef.collideConnected = false;
        jointDef.dampingRatio = 1f;
        jointDef.frequencyHz = 3f;

        float circum = (float) (Math.PI * 2 * radius());

        int count = (int) (circum * 4 * Math.pow(radius(), -0.2));

        // Center
        {
            BodyDef bodyDef = new BodyDef();
            bodyDef.type = BodyDef.BodyType.DynamicBody;
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
            bodyDef.type = BodyDef.BodyType.DynamicBody;
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
    }

    public void destroy() {
        if (center != null)
            world.destroyBody(center);
        for (Body body : border)
            world.destroyBody(body);
    }

    @Override
    public void act(float delta) {

    }

    @Override
    public void draw() {
        PolygonSprite poly;
        PolygonSpriteBatch polyBatch;
        Texture textureSolid;

        Pixmap pix = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pix.setColor(color);
        pix.fill();
        textureSolid = new Texture(pix);

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
        polyBatch = new PolygonSpriteBatch();
        polyBatch.setProjectionMatrix(BattleBlobs.get().stage.camera.combined);

        polyBatch.begin();
        poly.draw(polyBatch);
        polyBatch.end();
    }

    @Override
    public Vector2 getPosition() {
        return center.getPosition();
    }

    public float radius() {
        return (float) Math.pow(volume / (1.3333 * 3.1416), 0.3333);
    }

    @Override
    public int drawPriority() {
        return (int) volume * 1000;
    }

}
