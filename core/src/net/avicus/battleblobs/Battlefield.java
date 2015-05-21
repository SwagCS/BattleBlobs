package net.avicus.battleblobs;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import net.avicus.battleblobs.entity.Background;
import net.avicus.battleblobs.entity.Blob;
import net.avicus.battleblobs.entity.Entity;

import java.util.ArrayList;
import java.util.List;

public class Battlefield extends Stage {

    public static boolean DEBUG = true;

    public final World world;
    public final List<Entity> entities = new ArrayList<Entity>();
    public final OrthographicCamera camera;
    public final Box2DDebugRenderer debugger;

    public Battlefield() {
        world = new World(new Vector2(0, 0), true);
        entities.add(new Background(this));

        entities.add(new Blob(this, 1, 1, .6f, Color.RED));
        entities.add(new Blob(this, 3, 3, 2.5f, Color.BLUE));

        camera = createCamera();
        debugger = new Box2DDebugRenderer(true, true, true, false, false, true);
    }

    public float ppuX() {
        return getWidth() / 16f;
    }

    public float ppuY() {
        return getHeight() / 9f;
    }

    private OrthographicCamera createCamera() {
        OrthographicCamera camera = new OrthographicCamera(16, 9);
        camera.position.set(camera.viewportWidth / 2, camera.viewportHeight / 2, 0f);
        camera.update();
        return camera;
    }

    @Override
    public void act(float delta) {
        world.step(1.0F / 300.0F, 6, 2);
        for (Entity e : entities)
            e.act(delta);

        Blob player = (Blob) entities.get(1);

        if (player.getRadius() > 1)
            camera.zoom = (float) Math.sqrt((Math.sqrt(player.getRadius()))) * 2f;
        else
            camera.zoom = player.getRadius() * 2f;
    }

    @Override
    public void draw() {
        super.draw();
        for (Entity e : entities)
            e.draw();
        if (DEBUG)
            debugger.render(world, camera.combined);
    }

}