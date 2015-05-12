package old;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;

public class Constants2 {

    public static final int APP_WIDTH = 360;
    public static final int APP_HEIGHT = 640;

    public static final Vector2 WORLD_GRAVITY = new Vector2(0, 0);

    public static final float GROUND_X = 0;
    public static final float GROUND_Y = 1;
    public static final float GROUND_WIDTH = 50f;
    public static final float GROUND_HEIGHT = 2f;
    public static final float GROUND_DENSITY = 0f;

    public static final float RUNNER_WIDTH = 1f;
    public static final float RUNNER_HEIGHT = 2f;
    public static float RUNNER_DENSITY = 0.5f;

    public static Color COLORS[] = { new Color(0f, .8f, 0f, 1f), // Green
            new Color(.2f, .25f, .95f, 1f), // Blue
            new Color(.9f, 0f, 0f, 1f), // Red
            new Color(1f, .5f, 0f, 1f), // Orange
            new Color(1f, 1f, .0f, 1f), // Yellow
            new Color(.5f, .12f, 1f, 1f), // Purple
            new Color(.21f, .71f, .9f, 1f), // Cyan
            new Color(.3f, .1f, 0f, 1f), // Dark Red
            new Color(0f, .3f, 0f, 1f), // Dark Green
            new Color(1f, .5f, .9f, 1f), // Pink
            new Color(0f, 0f, 0f, 1f), // Black
            new Color(1f, 1f, 1f, 1f), // White
            new Color(1f, 1f, 1f, 1f), // White
            new Color(.8f, .8f, .8f, .5f) // Gray
    };

}