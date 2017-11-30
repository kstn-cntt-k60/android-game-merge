package kstn.game.logic.state.multiplayer;

import junit.framework.Assert;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import kstn.game.app.event.BaseEventManager;
import kstn.game.logic.event.EventListener;
import kstn.game.logic.event.EventManager;
import kstn.game.logic.playing_event.room.AcceptJoinRoomEvent;
import kstn.game.logic.playing_event.room.ExitRoomEvent;
import kstn.game.logic.playing_event.room.SetThisRoomEvent;

public class ThisRoomTest {
    private final EventManager eventManager = new BaseEventManager();
    private final ThisRoom thisRoom;
    private SetThisRoomEvent event
            = new SetThisRoomEvent("Tung", 1222);

    private int ip = 0x3244;
    private int anotherIp = 0x2211;
    private final Player newPlayer;
    private final List<Player> oldPlayers = new ArrayList<>();
    private final AcceptJoinRoomEvent acceptJoinRoomEvent;

    public ThisRoomTest() {
        thisRoom = new ThisRoom(eventManager);
        newPlayer = new Player(ip, "Client", 2);
        oldPlayers.add(new Player(anotherIp, "Server", 3));
        acceptJoinRoomEvent = new AcceptJoinRoomEvent(newPlayer, oldPlayers);
    }

    @Test
    public void shouldListenSetRoomEventOnEntry() {
        thisRoom.entry();
        eventManager.trigger(event);
        Assert.assertEquals(thisRoom.getRoomName(), event.getRoomName());
        Assert.assertEquals(thisRoom.getIpAddress(), event.getIpAddress());
    }

    @Test
    public void shouldNotListenSetRoomEventOnExit() {
        thisRoom.entry();
        thisRoom.exit();
        eventManager.trigger(event);
        Assert.assertEquals(thisRoom.getRoomName(), "");
        Assert.assertEquals(thisRoom.getIpAddress(), 0);
    }

    @Test
    public void acceptJoinRoomOnEntry() {
        thisRoom.entry();
        eventManager.trigger(acceptJoinRoomEvent);
        List<Player> players = thisRoom.getPlayerList();
        Assert.assertEquals(players.get(0).getIpAddress(), anotherIp);
        Assert.assertEquals(players.get(0).getName(), "Server");
        Assert.assertEquals(players.get(0).getAvatarId(), 3);

        Assert.assertEquals(players.get(1).getIpAddress(), ip);
        Assert.assertEquals(players.get(1).getName(), "Client");
        Assert.assertEquals(players.get(1).getAvatarId(), 2);

        eventManager.trigger(acceptJoinRoomEvent);
        Assert.assertSame(players, thisRoom.getPlayerList());
        Assert.assertEquals(players.get(0).getIpAddress(), anotherIp);
        Assert.assertEquals(players.get(0).getName(), "Server");
        Assert.assertEquals(players.get(0).getAvatarId(), 3);

        Assert.assertEquals(players.get(1).getIpAddress(), ip);
        Assert.assertEquals(players.get(1).getName(), "Client");
        Assert.assertEquals(players.get(1).getAvatarId(), 2);
    }

    @Test
    public void shouldListenToExitRoom() {
        thisRoom.entry();
        int ip = 244;
        thisRoom.getPlayerList().add(
                new Player(ip, "Tung", 3));
        Assert.assertEquals(thisRoom.getPlayerList().size(), 1);
        eventManager.trigger(new ExitRoomEvent(22));
        Assert.assertEquals(thisRoom.getPlayerList().size(), 1);
        eventManager.trigger(new ExitRoomEvent(ip));
        Assert.assertEquals(thisRoom.getPlayerList().size(), 0);
    }
}
