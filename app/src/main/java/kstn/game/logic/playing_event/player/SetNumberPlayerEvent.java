package kstn.game.logic.playing_event.player;

import kstn.game.logic.event.GameEventData;
import kstn.game.logic.playing_event.PlayingEventType;

public class SetNumberPlayerEvent extends GameEventData {
    private final int playerCount;

    public SetNumberPlayerEvent(int playerCount) {
        super(PlayingEventType.SET_NUMBER_PLAYER);
        this.playerCount = playerCount;
    }

    public int getPlayerCount() {
        return playerCount;
    }
}
