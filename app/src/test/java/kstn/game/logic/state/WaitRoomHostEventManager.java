package kstn.game.logic.state;

import org.junit.Test;

import kstn.game.app.event.BaseEventManager;
import kstn.game.logic.cone.Cone;
import kstn.game.logic.event.EventData;
import kstn.game.logic.event.EventListener;
import kstn.game.logic.event.EventManager;
import kstn.game.logic.network.Connection;
import kstn.game.logic.network.NetworkForwarder;
import kstn.game.logic.network.UDPForwarder;
import kstn.game.logic.playing_event.PlayingEventType;
import kstn.game.logic.playing_event.room.RequestJoinRoomEvent;
import kstn.game.logic.process.ProcessManager;
import kstn.game.logic.state.multiplayer.ActiveConnections;
import kstn.game.logic.state.multiplayer.ThisPlayer;
import kstn.game.logic.state.multiplayer.ThisRoom;
import kstn.game.view.state.ViewGameState;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class WaitRoomHostEventManager {
    private LogicStateManager stateManager = mock(LogicStateManager.class);
    private LogicGameState createdRoomsState = mock(LogicGameState.class);
    private LogicWaitRoomState state;

    private NetworkForwarder networkForwarder = mock(NetworkForwarder.class);
    private UDPForwarder udpForwarder = mock(UDPForwarder.class);

    private BaseEventManager eventManager = new BaseEventManager();
    private ProcessManager processManager = mock(ProcessManager.class);
    private ViewGameState viewGameState = mock(ViewGameState.class);
    private ThisPlayer thisPlayer = mock(ThisPlayer.class);
    private ThisRoom thisRoom = mock(ThisRoom.class);
    private ActiveConnections activeConnections = mock(ActiveConnections.class);
    private Cone cone = mock(Cone.class);

    public WaitRoomHostEventManager() {
        when(viewGameState.isReady()).thenReturn(true);
        when(activeConnections.isActive(any(Connection.class))).thenReturn(true);

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
    public void shouldListenToRequestJoinRoom() {
        EventListener acceptListener = mock(EventListener.class);
        eventManager.addListener(PlayingEventType.ACCEPT_JOIN_ROOM, acceptListener);
        RequestJoinRoomEvent event = new RequestJoinRoomEvent(1234, "XXX", 3);

        state.entryWhenIsHost();
        eventManager.trigger(event);
        eventManager.update();

        verify(acceptListener).onEvent(any(EventData.class));
    }
}
