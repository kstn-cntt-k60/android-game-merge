package kstn.game.logic.state;

import junit.framework.Assert;

import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.io.IOException;

import kstn.game.logic.cone.Cone;
import kstn.game.logic.cone.ConeMessage;
import kstn.game.logic.event.EventData;
import kstn.game.logic.event.EventListener;
import kstn.game.logic.event.EventManager;
import kstn.game.logic.event.EventType;
import kstn.game.logic.network.Connection;
import kstn.game.logic.network.Endpoint;
import kstn.game.logic.network.NetworkForwarder;
import kstn.game.logic.network.Server;
import kstn.game.logic.network.UDPForwarder;
import kstn.game.logic.playing_event.PlayingEventType;
import kstn.game.logic.process.ProcessManager;
import kstn.game.logic.state.multiplayer.ActiveConnections;
import kstn.game.logic.state.multiplayer.ThisPlayer;
import kstn.game.logic.state.multiplayer.ThisRoom;
import kstn.game.logic.state_event.StateEventType;
import kstn.game.view.asset.AssetManager;
import kstn.game.view.state.ViewGameState;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class LogicWaitRoomStateHostTest {
    private LogicStateManager stateManager = mock(LogicStateManager.class);
    private LogicGameState createdRoomsState = mock(LogicGameState.class);
    private LogicWaitRoomState state;

    private NetworkForwarder networkForwarder = mock(NetworkForwarder.class);
    private UDPForwarder udpForwarder = mock(UDPForwarder.class);

    private EventManager eventManager = mock(EventManager.class);
    private ProcessManager processManager = mock(ProcessManager.class);
    private ViewGameState viewGameState = mock(ViewGameState.class);
    private ThisPlayer thisPlayer = mock(ThisPlayer.class);
    private ThisRoom thisRoom = mock(ThisRoom.class);
    private ActiveConnections activeConnections = new ActiveConnections();
    private Cone cone = mock(Cone.class);

    public LogicWaitRoomStateHostTest() {
        when(viewGameState.isReady()).thenReturn(true);
        state = new LogicWaitRoomState(
                stateManager, eventManager,
                processManager, viewGameState,
                null, null,
                thisPlayer, thisRoom,
                udpForwarder, networkForwarder,
                activeConnections,
                cone
        );
    }

    @Test
    public void shouldSetupUDPServer() throws IOException {
        state.entryWhenIsHost();
        verify(udpForwarder).listen();
    }

    @Test
    public void shouldSetupTCPServer() throws IOException {
        state.entryWhenIsHost();
        verify(networkForwarder).listen();
    }

    @Test
    public void shouldTransitToCreatedRoomsWhenTCPThrows() throws IOException {
        doThrow(new IOException()).when(networkForwarder).listen();
        state.entryWhenIsHost();

        ArgumentCaptor<EventData> eventCaptor = ArgumentCaptor.forClass(EventData.class);
        verify(eventManager).queue(eventCaptor.capture());
        Assert.assertSame(eventCaptor.getValue().getEventType(), StateEventType.CREATED_ROOMS);
    }

    @Test
    public void shouldTransitToMenuWhenUDPThrow() throws IOException {
        doThrow(new IOException()).when(udpForwarder).listen();
        state.entryWhenIsHost();

        ArgumentCaptor<EventData> eventCaptor = ArgumentCaptor.forClass(EventData.class);
        verify(eventManager).queue(eventCaptor.capture());
        Assert.assertSame(eventCaptor.getValue().getEventType(), StateEventType.MENU);
    }

    @Test
    public void shouldGoBackCreatedRoomsStateWhenConnectionError() {
        ArgumentCaptor<Endpoint.OnConnectionErrorListener> listenerCaptor
                = ArgumentCaptor.forClass(Endpoint.OnConnectionErrorListener.class);
        state.entryWhenIsHost();
        verify(networkForwarder).setOnConnectionErrorListener(listenerCaptor.capture());
        Connection conn = mock(Connection.class);
        activeConnections.addConnection(conn);
        listenerCaptor.getValue().onConnectionError(conn);
        verify(eventManager).queue(any(EventData.class));
    }

    @Test
    public void shouldGoBackCreatedRoomsStateWhenServerAcceptError() {
        ArgumentCaptor<Server.OnAcceptErrorListener> listenerCaptor
                = ArgumentCaptor.forClass(Server.OnAcceptErrorListener.class);
        state.entryWhenIsHost();
        Assert.assertEquals(state.isHost, true);

        verify(networkForwarder).setOnAcceptErrorListener(listenerCaptor.capture());
        listenerCaptor.getValue().onAcceptError();
        verify(eventManager).queue(any(EventData.class));
    }

    @Test
    public void exitShouldShutdownUDPServerAndShutdownTCPServerWhenBackToCreatedRooms() {
        state.entryWhenIsHost();
        Assert.assertEquals(state.isHost, true);

        when(stateManager.getNextState()).thenReturn(createdRoomsState);
        when(stateManager.getCreatedRoomsState()).thenReturn(createdRoomsState);
        state.exitWhenIsHost();
        verify(udpForwarder).shutdown();
        verify(networkForwarder).shutdown();
    }

    @Test
    public void exitShouldShutdownUDPServerAndNotShutdownTCPServerWhenGoToPlayingState() {
        state.entryWhenIsHost();
        Assert.assertEquals(state.isHost, true);

        when(stateManager.getNextState()).thenReturn(null);
        when(stateManager.getCreatedRoomsState()).thenReturn(createdRoomsState);
        state.exitWhenIsHost();
        verify(udpForwarder).shutdown();
        verify(networkForwarder, never()).shutdown();
    }

    @Test
    public void testIsHost() {
        state.entryWhenIsHost();
        Assert.assertEquals(state.isHost, true);
    }
}
