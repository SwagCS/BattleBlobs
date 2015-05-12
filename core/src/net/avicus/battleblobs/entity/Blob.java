package net.avicus.battleblobs.entity;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.PolygonRegion;
import com.badlogic.gdx.graphics.g2d.PolygonSprite;
import com.badlogic.gdx.graphics.g2d.PolygonSpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Blob implements Entity {
    @Override
    public void act(float delta) {
        PolygonSprite poly;
        PolygonSpriteBatch polyBatch;
        Texture textureSolid;

        Pixmap pix = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pix.setColor(Color.DARK_GRAY);
        pix.fill();
        textureSolid = new Texture(pix);
        PolygonRegion polyReg = new PolygonRegion(new TextureRegion(textureSolid),
                new float[] {
                        0, 0,
                        100, 0,
                        100, 100,
                        0, 100
                },
                new short[]{
                        0, 1, 2,
                        0, 2, 3,
                        0, 3, 4
                });
        poly = new PolygonSprite(polyReg);
        polyBatch = new PolygonSpriteBatch();

        polyBatch.begin();
        poly.draw(polyBatch);
        polyBatch.end();
    }

    @Override
    public void draw() {

    }
}
