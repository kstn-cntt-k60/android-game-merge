package kstn.game.logic.playing_event.player;

import kstn.game.logic.event.GameEventData;
import kstn.game.logic.playing_event.PlayingEventType;

public class PlayerSetAvatarEvent extends GameEventData {
    private final int playerIndex;
    private final int avatarId;

    public PlayerSetAvatarEvent(int playerIndex, int avatarId) {
        super(PlayingEventType.PlAYER_SET_AVATAR);
        this.playerIndex = playerIndex;
        this.avatarId = avatarId;
    }

    public int getPlayerIndex() {
        return playerIndex;
    }

    public int getAvatarId() {
        return avatarId;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof PlayerSetAvatarEvent) {
            PlayerSetAvatarEvent event = (PlayerSetAvatarEvent) other;
            return this.playerIndex == event.playerIndex
                    && this.avatarId == event.avatarId;
        }
        return false;
    }
}
