package kstn.game.logic.state_event;

import kstn.game.logic.event.GameEventData;

public class TransitToRoomCreator extends GameEventData {
    public TransitToRoomCreator() {
        super(StateEventType.ROOM_CREATOR);
    }
}
