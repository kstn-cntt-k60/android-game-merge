package kstn.game.logic.state;

import junit.framework.Assert;

import org.junit.Test;
import org.mockito.ArgumentCaptor;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import kstn.game.app.event.BaseEventManager;
import kstn.game.app.process.BaseProcessManager;
import kstn.game.logic.event.EventData;
import kstn.game.logic.event.EventListener;
import kstn.game.logic.network.NetworkForwarder;
import kstn.game.logic.network.UDPForwarder;
import kstn.game.logic.playing_event.PlayingEventType;
import kstn.game.logic.playing_event.room.AcceptJoinRoomEvent;
import kstn.game.logic.playing_event.room.ClickRoomEvent;
import kstn.game.logic.playing_event.room.RequestJoinRoomEvent;
import kstn.game.logic.playing_event.room.SetThisRoomEvent;
import kstn.game.logic.process.ProcessManager;
import kstn.game.logic.state.multiplayer.Player;
import kstn.game.logic.state.multiplayer.ThisPlayer;
import kstn.game.logic.state.multiplayer.ThisRoom;
import kstn.game.logic.state_event.StateEventType;
import kstn.game.view.screen.View;
import kstn.game.view.screen.ViewManager;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class LogicCreatedRoomsStateTest {
    private LogicCreatedRoomsState state;
    private BaseEventManager eventManager = new BaseEventManager();
    private ThisPlayer thisPlayer = mock(ThisPlayer.class);
    private ThisRoom thisRoom = mock(ThisRoom.class);
    private ViewManager root = mock(ViewManager.class);
    private View view = mock(View.class);

    private final UDPForwarder udpForwarder = mock(UDPForwarder.class);
    private final NetworkForwarder networkForwarder = mock(NetworkForwarder.class);
    private final ProcessManager processManager;

    private final int ip = 0x1234;
    private final int anotherIp = 0x789a;
    private final ClickRoomEvent clickRoomEvent = new ClickRoomEvent(0x2233);

    private final Player newPlayer;
    private final List<Player> oldPlayers = new ArrayList<>();
    private final AcceptJoinRoomEvent acceptJoinRoomEvent;

    public LogicCreatedRoomsStateTest() {
        processManager = new BaseProcessManager();
        state = new LogicCreatedRoomsState(
                eventManager, root, view,
                thisPlayer, thisRoom,
                udpForwarder, networkForwarder, processManager);

        newPlayer = new Player(ip, "Client", 2);
        oldPlayers.add(new Player(anotherIp, "Server", 3));
        acceptJoinRoomEvent = new AcceptJoinRoomEvent(newPlayer, oldPlayers);
    }

    @Test
    public void shouldAddViewOnEntryAndRemoveOnExit() throws IOException {
        verify(root, times(0)).addView(view);
        state.entry();
        verify(root, times(1)).addView(view);
        verify(root, times(0)).removeView(view);
        state.exit();
        verify(root, times(1)).addView(view);
        verify(root, times(1)).removeView(view);
    }

    @Test
    public void shouldCallForwarderListenOnEntry() throws IOException {
        state.entry();
        verify(udpForwarder, times(1)).listen();
    }

    @Test
    public void shouldCallShutdownForwarderOnExit() {
        state.entry();
        verify(udpForwarder, times(0)).shutdown();
        state.exit();
        verify(udpForwarder, times(1)).shutdown();
    }

    @Test
    public void shouldGoBackLoginStateWhenUDPFactoryThrow() throws IOException {
        doThrow(new IOException()).when(udpForwarder).listen();

        EventListener toLoginListener = mock(EventListener.class);
        eventManager.addListener(StateEventType.LOGIN, toLoginListener);
        state.entry();
        eventManager.update();
        verify(toLoginListener, times(1)).onEvent(any(EventData.class));
    }

    @Test
    public void connectAndHandshakeToHostOnClickRoomEventNotThrow() throws IOException {
        state.entry();

        EventListener requestListener = mock(EventListener.class);
        EventListener acceptListener = mock(EventListener.class);
        EventListener transitToWaitRoomListener = mock(EventListener.class);
        EventListener setRoomListener = mock(EventListener.class);

        eventManager.addListener(PlayingEventType.REQUEST_JOIN_ROOM, requestListener);
        eventManager.addListener(PlayingEventType.ACCEPT_JOIN_ROOM, acceptListener);
        eventManager.addListener(StateEventType.WAIT_ROOM, transitToWaitRoomListener);

        when(udpForwarder.getIpAddress()).thenReturn(ip);

        eventManager.trigger(clickRoomEvent);
        verify(networkForwarder).connect(clickRoomEvent.getIpAddress());

        ArgumentCaptor<EventData> captor = ArgumentCaptor.forClass(EventData.class);
        verify(requestListener).onEvent(captor.capture());
        RequestJoinRoomEvent event = (RequestJoinRoomEvent) captor.getValue();
        Assert.assertEquals(event.getClientIpAddress(), ip);

        eventManager.trigger(acceptJoinRoomEvent);
        eventManager.update();
        verify(transitToWaitRoomListener).onEvent(any(EventData.class));

        // Should call set this Room
        ArgumentCaptor<EventData> roomCaptor = ArgumentCaptor.forClass(EventData.class);
        verify(setRoomListener, times(1)).onEvent(roomCaptor.capture());
        SetThisRoomEvent setRoomEvent = (SetThisRoomEvent) roomCaptor.getValue();
        Assert.assertEquals(setRoomEvent.getIpAddress(), clickRoomEvent.getIpAddress());
        Assert.assertEquals(setRoomEvent.getRoomName(), clickRoomEvent.getRoomName());
    }

    @Test
    public void acceptJoinRoomFromDifferentPerson() throws IOException {
        state.entry();

        EventListener acceptListener = mock(EventListener.class);
        EventListener transitToWaitRoomListener = mock(EventListener.class);

        eventManager.addListener(PlayingEventType.ACCEPT_JOIN_ROOM, acceptListener);
        eventManager.addListener(StateEventType.WAIT_ROOM, transitToWaitRoomListener);

        when(udpForwarder.getIpAddress()).thenReturn(ip);

        AcceptJoinRoomEvent event = new AcceptJoinRoomEvent(
                new Player(anotherIp, "SERVER", 10),
                new ArrayList<Player>());

        eventManager.trigger(event);
        eventManager.update();
        verify(transitToWaitRoomListener, never()).onEvent(any(EventData.class));
    }

    @Test
    public void connectAndHandshakeToHostOnClickRoomEventWithThrow() throws IOException {
        doThrow(new IOException()).when(networkForwarder).connect(clickRoomEvent.getIpAddress());
        state.entry();

        eventManager.trigger(clickRoomEvent);
        verify(networkForwarder).connect(clickRoomEvent.getIpAddress());
        verify(networkForwarder).shutdown();
    }

    @Test
    public void shouldCallThisRoomThisPlayerEntryExit() {
        state.entry();
        verify(thisPlayer).entry();
        verify(thisRoom).entry();
        verify(thisPlayer, never()).exit();
        verify(thisRoom, never()).exit();
        state.exit();
        verify(thisPlayer).exit();
        verify(thisRoom).exit();
    }
}
