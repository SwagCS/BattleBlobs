package old;

import com.badlogic.gdx.Game;
import old.screens.GameScreen;

public class BattleBlobs extends Game {

    private static BattleBlobs instance;

    @Override
    public void create() {
        instance = this;
        setScreen(new GameScreen());
    }

    public static BattleBlobs get() {
        return instance;
    }

}
