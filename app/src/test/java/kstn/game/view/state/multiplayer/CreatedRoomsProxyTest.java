package kstn.game.view.state.multiplayer;

import org.junit.Test;

import kstn.game.app.event.BaseEventManager;
import kstn.game.logic.playing_event.room.RemoveCreatedRoomEvent;
import kstn.game.logic.playing_event.room.SawCreatedRoomEvent;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class CreatedRoomsProxyTest {
    private final BaseEventManager eventManager;
    private CreatedRoomsProxy proxy;
    private ICreatedRooms createdRooms = mock(ICreatedRooms.class);

    public CreatedRoomsProxyTest() {
        eventManager = new BaseEventManager();
        proxy = new CreatedRoomsProxy(eventManager, createdRooms);
    }

    @Test
    public void shouldCallAddRoomBeforeExit() {
        proxy.entry();
        eventManager.queue(new SawCreatedRoomEvent(1233, "abc", 3));
        eventManager.update();
        verify(createdRooms, times(1))
                .addRoom(1233, "abc", 3);
    }

    @Test
    public void shouldNotCallAddRoomAfterExit() {
        proxy.entry();
        proxy.exit();
        eventManager.queue(new SawCreatedRoomEvent(1233, "abc", 3));
        eventManager.update();
        verify(createdRooms, times(0))
                .addRoom(1233, "abc", 3);
    }

    @Test
    public void shouldCallRemoveRoomBeforeExit() {
        proxy.entry();
        eventManager.trigger(new RemoveCreatedRoomEvent(1555));
        verify(createdRooms, times(1))
                .removeRoom(1555);
    }

    @Test
    public void shouldNotCallRemoveRoomAfterExit() {
        proxy.entry();
        proxy.exit();
        eventManager.trigger(new RemoveCreatedRoomEvent(1555));
        verify(createdRooms, times(0))
                .removeRoom(1555);
    }
}
