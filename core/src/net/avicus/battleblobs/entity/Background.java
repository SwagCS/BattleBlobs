package net.avicus.battleblobs.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Background implements Entity {

    private Texture texture;
    private SpriteBatch batch;

    public Background() {
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
        batch.draw(texture, 0, 0, texture.getWidth() * tileCount, texture.getHeight() * tileCount, 0, tileCount, tileCount, 0);
        batch.end();
    }

}
