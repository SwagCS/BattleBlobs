package net.avicus.battleblobs.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import net.avicus.battleblobs.BattleBlobs;
import net.avicus.battleblobs.Constants;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = Constants.WIDTH * 2;
        config.height = Constants.HEIGHT * 2;
		new LwjglApplication(new BattleBlobs(), config);
	}
}
