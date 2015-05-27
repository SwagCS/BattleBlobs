package net.avicus.battleblobs;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Timer;
import net.avicus.battleblobs.entity.*;
import net.avicus.battleblobs.utils.ControlUtils;

import java.util.*;

public class Battlefield extends Stage {

    public static boolean DEBUG = true;

    public final World world;
    public final OrthographicCamera camera;
    public final Box2DDebugRenderer debugger;

    public final List<Entity> entities = new ArrayList<Entity>();
    public final Blob player;

    public Battlefield() {
        world = new World(new Vector2(0, 0), true);
        entities.add(new Background(this));

        player = new Blob(this, 1, 1, 0.3f, new Color((float) Math.random(), (float) Math.random(), (float) Math.random(), 1));

        entities.add(player);
        entities.add(new Blob(this, 3, 3, 0.01f, new Color((float) Math.random(), (float) Math.random(), (float) Math.random(), 1)));
        entities.add(new UI(this));
        Timer time = new Timer();
        time.scheduleTask(new Timer.Task(){
            @Override
        public void run(){
                entities.add (new Dot(Battlefield.this, 5,5, 0.01f, new Color((float) Math.random(), (float) Math.random(), (float) Math.random(), 1)));

            }
        }, 1, 1);


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
        for (int i = 0; i < entities.size(); i++)
            entities.get(i).act(delta);

        Vector2 dir = ControlUtils.getArrowKeyDirection();
        dir.scl(0.1f);

        player.center.applyForce(dir, player.center.getPosition(), true);
        for (Body body : player.border)
            body.applyForce(dir, body.getPosition(), true);

        camera.position.set(player.getPosition().x, player.getPosition().y, 0f);
        camera.update();

        if (player.radius() > 1)
            camera.zoom = (float) Math.sqrt((Math.sqrt(player.radius()))) * 2f;
        else
            camera.zoom = player.radius() * 2f;
    }

    @Override
    public void draw() {
        super.draw();
        List<Entity> list = new ArrayList<Entity>();
        list.addAll(entities);
        Collections.sort(list, new Comparator<Entity>() {
            @Override
            public int compare(Entity o1, Entity o2) {
                return o1.drawPriority() - o2.drawPriority();
            }
        });
        for (Entity entity : list)
            entity.draw();
        if (DEBUG)
            debugger.render(world, camera.combined);
    }

}
