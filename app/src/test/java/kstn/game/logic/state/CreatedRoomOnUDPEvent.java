package kstn.game.logic.state;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import kstn.game.app.event.BaseEventManager;
import kstn.game.app.process.BaseProcessManager;
import kstn.game.logic.event.EventData;
import kstn.game.logic.event.EventListener;
import kstn.game.logic.event.EventManager;
import kstn.game.logic.network.NetworkForwarder;
import kstn.game.logic.network.UDPForwarder;
import kstn.game.logic.playing_event.PlayingEventType;
import kstn.game.logic.playing_event.room.RemoveCreatedRoomEvent;
import kstn.game.logic.playing_event.room.SawCreatedRoomEvent;
import kstn.game.logic.state.multiplayer.ThisPlayer;
import kstn.game.logic.state.multiplayer.ThisRoom;
import kstn.game.view.screen.View;
import kstn.game.view.screen.ViewManager;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

public class CreatedRoomOnUDPEvent {
    private SawCreatedRoomEvent event =
            new SawCreatedRoomEvent(3344, "ABC", 2);

    private EventManager eventManager = new BaseEventManager();
    private BaseProcessManager processManager = new BaseProcessManager();
    private final LogicCreatedRoomsState state;
    private final ViewManager root = mock(ViewManager.class);
    private final View backgroundView = mock(View.class);

    private final ThisPlayer thisPlayer = mock(ThisPlayer.class);
    private final ThisRoom thisRoom = mock(ThisRoom.class);

    private final UDPForwarder forwarder = mock(UDPForwarder.class);
    private final NetworkForwarder networkForwarder = mock(NetworkForwarder.class);

    public CreatedRoomOnUDPEvent() {
        state = new LogicCreatedRoomsState(
                eventManager, root, backgroundView,
                thisPlayer, thisRoom,
                forwarder, networkForwarder, processManager);
        state.entry();
    }

    @Test
    public void shouldSendEventToViewAndCreateProcessToWait() {
        EventListener listener = mock(EventListener.class);
        eventManager.addListener(PlayingEventType.REMOVE_CREATED_ROOM, listener);
        Assert.assertEquals(state.expireProcessMap.size(), 0);

        eventManager.trigger(event);
        Assert.assertEquals(state.expireProcessMap.size(), 1);

        processManager.updateProcesses(3000);
        verify(listener, never()).onEvent(any(EventData.class));
        Assert.assertEquals(state.expireProcessMap.size(), 1);

        processManager.updateProcesses(2001);
        Assert.assertEquals(state.expireProcessMap.size(), 0);

        ArgumentCaptor<EventData> captor = ArgumentCaptor.forClass(EventData.class);
        verify(listener).onEvent(captor.capture());
        RemoveCreatedRoomEvent event1 = (RemoveCreatedRoomEvent) captor.getValue();
        Assert.assertEquals(event1.getIpAddress(), event.getIpAddress());
    }
}
