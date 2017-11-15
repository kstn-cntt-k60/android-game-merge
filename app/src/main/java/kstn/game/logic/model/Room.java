package kstn.game.logic.model;

import java.util.ArrayList;
import java.util.List;

import kstn.game.logic.state.LogicStateManager;

public class Room {
    private LogicStateManager stateManager;
    private String roomName;
    private List<Player> playerList;
    private Player finalPlayer;
    private int currentLevel;

    public Room(LogicStateManager stateManager, String roomName) {
        this.stateManager = stateManager;
        this.roomName = roomName;
        playerList = new ArrayList<>();
        currentLevel = 0;
    }

    public void exit() {
    }


    private void requestedJoinRoom(int requestId, String playerName, int avatarId) {
    }

    private void acceptJoinRoom(int requestId, int playerId,
                                String playerName, int avatarId)
    {}

    private void nextLevel() {
        currentLevel++;
    }

    public void startPlaying() {
    }

    public void endPlaying() {

    }
}
