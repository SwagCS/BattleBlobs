package net.avicus.battleblobs;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.GL20;

public class Experiment extends Game {

    private GameStage stage;

    @Override
    public void create() {
        stage = new GameStage();
    }

    @Override
    public void render() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        float delta = Gdx.graphics.getDeltaTime();
        stage.draw();
        stage.act(delta);
    }

}
