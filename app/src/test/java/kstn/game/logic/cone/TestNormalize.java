package kstn.game.logic.cone;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Created by qi on 16/11/2017.
 */

public class TestNormalize {
    @Test
    public void normalize() {
        assertEquals(Cone.normalize(360), 0, 0.0001);
        assertEquals(Cone.normalize(359), 359, 0.0001);
        assertEquals(Cone.normalize(0), 0, 0.0001);
        assertEquals(Cone.normalize(-1), 359, 0.0001);
        assertEquals(Cone.normalize(-360), 0, 0.0001);
    }
}
