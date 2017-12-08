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
import static kstn.game.logic.event.EventUtil.*;

public class ScorePlayerManagerInitTest extends ScorePlayerManagerFixture {
    ScorePlayerManager manager;
    Player player1 = new Player(2, "X", 4);
    Player player2 = new Player(5, "Y", 2);
    Player player3 = new Player(6, "Z", 1);
    List<Player> playerList = new ArrayList<>();

    public ScorePlayerManagerInitTest() {
        playerList.add(player1);
        playerList.add(player2);
        playerList.add(player3);

        eventManager = new BaseEventManager();
        wifiInfo = mock(WifiInfo.class);
        thisRoom = mock(ThisRoom.class);
        manager = build();
        manager.scorePlayerList.add(
                new ScorePlayer(new Player(2, "x", 1)));
    }

    @Test
    public void loadInfo_ClearPlayerList() {
        manager.loadInfoFromThisRoom();
        Assert.assertEquals(manager.scorePlayerList.size(), 0);
    }

    @Test
    public void loadInfo_StorePlayerList() {
        when(thisRoom.getPlayerList()).thenReturn(playerList);
        manager.loadInfoFromThisRoom();
        Assert.assertEquals(manager.scorePlayerList.get(0).getPlayer(), player1);
        Assert.assertEquals(manager.scorePlayerList.get(1).getPlayer(), player2);
        Assert.assertEquals(manager.scorePlayerList.get(2).getPlayer(), player3);
    }

    @Test
    public void loadInfo_CheckIsHost_WhenTrue() {
        when(thisRoom.getPlayerList()).thenReturn(playerList);
        when(wifiInfo.getIP()).thenReturn(player1.getIpAddress());
        manager.loadInfoFromThisRoom();
        Assert.assertEquals(manager.thisIpAddress, player1.getIpAddress());
        Assert.assertEquals(manager.thisPlayerIsHost(), true);
    }

    @Test
    public void loadInfo_CheckIsHost_WhenFalse() {
        manager.isHost = true;
        when(thisRoom.getPlayerList()).thenReturn(playerList);
        when(wifiInfo.getIP()).thenReturn(player2.getIpAddress());
        manager.loadInfoFromThisRoom();
        // manager.onViewReady();
        Assert.assertEquals(manager.thisPlayerIsHost(), false);
    }

    @Test
    public void loadInfo_CalculateThisPlayerIndex() {
        when(thisRoom.getPlayerList()).thenReturn(playerList);
        when(wifiInfo.getIP()).thenReturn(player2.getIpAddress());
        manager.loadInfoFromThisRoom();
        Assert.assertEquals(manager.thisPlayerIndex, 1);
    }

    @Test
    public void onViewReady_Send_NumberPlayers_SetName_SetAvatar_Events_ToView() {
        when(thisRoom.getPlayerList()).thenReturn(playerList);

        EventListener numberPlayerListener
                = getMockedListener(eventManager, PlayingEventType.SET_NUMBER_PLAYER);
        EventListener setAvatarListener
                = getMockedListener(eventManager, PlayingEventType.PlAYER_SET_AVATAR);
        EventListener setNameListener
                = getMockedListener(eventManager, PlayingEventType.PLAYER_SET_NAME);

        manager.loadInfoFromThisRoom();
        manager.onViewReady();

        Assert.assertEquals(manager.currentPlayerIndex, 0);
        assertTriggered(numberPlayerListener, new SetNumberPlayerEvent(3));

        verify(setAvatarListener).onEvent(new PlayerSetAvatarEvent(0, player1.getAvatarId()));
        verify(setAvatarListener).onEvent(new PlayerSetAvatarEvent(1, player2.getAvatarId()));
        verify(setAvatarListener).onEvent(new PlayerSetAvatarEvent(2, player3.getAvatarId()));

        verify(setNameListener).onEvent(new PlayerSetNameEvent(0, player1.getName()));
        verify(setNameListener).onEvent(new PlayerSetNameEvent(1, player2.getName()));
        verify(setNameListener).onEvent(new PlayerSetNameEvent(2, player3.getName()));
    }

    @Test
    public void loadInfo_setCurrentPlayerIndex_ToZero() {
        manager.currentPlayerIndex = 3;
        manager.loadInfoFromThisRoom();
        Assert.assertEquals(manager.currentPlayerIndex, 0);
    }
}
