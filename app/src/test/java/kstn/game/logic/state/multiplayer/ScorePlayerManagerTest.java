package kstn.game.logic.state.multiplayer;

import junit.framework.Assert;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import kstn.game.app.event.BaseEventManager;
import kstn.game.logic.event.EventData;
import kstn.game.logic.event.EventListener;
import kstn.game.logic.playing_event.NextQuestionEvent;
import kstn.game.logic.playing_event.PlayingEventType;
import kstn.game.logic.playing_event.player.NextPlayerEvent;
import kstn.game.logic.playing_event.player.PlayerActivateEvent;
import kstn.game.logic.playing_event.player.PlayerDeactivateEvent;
import kstn.game.logic.playing_event.player.PlayerSetScoreEvent;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.internal.verification.VerificationModeFactory.times;
import static kstn.game.logic.event.EventUtil.*;

public class ScorePlayerManagerTest extends ScorePlayerManagerFixture{
    ScorePlayerManager manager;

    EventListener setScoreListener = mock(EventListener.class);
    EventListener nextPlayerListener = mock(EventListener.class);
    EventListener deactivateListener = mock(EventListener.class);
    EventListener activateListener = mock(EventListener.class);

    ScorePlayer player1 = new ScorePlayer(new Player(100, "a", 1));
    ScorePlayer player2 = new ScorePlayer(new Player(101, "b", 2));
    ScorePlayer player3 = new ScorePlayer(new Player(102, "c", 3));
    ScorePlayer player4 = new ScorePlayer(new Player(103, "d", 4));

    public ScorePlayerManagerTest() {
        eventManager = new BaseEventManager();
        manager = build();

        List<ScorePlayer> scorePlayerList = new ArrayList<>();
        scorePlayerList.add(player1);
        scorePlayerList.add(player2);
        scorePlayerList.add(player3);
        scorePlayerList.add(player4);
        manager.scorePlayerList.addAll(scorePlayerList);

        eventManager.addListener(PlayingEventType.PLAYER_SET_SCORE, setScoreListener);
        eventManager.addListener(PlayingEventType.NEXT_PLAYER, nextPlayerListener);
        eventManager.addListener(PlayingEventType.PLAYER_ACTIVATE, activateListener);
        eventManager.addListener(PlayingEventType.PLAYER_DEACTIVATE, deactivateListener);

        manager.entry();
    }

    @Test
    public void getScoreShouldGetRightPlayer() {
        manager.currentPlayerIndex = 1;
        player2.setScore(22);
        Assert.assertEquals(manager.getScore(), player2.getScore());
    }

    @Test
    public void setScoreShouldSetRightPlayer_AndSendEvent() {
        manager.currentPlayerIndex = 1;
        manager.setScore(100);
        Assert.assertEquals(player2.getScore(), 100);

        update(eventManager);
        verify(setScoreListener).onEvent(new PlayerSetScoreEvent(100));
    }

    @Test
    public void nextPlayerShouldRotateAndSendEvent() {
        manager.currentPlayerIndex = 1;
        Assert.assertEquals(manager.scorePlayerList.size(), 4);

        player2.deactivate();
        player3.deactivate();

        int result = manager.nextPlayer();
        Assert.assertEquals(result, 3);
        Assert.assertEquals(manager.currentPlayerIndex, 3);

        update(eventManager);
        verify(nextPlayerListener).onEvent(new NextPlayerEvent(3));

        result = manager.nextPlayer();
        Assert.assertEquals(result, 0);
        Assert.assertEquals(manager.currentPlayerIndex, 0);

        update(eventManager);
        verify(nextPlayerListener).onEvent(new NextPlayerEvent(0));
    }

    @Test
    public void nextPlayerShouldBackToPrevPlayer_WhenNoOneElseIsActive() {
        manager.currentPlayerIndex = 1;
        Assert.assertEquals(manager.scorePlayerList.size(), 4);

        player1.deactivate();
        player2.deactivate();
        player3.deactivate();

        int result = manager.nextPlayer();
        Assert.assertEquals(result, 3);
        Assert.assertEquals(manager.currentPlayerIndex, 3);

        update(eventManager);
        verify(nextPlayerListener).onEvent(new NextPlayerEvent(3));

        result = manager.nextPlayer();
        Assert.assertEquals(result, 3);
        Assert.assertEquals(manager.currentPlayerIndex, 3);
    }

    @Test
    public void nextPlayer_ReturnMinusOne_NotChangeCurrentIndex_WhenNoPlayerActive() {
        manager.currentPlayerIndex = 2;
        player1.deactivate();
        player2.deactivate();
        player3.deactivate();
        player4.deactivate();

        int result = manager.nextPlayer();

        Assert.assertEquals(result, -1);
        Assert.assertEquals(manager.currentPlayerIndex, 2);
    }

    @Test
    public void deactivatePlayer_SendEvent() {
        Assert.assertTrue(manager.scorePlayerList.get(2).isActive());
        manager.deactivatePlayer(2);

        update(eventManager);
        verify(deactivateListener).onEvent(new PlayerDeactivateEvent(2));
        Assert.assertTrue(manager.scorePlayerList.get(0).isActive());
        Assert.assertFalse(manager.scorePlayerList.get(2).isActive());
    }

    @Test
    public void activatePlayer_SendEvent() {
        manager.scorePlayerList.get(2).deactivate();
        Assert.assertFalse(manager.scorePlayerList.get(2).isActive());
        manager.activatePlayer(2);

        Assert.assertTrue(manager.scorePlayerList.get(0).isActive());
        Assert.assertTrue(manager.scorePlayerList.get(2).isActive());

        update(eventManager);
        verify(activateListener).onEvent(new PlayerActivateEvent(2));
    }


    @Test
    public void deactivateAllExcept_DoOnRightPlayers() {
        player1.deactivate();
        player2.deactivate();

        Assert.assertFalse(manager.scorePlayerList.get(0).isActive());
        // Assert.assertTrue(manager.scorePlayerList.get(1).isActive());
        Assert.assertFalse(manager.scorePlayerList.get(1).isActive());
        Assert.assertTrue(manager.scorePlayerList.get(2).isActive());
        Assert.assertTrue(manager.scorePlayerList.get(3).isActive());

        Assert.assertEquals(manager.currentPlayerIndex, 0);
        manager.deactivateAllExcept(1);
        Assert.assertEquals(manager.currentPlayerIndex, 1);

        Assert.assertFalse(manager.scorePlayerList.get(0).isActive());
        Assert.assertTrue(manager.scorePlayerList.get(1).isActive());
        Assert.assertFalse(manager.scorePlayerList.get(2).isActive());
        Assert.assertFalse(manager.scorePlayerList.get(3).isActive());

        update(eventManager);
        verify(activateListener, times(1))
                .onEvent(any(EventData.class));
        verify(deactivateListener, times(2))
                .onEvent(any(EventData.class));
    }

    @Test
    public void chooseHighestScore_CallDeactiveAllExcept_NextToThatPlayer() {
        player1.setScore(100);
        player2.setScore(500);
        player3.setScore(200);
        player4.setScore(300);
        Assert.assertEquals(manager.currentPlayerIndex, 0);
        manager.chooseBiggestScorePlayer();
        Assert.assertEquals(manager.currentPlayerIndex, 1);

        update(eventManager);

        Assert.assertFalse(player1.isActive());
        Assert.assertTrue(player2.isActive());
        Assert.assertFalse(player3.isActive());
        Assert.assertFalse(player4.isActive());

        verify(nextPlayerListener, times(1))
                .onEvent(new NextPlayerEvent(1));

        verify(deactivateListener, times(3))
                .onEvent(any(EventData.class));
    }

    @Test
    public void playerReadyShouldFindRightPlayer() {
        Assert.assertFalse(player1.isReady());
        Assert.assertFalse(player2.isReady());
        Assert.assertFalse(player3.isReady());
        Assert.assertFalse(player4.isReady());

        manager.playerReady(player3.getPlayer().getIpAddress());

        Assert.assertFalse(player1.isReady());
        Assert.assertFalse(player2.isReady());
        Assert.assertTrue(player3.isReady());
        Assert.assertFalse(player4.isReady());
    }

    @Test
    public void testAreAllPlayersReady_WhenTrue() {
        player1.ready();
        player2.ready();
        player3.ready();
        player4.ready();

        Assert.assertTrue(manager.areAllPlayersReady());
    }

    @Test
    public void testAreAllPlayersReady_WhenFalse() {
        player1.ready();
        player2.ready();
        player4.ready();

        Assert.assertFalse(manager.areAllPlayersReady());
    }
}
