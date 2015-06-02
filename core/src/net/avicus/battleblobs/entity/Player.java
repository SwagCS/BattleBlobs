package net.avicus.battleblobs.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import net.avicus.battleblobs.Battlefield;
import net.avicus.battleblobs.utils.ControlUtils;

import java.util.ArrayList;
import java.util.List;

public class Player extends Entity {

    private List<Blob> blobs = new ArrayList<Blob>();
    private Color color;

    public Player(Battlefield battlefield, float x, float y) {
        super(battlefield);
        this.color = new Color((float) Math.random(), (float) Math.random(), (float) Math.random(), 1);

        blobs.add(new Blob(battlefield, x, y, 0.10f, color));
        battlefield.entities.addAll(blobs);
    }

    public boolean over = false;

    private Vector2 lastPosition = new Vector2();

    @Override
    public void act(float delta) {
        if (blobs.get(0).destroyed) {
            Vector2 dir = ControlUtils.getArrowKeyDirection();
            dir.scl(0.05f);
            Vector2 position = lastPosition.add(dir);
            battlefield.camera.position.set(position.x, position.y, 0f);
            over = true;
            return;
        }

        Vector2 dir = ControlUtils.getArrowKeyDirection();
        dir.scl(0.05f);

        for (Blob blob : blobs) {
            blob.center.applyForce(dir, blob.center.getPosition(), true);
            for (Body body : blob.border)
                body.applyForce(dir, body.getPosition(), true);
        }

        Vector2 position = getPosition();
        battlefield.camera.position.set(position.x, position.y, 0f);

        battlefield.camera.zoom = (float) Math.sqrt((Math.sqrt(area()))) * 1.5f;
    }

    @Override
    public void draw() {

    }

    public float area() {
        float area = 0;
        for (Blob blob : blobs)
            area += blob.area;
        return area;
    }

    @Override
    public Vector2 getPosition() {
        float x = 0;
        float y = 0;
        for (Blob blob : blobs) {
            x += blob.getPosition().x;
            y += blob.getPosition().y;
        }

        return lastPosition = new Vector2(x / (float) blobs.size(), y / (float) blobs.size());
    }

}