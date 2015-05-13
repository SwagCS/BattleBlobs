package net.avicus.battleblobs.utils;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.math.Vector2;

public class ControlUtils {

    public static Vector2 getArrowKeyDirection() {
        Vector2 dir = new Vector2();
        if (Gdx.input.isKeyPressed(Keys.LEFT))
            dir.x -= 1;
        if (Gdx.input.isKeyPressed(Keys.RIGHT))
            dir.x += 1;
        if (Gdx.input.isKeyPressed(Keys.UP))
            dir.y += 1;
        if (Gdx.input.isKeyPressed(Keys.DOWN))
            dir.y -= 1;
        return dir;
    }

}
