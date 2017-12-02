package kstn.game.logic.playing_event.player;

import kstn.game.logic.event.GameEventData;
import kstn.game.logic.playing_event.PlayingEventType;

public class PlayerSetNameEvent extends GameEventData {
    private final int playerIndex;
    private final String playerName;

    public PlayerSetNameEvent(int playerIndex, String playerName) {
        super(PlayingEventType.PLAYER_SET_NAME);
        this.playerIndex = playerIndex;
        this.playerName = playerName;
    }

    public int getPlayerIndex() {
        return playerIndex;
    }

    public String getPlayerName() {
        return playerName;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof PlayerSetNameEvent) {
            PlayerSetNameEvent event = (PlayerSetNameEvent) other;
            return this.playerIndex == event.playerIndex
                    && this.playerName.equals(event.playerName);
        }
        return false;
    }
}
