package kstn.game.logic.state;

import org.junit.Test;

import kstn.game.app.event.BaseEventManager;
import kstn.game.logic.event.EventListener;
import kstn.game.logic.event.EventManager;
import kstn.game.logic.network.Connection;
import kstn.game.logic.playing_event.StartPlayingEvent;
import kstn.game.logic.playing_event.room.ExitRoomEvent;
import kstn.game.logic.state.multiplayer.ActiveConnections;
import kstn.game.logic.state_event.StateEventType;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

public class LogicWaitRoomClientHostTest {
    private LogicWaitRoomState state;
    private BaseEventManager eventManager = new BaseEventManager();
    private ActiveConnections activeConnections = mock(ActiveConnections.class);

    public LogicWaitRoomClientHostTest() {
        state = new LogicWaitRoomState(
                null, eventManager,
                null, null,
                null, null,
                null, null,
                null, null,
                 activeConnections,
                null
        );
    }

    @Test
    public void shouldRemoveActiveConnectionWhenExitRoomIsHost() {
        state.isHost = true;
        state.entryBoth();

        ExitRoomEvent event = new ExitRoomEvent(124);
        Connection conn = mock(Connection.class);
        event.setConnection(conn);
        eventManager.trigger(event);

        verify(activeConnections).removeConnection(conn);

        state.exitBoth();
    }

    @Test
    public void shouldNotRemoveActiveConnectionWhenExitRoomIsClient() {
        state.isHost = false;
        state.entryBoth();
        ExitRoomEvent event = new ExitRoomEvent(124);
        Connection conn = mock(Connection.class);
        event.setConnection(conn);
        eventManager.trigger(event);
        verify(activeConnections, never()).removeConnection(conn);
    }
}
