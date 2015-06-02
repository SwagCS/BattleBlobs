package net.avicus.battleblobs;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import net.avicus.battleblobs.entity.*;

import java.util.*;

public class Battlefield extends Stage {

    public static boolean DEBUG = false;

    public final World world;
    public final OrthographicCamera camera;
    public final Box2DDebugRenderer debugger;

    public final List<Entity> entities = new ArrayList<Entity>();

    public final Player player;

    private Color color;

    public Battlefield(float width, float height) {
        world = new World(new Vector2(0, 0), true);
        camera = createCamera();
        debugger = new Box2DDebugRenderer(true, true, true, false, false, true);

        entities.add(new Background(this, width, height));

        entities.add(new Wall(this, 0, -0.64f, 0.32f * width, 0.32f));
        entities.add(new Wall(this, -0.64f, 0, 0.32f, 0.32f * height));
        entities.add(new Wall(this, 0, 2 * 0.315f * height, 0.32f * width, 0.32f));
        entities.add(new Wall(this, 2 * 0.3125f * width, 0, 0.32f, 0.315f * height));

        entities.add(new Player(this, width / 2.5f, height / 2.5f));
        player = (Player) entities.get(entities.size() - 1);

        Random rand = new Random();

        int ai = 100;

        for(int i = 0; i < ai; i++) {
            color = new Color((float) Math.random(), (float) Math.random(), (float) Math.random(), 1);
            entities.add(new AI(this, rand.nextInt((int) width), rand.nextInt((int) height), (float)Math.random()*1f, color));
        }
        entities.add(new UI(this));


        int dots = 300;

        for (int i = 0; i < dots; i++)
            entities.add(new Dot(this, rand.nextInt((int) width), rand.nextInt((int) height), new Color((float) Math.random(), (float) Math.random(), (float) Math.random(), 1)));
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

        camera.update();
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
