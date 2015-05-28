package net.avicus.battleblobs.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import net.avicus.battleblobs.BattleBlobs;
import net.avicus.battleblobs.Battlefield;

public class Background extends Entity {

    private Texture texture;
    private SpriteBatch batch;
    private float width;
    private float height;

    public Background(Battlefield battlefield, float width, float height) {
        super(battlefield);
        this.width = width;
        this.height = height;

        texture = new Texture(Gdx.files.internal("grid.png"));
        texture.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
        batch = new SpriteBatch();
    }

    @Override
    public void act(float delta) {

    }

    @Override
    public void draw() {
        batch.begin();

        // This is how we roll
        batch.setProjectionMatrix(BattleBlobs.get().stage.camera.combined);

        // pixels per unit
        float tileW = texture.getWidth() / battlefield.ppuX();
        float tileH = texture.getHeight() / battlefield.ppuY();

        // ba-da-bing ba-da-boom.
        batch.draw(texture, 0, 0, tileW * width, tileH * height, 0, width, height, 0);
        batch.end();
    }

    @Override
    public Vector2 getPosition() {
        return null;
    }

}
