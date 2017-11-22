package kstn.game.logic.state_event;

import kstn.game.logic.event.GameEventData;

public class TransitToCreatedRoomsState extends GameEventData {
    private final String playerName;
    private final int avatarId;

    public TransitToCreatedRoomsState(String playerName, int avatarId) {
        super(StateEventType.CREATED_ROOMS);
        this.playerName = playerName;
        this.avatarId = avatarId;
    }

    public int getAvatarId() {
        return avatarId;
    }

    public String getPlayerName() {
        return playerName;
    }
}
