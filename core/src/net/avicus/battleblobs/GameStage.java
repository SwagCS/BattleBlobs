package net.avicus.battleblobs;

import com.badlogic.gdx.scenes.scene2d.Stage;
import net.avicus.battleblobs.entity.Blob;
import net.avicus.battleblobs.entity.Entity;

import java.util.ArrayList;
import java.util.List;

public class GameStage extends Stage {

    List<Entity> entities = new ArrayList<Entity>();

    public GameStage() {
        entities.add(new Blob());
    }

    @Override
    public void act(float delta) {
        for (Entity e : entities)
            e.act(delta);
    }

    @Override
    public void draw() {
        super.draw();
        for (Entity e : entities)
            e.draw();
    }

}
