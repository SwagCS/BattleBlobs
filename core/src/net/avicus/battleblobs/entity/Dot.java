package net.avicus.battleblobs.entity;

import com.badlogic.gdx.graphics.Color;
import net.avicus.battleblobs.Battlefield;

public class Dot extends Blob {

    public Dot(Battlefield battlefield, float cx, float cy, Color color) {
        super(battlefield, cx, cy, 0.29f, color);
    }

    @Override
    public void act(float delta) {

    }

}
