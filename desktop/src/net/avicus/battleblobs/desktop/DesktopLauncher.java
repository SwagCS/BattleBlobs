package net.avicus.battleblobs.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import net.avicus.battleblobs.Constants;
import net.avicus.battleblobs.Experiment;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.width = Constants.WIDTH;
        config.height = Constants.HEIGHT;
		new LwjglApplication(new Experiment(), config);
	}
}
