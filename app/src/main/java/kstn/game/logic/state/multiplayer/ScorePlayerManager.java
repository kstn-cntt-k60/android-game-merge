package kstn.game.logic.state.multiplayer;

import java.util.ArrayList;
import java.util.List;

import kstn.game.logic.event.EventData;
import kstn.game.logic.event.EventListener;
import kstn.game.logic.event.EventManager;
import kstn.game.logic.network.WifiInfo;
import kstn.game.logic.playing_event.PlayingEventType;
import kstn.game.logic.playing_event.player.NextPlayerEvent;
import kstn.game.logic.playing_event.player.PlayerDeactivateEvent;
import kstn.game.logic.playing_event.player.PlayerSetAvatarEvent;
import kstn.game.logic.playing_event.player.PlayerSetNameEvent;
import kstn.game.logic.playing_event.player.PlayerSetScoreEvent;
import kstn.game.logic.playing_event.player.SetNumberPlayerEvent;

public class ScorePlayerManager {
    private final EventManager eventManager;
    private final WifiInfo wifiInfo;
    private final ThisRoom thisRoom;

    private final EventListener setScoreListener;
    private final EventListener nextPlayerListener;
    private final EventListener deactivateListener;
    private final EventListener nextQuestionListener;

    final List<ScorePlayer> scorePlayerList = new ArrayList<>();
    int thisIpAddress;
    private boolean isHost = false;
    int thisPlayerIndex;
    int currentPlayerIndex;

    public ScorePlayerManager(EventManager eventManager,
                              WifiInfo wifiInfo, ThisRoom thisRoom) {
        this.eventManager = eventManager;
        this.wifiInfo = wifiInfo;
        this.thisRoom = thisRoom;

        setScoreListener = new EventListener() {
            @Override
            public void onEvent(EventData event) {
                PlayerSetScoreEvent event1 = (PlayerSetScoreEvent) event;
                scorePlayerList.get(currentPlayerIndex)
                        .setScore(event1.getScore());
            }
        };

        nextPlayerListener = new EventListener() {
            @Override
            public void onEvent(EventData event) {
                NextPlayerEvent event1 = (NextPlayerEvent) event;
                currentPlayerIndex = event1.getPlayerIndex();
            }
        };

        deactivateListener = new EventListener() {
            @Override
            public void onEvent(EventData event) {
                PlayerDeactivateEvent event1 = (PlayerDeactivateEvent) event;
                scorePlayerList.get(event1.getPlayerIndex()).deactivate();
            }
        };

        nextQuestionListener = new EventListener() {
            @Override
            public void onEvent(EventData event) {
                for (ScorePlayer scorePlayer: scorePlayerList)
                    scorePlayer.activate();
            }
        };
    }

    public void entry() {
        eventManager.addListener(PlayingEventType.PLAYER_SET_SCORE, setScoreListener);
        eventManager.addListener(PlayingEventType.NEXT_PLAYER, nextPlayerListener);
        eventManager.addListener(PlayingEventType.PLAYER_DEACTIVATE, deactivateListener);
        eventManager.addListener(PlayingEventType.NEXT_QUESTION, nextQuestionListener);
    }

    public void exit() {
        eventManager.removeListener(PlayingEventType.NEXT_QUESTION, nextQuestionListener);
        eventManager.removeListener(PlayingEventType.PLAYER_DEACTIVATE, deactivateListener);
        eventManager.removeListener(PlayingEventType.NEXT_PLAYER, nextPlayerListener);
        eventManager.removeListener(PlayingEventType.PLAYER_SET_SCORE, setScoreListener);
    }

    public void onViewReady() {
        scorePlayerList.clear();
        for (Player player: thisRoom.getPlayerList()) {
            scorePlayerList.add(new ScorePlayer(player));
        }
        thisIpAddress = wifiInfo.getIP();
        for (int i = 0; i < scorePlayerList.size(); i++)
            if (scorePlayerList.get(i).getPlayer().getIpAddress() == thisIpAddress) {
                thisPlayerIndex = i;
                break;
            }
        if (thisPlayerIndex == 0)
            isHost = true;
        currentPlayerIndex = 0;

        eventManager.trigger(new SetNumberPlayerEvent(scorePlayerList.size()));
        for (int i = 0; i < scorePlayerList.size(); i++) {
            ScorePlayer scorePlayer = scorePlayerList.get(i);
            eventManager.trigger(new PlayerSetNameEvent(
                    i, scorePlayer.getPlayer().getName()));
            eventManager.trigger(new PlayerSetAvatarEvent(
                    i, scorePlayer.getPlayer().getAvatarId()
            ));
        }
    }

    boolean currentIsThisPlayer() {
        return currentPlayerIndex == thisPlayerIndex;
    }

    boolean thisPlayerIsHost() {
        return isHost;
    }

    int nextPlayer() {
        int i = (currentPlayerIndex + 1) % scorePlayerList.size();
        for (; i != currentPlayerIndex; i = (i + 1) % scorePlayerList.size()) {
            if (scorePlayerList.get(i).isActive()) {
                eventManager.trigger(new NextPlayerEvent(i));
                return i;
            }
        }
        if (scorePlayerList.get(i).isActive())
            return i;
        return -1;
    }

    int getScore() {
        return scorePlayerList.get(currentPlayerIndex).getScore();
    }

    void setScore(int value) {
        eventManager.trigger(new PlayerSetScoreEvent(value));
    }

    int chooseBiggestScorePlayer() {
        int maxScore = 0;
        int maxIndex = 0;
        for (int i = 0; i < scorePlayerList.size(); i++) {
            if (scorePlayerList.get(i).getScore() > maxScore) {
                maxIndex = i;
                maxScore = scorePlayerList.get(i).getScore();
            }
        }
        deactivateAllExcept(maxIndex);
        return nextPlayer();
    }

    void deactivatePlayer(int playerIndex) {
        eventManager.trigger(new PlayerDeactivateEvent(playerIndex));
    }

    void deactivateAllExcept(int playerIndex) {
        for (int i = 0; i < scorePlayerList.size(); i++) {
            if (i != playerIndex && scorePlayerList.get(i).isActive())
                deactivatePlayer(i);
        }
    }

    void playerReady(int ipAddress) {
        for (int i = 0; i < scorePlayerList.size(); i++) {
            if (scorePlayerList.get(i).getPlayer().getIpAddress() == ipAddress) {
                scorePlayerList.get(i).ready();
                break;
            }
        }
    }

    boolean areAllPlayersReady() {
        for (ScorePlayer scorePlayer: scorePlayerList) {
            if (!scorePlayer.isReady())
                return false;
        }
        return true;
    }
}