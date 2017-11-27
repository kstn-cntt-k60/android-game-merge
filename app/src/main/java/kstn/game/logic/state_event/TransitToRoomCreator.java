package kstn.game.logic.state_event;

import kstn.game.logic.event.BaseEventData;
import kstn.game.logic.event.EventType;

/**
 * Created by qi on 13/11/2017.
 */

public class TransitToRoomCreator extends BaseEventData {
    private  String roomName;

    public TransitToRoomCreator(String roomName) {
        super(0);
        this.roomName = roomName;
    }

    @Override
    public EventType getEventType() {
        return StateEventType.ROOM_CREATOR;
    }

    @Override
    public String getName() {
        return null;
    }
}
