package kstn.game.view.state.multiplayer;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import kstn.game.app.event.BaseEventManager;
import kstn.game.logic.playing_event.room.AcceptJoinRoomEvent;
import kstn.game.logic.playing_event.room.ExitRoomEvent;
import kstn.game.logic.state.multiplayer.Player;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class WaitRoomProxyTest {
    BaseEventManager eventManager = new BaseEventManager();
    IWaitRoom waitRoom = mock(IWaitRoom.class);
    WaitRoomProxy proxy = new WaitRoomProxy(eventManager, waitRoom);

    @Test
    public void shouldListenToAcceptJoinRoom() {
        proxy.entry();
        Player newPlayer = new Player(22, "XX", 2);
        List<Player> oldPlayers = new ArrayList<>();
        oldPlayers.add(new Player(33, "AA", 1));
        eventManager.trigger(
                new AcceptJoinRoomEvent(newPlayer, oldPlayers));

        verify(waitRoom).addPlayer(new Player(22, "XX", 2));
    }

    @Test
    public void shouldListenToExitRoom() {
        proxy.entry();
        int ip = 555;
        eventManager.trigger(new ExitRoomEvent(ip));
        verify(waitRoom).removePlayer(ip);
        proxy.exit();
    }
}
