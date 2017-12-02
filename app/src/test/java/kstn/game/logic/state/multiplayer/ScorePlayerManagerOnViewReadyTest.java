package kstn.game.logic.state.multiplayer;

import junit.framework.Assert;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import kstn.game.app.event.BaseEventManager;
import kstn.game.logic.event.EventListener;
import kstn.game.logic.network.WifiInfo;
import kstn.game.logic.playing_event.PlayingEventType;
import kstn.game.logic.playing_event.player.PlayerSetAvatarEvent;
import kstn.game.logic.playing_event.player.PlayerSetNameEvent;
import kstn.game.logic.playing_event.player.SetNumberPlayerEvent;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class ScorePlayerManagerOnViewReadyTest extends ScorePlayerManagerFixture {
    ScorePlayerManager manager;
    Player player1 = new Player(2, "X", 4);
    Player player2 = new Player(5, "Y", 2);
    Player player3 = new Player(6, "Z", 1);
    List<Player> playerList = new ArrayList<>();

    EventListener numberPlayerListener = mock(EventListener.class);
    EventListener setNameListener = mock(EventListener.class);
    EventListener setAvatarListener = mock(EventListener.class);

    public ScorePlayerManagerOnViewReadyTest() {
        playerList.add(player1);
        playerList.add(player2);
        playerList.add(player3);

        eventManager = new BaseEventManager();
        wifiInfo = mock(WifiInfo.class);
        thisRoom = mock(ThisRoom.class);
        manager = build();
        manager.scorePlayerList.add(
                new ScorePlayer(new Player(2, "x", 1)));

        eventManager.addListener(PlayingEventType.SET_NUMBER_PLAYER, numberPlayerListener);
        eventManager.addListener(PlayingEventType.PLAYER_SET_NAME, setNameListener);
        eventManager.addListener(PlayingEventType.PlAYER_SET_AVATAR, setAvatarListener);
    }

    @Test
    public void shouldClearPlayerList() {
        manager.onViewReady();
        Assert.assertEquals(manager.scorePlayerList.size(), 0);
    }

    @Test
    public void shouldStorePlayerList() {
        when(thisRoom.getPlayerList()).thenReturn(playerList);
        manager.onViewReady();
        Assert.assertEquals(manager.scorePlayerList.get(0).getPlayer(), player1);
        Assert.assertEquals(manager.scorePlayerList.get(1).getPlayer(), player2);
        Assert.assertEquals(manager.scorePlayerList.get(2).getPlayer(), player3);
    }

    @Test
    public void shouldCheckIsHost_WhenTrue() {
        when(thisRoom.getPlayerList()).thenReturn(playerList);
        when(wifiInfo.getIP()).thenReturn(player1.getIpAddress());
        manager.onViewReady();
        Assert.assertEquals(manager.thisIpAddress, player1.getIpAddress());
        Assert.assertEquals(manager.thisPlayerIsHost(), true);
    }

    @Test
    public void shouldCheckIsHost_WhenFalse() {
        when(thisRoom.getPlayerList()).thenReturn(playerList);
        when(wifiInfo.getIP()).thenReturn(player2.getIpAddress());
        manager.onViewReady();
        Assert.assertEquals(manager.thisPlayerIsHost(), false);
    }

    @Test
    public void shouldCalculateThisPlayerIndex() {
        when(thisRoom.getPlayerList()).thenReturn(playerList);
        when(wifiInfo.getIP()).thenReturn(player2.getIpAddress());
        manager.onViewReady();
        Assert.assertEquals(manager.thisPlayerIndex, 1);
    }

    @Test
    public void shouldSendEventsToView() {
        when(thisRoom.getPlayerList()).thenReturn(playerList);
        manager.onViewReady();

        Assert.assertEquals(manager.currentPlayerIndex, 0);
        verify(numberPlayerListener).onEvent(new SetNumberPlayerEvent(3));

        verify(setAvatarListener).onEvent(new PlayerSetAvatarEvent(0, player1.getAvatarId()));
        verify(setAvatarListener).onEvent(new PlayerSetAvatarEvent(1, player2.getAvatarId()));
        verify(setAvatarListener).onEvent(new PlayerSetAvatarEvent(2, player3.getAvatarId()));

        verify(setNameListener).onEvent(new PlayerSetNameEvent(0, player1.getName()));
        verify(setNameListener).onEvent(new PlayerSetNameEvent(1, player2.getName()));
        verify(setNameListener).onEvent(new PlayerSetNameEvent(2, player3.getName()));
    }
}
