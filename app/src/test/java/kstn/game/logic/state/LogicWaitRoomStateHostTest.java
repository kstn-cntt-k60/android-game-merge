package kstn.game.logic.state;

import junit.framework.Assert;

import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.io.IOException;

import kstn.game.logic.event.EventData;
import kstn.game.logic.event.EventManager;
import kstn.game.logic.network.NetworkForwarder;
import kstn.game.logic.network.UDPForwarder;
import kstn.game.logic.state_event.StateEventType;

import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class LogicWaitRoomStateHostTest {
    private LogicStateManager stateManager = mock(LogicStateManager.class);
    private LogicGameState createdRoomsState = mock(LogicGameState.class);
    private LogicWaitRoomState state;

    private NetworkForwarder networkForwarder = mock(NetworkForwarder.class);
    private UDPForwarder udpForwarder = mock(UDPForwarder.class);

    private EventManager eventManager = mock(EventManager.class);

    public LogicWaitRoomStateHostTest() {
        state = new LogicWaitRoomState(
                stateManager, eventManager, udpForwarder, networkForwarder);
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
    public void shouldTransitToMenuWhenTCPThrows() throws IOException {
        doThrow(new IOException()).when(networkForwarder).listen();
        state.entryWhenIsHost();

        ArgumentCaptor<EventData> eventCaptor = ArgumentCaptor.forClass(EventData.class);
        verify(eventManager).queue(eventCaptor.capture());
        Assert.assertSame(eventCaptor.getValue().getEventType(), StateEventType.MENU);
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
    public void exitShouldShutdownUDPServerAndNotShutdownTCPServerWhenConnectionError() {
        state.entryWhenIsHost();
        state.exitWhenIsHost();
        verify(udpForwarder).shutdown();
        when(stateManager.getNextState()).thenReturn(createdRoomsState);
        when(stateManager.getCreatedRoomsState()).thenReturn(createdRoomsState);
    }

    @Test
    public void exitShouldShutdownUDPServerAndShutdownTCPServerWhenConnectionError() {
        state.entryWhenIsHost();
        state.exitWhenIsHost();
        verify(udpForwarder).shutdown();
        when(stateManager.getNextState()).thenReturn(createdRoomsState);
        when(stateManager.getCreatedRoomsState()).thenReturn(createdRoomsState);
    }

}
