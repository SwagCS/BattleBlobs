package net.avicus.battleblobs.entity;

import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import net.avicus.battleblobs.Battlefield;

public class UI extends Entity {

    private SpriteBatch batch;
    private BitmapFont font;

    public UI(Battlefield battlefield) {
        super(battlefield);

        this.batch = new SpriteBatch();
        this.font = new BitmapFont();
        font.getData().setScale(2);
    }

    @Override
    public void act(float delta) {

    }

    @Override
    public void draw() {
        batch.begin();
        font.setColor(1, 153f/255f, 51f/255f, 1);
        font.draw(batch, "Score: " + (int) (battlefield.player.area() * 100), 23, 40);
        batch.end();
    }

    @Override
    public Vector2 getPosition() {
        return null;
    }
}
