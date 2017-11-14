package kstn.game.logic.state_event;

import kstn.game.logic.event.BaseEventData;
import kstn.game.logic.event.EventType;

/**
 * Created by qi on 13/11/2017.
 */

public class TransiteToCreatedRoomsState extends BaseEventData {
    public final String playerName;
    public final int avatarId;

    public TransiteToCreatedRoomsState(String playerName, int avatarId) {
        super(0);
        this.playerName = playerName;
        this.avatarId = avatarId;
    }

    @Override
    public EventType getEventType() {
        return StateEventType.CREATED_ROOMS;
    }

    @Override
    public String getName() {
        return null;
    }
}
