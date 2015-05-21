package net.avicus.battleblobs;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

public class BattleBlobs extends Game {

    private static BattleBlobs instance;
    public Battlefield stage;

    @Override
    public void create() {
        instance = this;
        this.stage = new Battlefield();
    }

    @Override
    public void render() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        float delta = Gdx.graphics.getDeltaTime();
        stage.draw();
        stage.act(delta);
    }

    public static BattleBlobs get() {
        return instance;
    }

}
