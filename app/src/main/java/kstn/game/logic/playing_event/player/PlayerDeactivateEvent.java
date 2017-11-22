package kstn.game.logic.playing_event.player;

import kstn.game.logic.event.EventType;
import kstn.game.logic.event.GameEventData;
import kstn.game.logic.playing_event.PlayingEventType;

public class PlayerDeactivateEvent extends GameEventData {
    private final int playerIndex;

    public PlayerDeactivateEvent(int playerIndex) {
        super(PlayingEventType.PLAYER_DEACTIVATE);
        this.playerIndex = playerIndex;
    }

    public int getPlayerIndex() {
        return playerIndex;
    }
}
