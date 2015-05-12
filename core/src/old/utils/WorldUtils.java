package old.utils;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;
import old.Constants2;

public class WorldUtils {

    public static World createWorld() {
        return new World(Constants2.WORLD_GRAVITY, true);
    }

    public static Body createGround(World world) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(new Vector2(Constants2.GROUND_X, Constants2.GROUND_Y));
        Body body = world.createBody(bodyDef);
        PolygonShape shape = new PolygonShape();
        shape.setAsBox(Constants2.GROUND_WIDTH / 2, Constants2.GROUND_HEIGHT / 2);
        body.createFixture(shape, Constants2.GROUND_DENSITY);
        shape.dispose();
        return body;
    }

}