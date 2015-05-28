package net.avicus.battleblobs.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import net.avicus.battleblobs.Battlefield;

public class AI extends Blob {

    public AI(Battlefield battlefield, float cx, float cy, float area, Color color) {
        super(battlefield, cx, cy, area, color);
    }

    private float time;
    private float last;

    @Override
    public void act(float delta) {
        super.act(delta);

        time += delta;
        if (time - last < 0.3)
            return;
        last = time;

        System.out.println(time);

        float x = battlefield.player.getPosition().x - getPosition().x;
        float y = battlefield.player.getPosition().y - getPosition().y;

        Vector2 dir = new Vector2(x, y);
        dir.nor();
        dir.scl(5);

        for (Body body : border)
            body.setLinearVelocity(dir);
        center.setLinearVelocity(dir);
    }

}
