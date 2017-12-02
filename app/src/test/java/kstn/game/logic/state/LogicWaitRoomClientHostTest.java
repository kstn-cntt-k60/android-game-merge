package kstn.game.logic.state;

import org.junit.Test;

import kstn.game.app.event.BaseEventManager;
import kstn.game.logic.event.EventData;
import kstn.game.logic.event.EventListener;
import kstn.game.logic.event.EventManager;
import kstn.game.logic.network.Connection;
import kstn.game.logic.network.UDPForwarder;
import kstn.game.logic.playing_event.StartPlayingEvent;
import kstn.game.logic.playing_event.room.ExitRoomEvent;
import kstn.game.logic.process.Process;
import kstn.game.logic.state.multiplayer.ActiveConnections;
import kstn.game.logic.state_event.StateEventType;
import kstn.game.logic.state_event.TransitToCreatedRoomsState;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class LogicWaitRoomClientHostTest {
    private LogicWaitRoomState state;
    private BaseEventManager eventManager = new BaseEventManager();
    private ActiveConnections activeConnections = mock(ActiveConnections.class);
    private UDPForwarder udpForwarder = mock(UDPForwarder.class);
    int ip = 223344;

    public LogicWaitRoomClientHostTest() {
        state = new LogicWaitRoomState(
                null, eventManager,
                null, null,
                null, null,
                null, null,
                 udpForwarder, null,
                 activeConnections,
                null
        );
    }

    @Test
    public void shouldRemoveActiveConnectionWhenExitRoomIsHostNotSameIp() {
        state.isHost = true;
        state.entryBoth();

        EventListener listener = mock(EventListener.class);
        eventManager.addListener(StateEventType.CREATED_ROOMS, listener);

        when(udpForwarder.getIpAddress()).thenReturn(ip);

        ExitRoomEvent event = new ExitRoomEvent(124);
        Connection conn = mock(Connection.class);
        event.setConnection(conn);
        eventManager.trigger(event);
        eventManager.update();

        verify(activeConnections).removeConnection(conn);
        verify(listener, never()).onEvent(any(EventData.class));

        state.exitBoth();
    }

    @Test
    public void shouldToCreatedRoomsWhenExitRoomIsHostSameIp() {
        state.isHost = true;
        state.entryBoth();

        EventListener listener = mock(EventListener.class);
        eventManager.addListener(StateEventType.CREATED_ROOMS, listener);

        when(udpForwarder.getIpAddress()).thenReturn(ip);

        ExitRoomEvent event = new ExitRoomEvent(ip);
        Connection conn = mock(Connection.class);
        event.setConnection(conn);
        eventManager.trigger(event);
        eventManager.update();

        verify(activeConnections, never()).removeConnection(conn);
        verify(listener).onEvent(new TransitToCreatedRoomsState());

        state.exitBoth();
    }

    @Test
    public void shouldToCreatedRoomsWhenExitRoomIsClientSameIp() {
        state.isHost = false;
        when(udpForwarder.getIpAddress()).thenReturn(ip);
        state.entryBoth();

        EventListener listener = mock(EventListener.class);
        eventManager.addListener(StateEventType.CREATED_ROOMS, listener);

        ExitRoomEvent event = new ExitRoomEvent(ip);
        Connection conn = mock(Connection.class);
        event.setConnection(conn);
        eventManager.trigger(event);
        eventManager.update();

        verify(activeConnections, never()).removeConnection(conn);
        verify(listener).onEvent(new TransitToCreatedRoomsState());
    }

    @Test
    public void shouldDoNothingWhenExitRoomIsClientNotSameIp() {
        state.isHost = false;
        when(udpForwarder.getIpAddress()).thenReturn(ip);

        EventListener listener = mock(EventListener.class);
        eventManager.addListener(StateEventType.CREATED_ROOMS, listener);
        eventManager.update();

        state.entryBoth();
        ExitRoomEvent event = new ExitRoomEvent(231);
        Connection conn = mock(Connection.class);
        event.setConnection(conn);
        eventManager.trigger(event);
        verify(activeConnections, never()).removeConnection(conn);
        verify(listener, never()).onEvent(any(EventData.class));
    }
}
