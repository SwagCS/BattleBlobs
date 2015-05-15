package net.avicus.battleblobs.entity;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.joints.DistanceJointDef;

public class Wall implements Entity {

    private final Body body;

    public Wall(World world, float cx, float cy) {
        DistanceJointDef jointDef = new DistanceJointDef();
        jointDef.collideConnected = false;
        jointDef.dampingRatio = 1f;
        jointDef.frequencyHz = 3f;

        BodyDef bodyDef = new BodyDef();
        bodyDef.type = BodyType.DynamicBody;
        bodyDef.position.set(new Vector2(cx, cy));
        CircleShape shape = new CircleShape();
        shape.setRadius(0.4f);
        body = world.createBody(bodyDef);

        FixtureDef fd = new FixtureDef();
        fd.shape = shape;
        fd.density = 1;
        fd.friction = 0.5f;
        fd.restitution = 0.3f;

        body.createFixture(fd);
        shape.dispose();
    }

    @Override
    public void act(float delta) {

    }

    @Override
    public void draw() {

    }
}
