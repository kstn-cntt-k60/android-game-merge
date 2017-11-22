package kstn.game.logic.playing_event.player;

import kstn.game.logic.event.GameEventData;
import kstn.game.logic.playing_event.PlayingEventType;

public class NextPlayerEvent extends GameEventData {
    private final int playerIndex;

    public NextPlayerEvent(int playerIndex) {
        super(PlayingEventType.NEXT_PLAYER);
        this.playerIndex = playerIndex;
    }

    public int getPlayerIndex() {
        return playerIndex;
    }
}
