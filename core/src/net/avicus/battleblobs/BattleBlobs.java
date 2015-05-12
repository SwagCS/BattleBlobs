package net.avicus.battleblobs;

import com.badlogic.gdx.Game;
import net.avicus.battleblobs.screens.GameScreen;

public class BattleBlobs extends Game {

    @Override
    public void create() {
        setScreen(new GameScreen());
    }

}
