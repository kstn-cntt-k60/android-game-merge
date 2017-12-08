package kstn.game.logic.state.multiplayer;

import java.util.ArrayList;
import java.util.List;

import kstn.game.logic.event.EventData;
import kstn.game.logic.event.EventListener;
import kstn.game.logic.event.EventManager;
import kstn.game.logic.network.WifiInfo;
import kstn.game.logic.playing_event.PlayingEventType;
import kstn.game.logic.playing_event.player.NextPlayerEvent;
import kstn.game.logic.playing_event.player.PlayerActivateEvent;
import kstn.game.logic.playing_event.player.PlayerDeactivateEvent;
import kstn.game.logic.playing_event.player.PlayerSetAvatarEvent;
import kstn.game.logic.playing_event.player.PlayerSetNameEvent;
import kstn.game.logic.playing_event.player.PlayerSetScoreEvent;
import kstn.game.logic.playing_event.player.SetNumberPlayerEvent;
import kstn.game.logic.state.IEntryExit;

public class ScorePlayerManager implements IEntryExit {
    private final EventManager eventManager;
    private final WifiInfo wifiInfo;
    private final ThisRoom thisRoom;

    private final EventListener setScoreListener;
    private final EventListener nextPlayerListener;
    private final EventListener activateListener;
    private final EventListener deactivateListener;

    final List<ScorePlayer> scorePlayerList = new ArrayList<>();
    int thisIpAddress;
    boolean isHost;
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

        activateListener = new EventListener() {
            @Override
            public void onEvent(EventData event) {
                PlayerActivateEvent event1 = (PlayerActivateEvent) event;
                scorePlayerList.get(event1.getPlayerIndex()).activate();
            }
        };

        deactivateListener = new EventListener() {
            @Override
            public void onEvent(EventData event) {
                PlayerDeactivateEvent event1 = (PlayerDeactivateEvent) event;
                scorePlayerList.get(event1.getPlayerIndex()).deactivate();
            }
        };
    }

    public void loadInfoFromThisRoom() {
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
        else
            isHost = false;
        currentPlayerIndex = 0;
    }

    @Override
    public void entry() {
        eventManager.addListener(PlayingEventType.PLAYER_SET_SCORE, setScoreListener);
        eventManager.addListener(PlayingEventType.NEXT_PLAYER, nextPlayerListener);
        eventManager.addListener(PlayingEventType.PLAYER_ACTIVATE, activateListener);
        eventManager.addListener(PlayingEventType.PLAYER_DEACTIVATE, deactivateListener);

        playerReady(thisIpAddress);
    }

    @Override
    public void exit() {
        eventManager.removeListener(PlayingEventType.PLAYER_DEACTIVATE, deactivateListener);
        eventManager.removeListener(PlayingEventType.PLAYER_ACTIVATE, activateListener);
        eventManager.removeListener(PlayingEventType.NEXT_PLAYER, nextPlayerListener);
        eventManager.removeListener(PlayingEventType.PLAYER_SET_SCORE, setScoreListener);
    }

    public void onViewReady() {
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

    public boolean currentIsThisPlayer() {
        return currentPlayerIndex == thisPlayerIndex;
    }

    public boolean thisPlayerIsHost() {
        return isHost;
    }

    public int nextPlayer() {
        int i = (currentPlayerIndex + 1) % scorePlayerList.size();
        for (; i != currentPlayerIndex; i = (i + 1) % scorePlayerList.size()) {
            if (scorePlayerList.get(i).isActive()) {
                eventManager.queue(new NextPlayerEvent(i));
                currentPlayerIndex = i;
                return i;
            }
        }
        if (scorePlayerList.get(i).isActive())
            return i;
        return -1;
    }

    public int getScore() {
        return scorePlayerList.get(currentPlayerIndex).getScore();
    }

    public void setScore(int value) {
        eventManager.queue(new PlayerSetScoreEvent(value));
        scorePlayerList.get(currentPlayerIndex).setScore(value);
    }

    public int countActivePlayers() {
        int count = 0;
        for (ScorePlayer scorePlayer: scorePlayerList) {
            if (scorePlayer.isActive())
                count++;
        }
        return count;
    }

    public void chooseBiggestScorePlayer() {
        int maxScore = 0;
        int maxIndex = 0;
        for (int i = 0; i < scorePlayerList.size(); i++) {
            if (scorePlayerList.get(i).getScore() > maxScore) {
                maxIndex = i;
                maxScore = scorePlayerList.get(i).getScore();
            }
        }
        deactivateAllExcept(maxIndex);
    }

    public void activatePlayer(int playerIndex) {
        eventManager.queue(new PlayerActivateEvent(playerIndex));
        scorePlayerList.get(playerIndex).activate();
    }

    public void activateAllPlayers() {
        for (int i = 0; i < scorePlayerList.size(); i++) {
            activatePlayer(i);
        }
    }

    public void deactivateCurrentPlayer() {
        deactivatePlayer(currentPlayerIndex);
    }

    public void deactivatePlayer(int playerIndex) {
        eventManager.queue(new PlayerDeactivateEvent(playerIndex));
        scorePlayerList.get(playerIndex).deactivate();
    }

    public void deactivateAllExcept(int playerIndex) {
        currentPlayerIndex = playerIndex;
        eventManager.queue(new NextPlayerEvent(playerIndex));

        for (int i = 0; i < scorePlayerList.size(); i++) {
            if (i == playerIndex) {
                if (!scorePlayerList.get(i).isActive())
                    activatePlayer(i);
            }
            else if (scorePlayerList.get(i).isActive()){
                deactivatePlayer(i);
            }
        }
    }

    public void playerReady(int ipAddress) {
        for (int i = 0; i < scorePlayerList.size(); i++) {
            if (scorePlayerList.get(i).getPlayer().getIpAddress() == ipAddress) {
                scorePlayerList.get(i).ready();
                break;
            }
        }
    }

    public boolean areAllPlayersReady() {
        for (ScorePlayer scorePlayer: scorePlayerList) {
            if (!scorePlayer.isReady())
                return false;
        }
        return true;
    }
}