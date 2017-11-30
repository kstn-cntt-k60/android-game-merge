package kstn.game.logic.state;

import junit.framework.Assert;

import org.junit.Test;
import org.mockito.ArgumentCaptor;

import kstn.game.app.event.BaseEventManager;
import kstn.game.logic.cone.Cone;
import kstn.game.logic.event.EventData;
import kstn.game.logic.event.EventListener;
import kstn.game.logic.event.EventManager;
import kstn.game.logic.event.EventType;
import kstn.game.logic.network.Connection;
import kstn.game.logic.network.Endpoint;
import kstn.game.logic.network.NetworkForwarder;
import kstn.game.logic.playing_event.PlayingEventType;
import kstn.game.logic.playing_event.StartPlayingEvent;
import kstn.game.logic.process.Process;
import kstn.game.logic.process.ProcessManager;
import kstn.game.logic.state.multiplayer.ActiveConnections;
import kstn.game.logic.state.multiplayer.ThisPlayer;
import kstn.game.logic.state.multiplayer.ThisRoom;
import kstn.game.logic.state_event.StateEventData;
import kstn.game.logic.state_event.StateEventType;
import kstn.game.logic.state_event.TransitToCreatedRoomsState;
import kstn.game.logic.state_event.TransitToPlayingState;
import kstn.game.view.state.ViewGameState;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class LogicWaitRoomStateClientTest {
    private LogicStateManager stateManager = mock(LogicStateManager.class);
    private LogicGameState createdRoomsState = mock(LogicGameState.class);
    private LogicWaitRoomState state;
    private NetworkForwarder networkForwarder = mock(NetworkForwarder.class);
    private BaseEventManager eventManager = new BaseEventManager();
    private ProcessManager processManager = mock(ProcessManager.class);
    private ViewGameState viewGameState = mock(ViewGameState.class);
    private ThisPlayer thisPlayer = mock(ThisPlayer.class);
    private ThisRoom thisRoom = mock(ThisRoom.class);
    private Cone cone = mock(Cone.class);
    private ActiveConnections activeConnections = mock(ActiveConnections.class);

    public LogicWaitRoomStateClientTest() {
        when(viewGameState.isReady()).thenReturn(true);
        when(activeConnections.isActive(any(Connection.class))).thenReturn(true);

        state = new LogicWaitRoomState(
                stateManager, eventManager,
                processManager, viewGameState,
                null, null,
                thisPlayer, thisRoom,
                null, networkForwarder,
                activeConnections,
                cone
        );
    }

    @Test
    public void shouldToPlayingStateWhenStartPlaying() {
        EventListener playingListener = mock(EventListener.class);
        eventManager.addListener(StateEventType.PLAYING, playingListener);

        state.entryWhenIsClient();
        eventManager.trigger(new StartPlayingEvent());
        eventManager.update();

        verify(playingListener).onEvent(new TransitToPlayingState());
        state.exitWhenIsClient();
    }

    @Test
    public void shouldTransitToCreatedRoomsWhenConnectionError() {
        EventListener createdRoomsListener = mock(EventListener.class);
        eventManager.addListener(StateEventType.CREATED_ROOMS, createdRoomsListener);

        state.entryWhenIsClient();

        ArgumentCaptor<Endpoint.OnConnectionErrorListener> connectionErrorCaptor
                = ArgumentCaptor.forClass(Endpoint.OnConnectionErrorListener.class);
        verify(networkForwarder).setOnConnectionErrorListener(
                connectionErrorCaptor.capture());
        Endpoint.OnConnectionErrorListener listener = connectionErrorCaptor.getValue();
        listener.onConnectionError(null);

        eventManager.update();
        verify(createdRoomsListener).onEvent(new TransitToCreatedRoomsState());
    }

    @Test
    public void shouldShutdownNetworkForwarderWhenBackToCreatedRooms() {
        state.entryWhenIsClient();

        when(stateManager.getNextState()).thenReturn(createdRoomsState);
        when(stateManager.getCreatedRoomsState()).thenReturn(createdRoomsState);
        state.exitWhenIsClient();
        verify(networkForwarder).shutdown();
    }

    @Test
    public void testIsHost() {
        state.entryWhenIsClient();
        Assert.assertEquals(state.isHost, false);
    }
}
