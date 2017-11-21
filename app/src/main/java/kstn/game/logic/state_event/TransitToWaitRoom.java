package kstn.game.logic.state_event;

import kstn.game.logic.event.BaseEventData;
import kstn.game.logic.event.EventType;

/**
 * Created by qi on 13/11/2017.
 */

public class TransitToWaitRoom extends BaseEventData {
    public final String roomName;
    public final boolean isHost;

    public TransitToWaitRoom(String roomName, boolean isHost) {
        super(0);
        this.roomName = roomName;
        this.isHost = isHost;
    }

    @Override
    public EventType getEventType() {
        return null;
    }

    @Override
    public String getName() {
        return null;
    }
}
