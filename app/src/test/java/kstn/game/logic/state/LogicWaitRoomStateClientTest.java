package kstn.game.logic.state;

import junit.framework.Assert;

import org.junit.Test;
import org.mockito.ArgumentCaptor;

import kstn.game.logic.event.EventData;
import kstn.game.logic.event.EventListener;
import kstn.game.logic.event.EventManager;
import kstn.game.logic.event.EventType;
import kstn.game.logic.network.Endpoint;
import kstn.game.logic.network.NetworkForwarder;
import kstn.game.logic.playing_event.PlayingEventType;
import kstn.game.logic.state_event.StateEventType;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class LogicWaitRoomStateClientTest {
    private LogicStateManager stateManager = mock(LogicStateManager.class);
    private LogicGameState createdRoomsState = mock(LogicGameState.class);
    private LogicWaitRoomState state;
    private NetworkForwarder networkForwarder = mock(NetworkForwarder.class);
    private EventManager eventManager = mock(EventManager.class);

    public LogicWaitRoomStateClientTest() {
        state = new LogicWaitRoomState(
                stateManager, eventManager,null, networkForwarder);
    }

    @Test
    public void shouldListenToStartPlaying() {
        state.entryWhenIsClient();

        EventListener startListener;

        ArgumentCaptor<EventType> typeCaptor = ArgumentCaptor.forClass(EventType.class);
        ArgumentCaptor<EventListener> startCaptor = ArgumentCaptor.forClass(EventListener.class);
        verify(eventManager).addListener(typeCaptor.capture(), startCaptor.capture());
        Assert.assertSame(typeCaptor.getValue(), PlayingEventType.START_PLAYING);
        startListener = startCaptor.getValue();

        startListener.onEvent(null);
        ArgumentCaptor<EventData> eventCaptor = ArgumentCaptor.forClass(EventData.class);
        verify(eventManager).queue(eventCaptor.capture());
        Assert.assertSame(eventCaptor.getValue().getEventType(), StateEventType.PLAYING);

        state.exitWhenIsClient();
        verify(eventManager).removeListener(typeCaptor.capture(), startCaptor.capture());
        Assert.assertSame(typeCaptor.getValue(), PlayingEventType.START_PLAYING);
        Assert.assertSame(startCaptor.getValue(), startListener);
    }

    @Test
    public void shouldTransitToCreatedRoomsWhenConnectionError() {
        ArgumentCaptor<Endpoint.OnConnectionErrorListener> connectionErrorCaptor
                = ArgumentCaptor.forClass(Endpoint.OnConnectionErrorListener.class);

        state.entryWhenIsClient();
        verify(networkForwarder).setOnConnectionErrorListener(
                connectionErrorCaptor.capture());
        Endpoint.OnConnectionErrorListener listener = connectionErrorCaptor.getValue();
        listener.onConnectionError(null);

        ArgumentCaptor<EventData> eventCaptor = ArgumentCaptor.forClass(EventData.class);
        verify(eventManager).queue(eventCaptor.capture());
        Assert.assertSame(eventCaptor.getValue().getEventType(), StateEventType.CREATED_ROOMS);
    }

    @Test
    public void shouldShutdownNetworkForwarderWhenBackToCreatedRooms() {
        state.entryWhenIsClient();

        when(stateManager.getNextState()).thenReturn(createdRoomsState);
        when(stateManager.getCreatedRoomsState()).thenReturn(createdRoomsState);
        state.exitWhenIsClient();
        verify(networkForwarder).shutdown();
    }
}
