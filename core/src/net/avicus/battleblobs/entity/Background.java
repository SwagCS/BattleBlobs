package net.avicus.battleblobs.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import net.avicus.battleblobs.BattleBlobs;
import net.avicus.battleblobs.Battlefield;

public class Background extends Entity {

    private Texture texture;
    private SpriteBatch batch;

    public Background(Battlefield battlefield) {
        super(battlefield);

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
        int tileCount = 50;

        // This is how we roll
        batch.setProjectionMatrix(BattleBlobs.get().stage.camera.combined);

        // pixels per unit
        float tileW = texture.getWidth() / BattleBlobs.get().stage.ppuX();
        float tileH = texture.getHeight() / BattleBlobs.get().stage.ppuY();

        // ba-da-bing ba-da-boom.
        batch.draw(texture, -16f, -9f, tileW * tileCount, tileH * tileCount, 0, tileCount, tileCount, 0);
        batch.end();
    }

}
