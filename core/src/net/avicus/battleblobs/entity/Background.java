package net.avicus.battleblobs.entity;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureWrap;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.Actor;
import net.avicus.battleblobs.Constants;

public class Background implements Entity{

    private Texture texture;
    private SpriteBatch batch;

    private Rectangle textureRegionBounds1;
    private Rectangle textureRegionBounds2;

    public Background() {
        texture = new Texture(Gdx.files.internal("grid.png"));
        texture.setWrap(TextureWrap.Repeat, TextureWrap.Repeat);
        batch = new SpriteBatch();

        //Not sure if textureRegionBounds2 is needed anymore, it was in the tutorial I was trying to follow. That method ended up with weird black space
        //so I went back to Keenan's. Then I guessed a lot and I think it works. Still need to figure out how to get the blob's speed factor in for the updateBounds
        //method. Right now I guessed and it works okay, but the blob accelerates/decelerates and it messes up.

        //textureRegionBounds1 = new Rectangle(0 - Constants.WIDTH / 2, 0, Constants.WIDTH, Constants.HEIGHT);
        textureRegionBounds1 = new Rectangle(0 - Constants.WIDTH / 2, 0, Constants.WIDTH, Constants.HEIGHT);
        textureRegionBounds2 = new Rectangle(Constants.WIDTH / 2, 0, Constants.WIDTH, Constants.HEIGHT);


    }

    @Override
    public void act(float delta) {
        updateBounds(-delta);
    }

    public void updateBounds(float delta) {
        float speed = 55f/.5f;

        //To account for acceleration we could retrieve a deltaX/Y from Blob and just += that. Dunno how to do it, tried for a few hours and could not get
        //it to update right
        if(Gdx.input.isKeyPressed(Input.Keys.UP)) {
            textureRegionBounds1.y += delta * speed;
            textureRegionBounds2.y += delta * speed;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.DOWN)) {
            textureRegionBounds1.y -= delta * speed;
            textureRegionBounds2.y -= delta * speed;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.LEFT)) {
            textureRegionBounds1.x -= delta * speed;
            textureRegionBounds2.x -= delta * speed;
        }
       if(Gdx.input.isKeyPressed(Input.Keys.RIGHT)) {
            textureRegionBounds1.x += delta * speed;
            textureRegionBounds2.x += delta * speed;
        }

    }

    @Override
    public void draw() {
        batch.begin();
        int tileCount = 50;
        batch.draw(texture, textureRegionBounds1.x, textureRegionBounds1.y, texture.getWidth() * tileCount, texture.getHeight() * tileCount, 0, tileCount, tileCount, 0);
        //batch.draw(texture, textureRegionBounds1.y, 0, texture.getWidth() * tileCount, texture.getHeight() * tileCount, 0, tileCount, tileCount, 0);
        //batch.draw(texture, textureRegionBounds1.x, textureRegionBounds1.y, Constants.WIDTH, Constants.HEIGHT);
        //batch.draw(texture, textureRegionBounds2.x, textureRegionBounds2.y, Constants.WIDTH, Constants.HEIGHT);
        batch.end();
    }

}
