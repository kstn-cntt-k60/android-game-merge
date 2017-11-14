package kstn.game.app.screen;

/**
 * Created by tung on 14/11/2017.
 */

public class SurfaceTick {
    private static int count = 0;

    public static void increase() {
        count++;
    }

    public static int get() {
        return count;
    }
}
