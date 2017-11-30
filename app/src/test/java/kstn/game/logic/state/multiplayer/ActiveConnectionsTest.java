package kstn.game.logic.state.multiplayer;

import junit.framework.Assert;

import org.junit.Test;

import kstn.game.logic.network.Connection;

import static org.mockito.Mockito.mock;

public class ActiveConnectionsTest {
    private ActiveConnections activeConnections = new ActiveConnections();

    @Test
    public void testAddConnectionAndIsActive() {
        Connection conn = mock(Connection.class);
        Assert.assertEquals(activeConnections.size(), 0);
        activeConnections.addConnection(conn);

        Assert.assertEquals(activeConnections.size(), 1);
        Assert.assertTrue(activeConnections.isActive(conn));
        activeConnections.removeConnection(conn);
        Assert.assertFalse(activeConnections.isActive(conn));
    }
}
