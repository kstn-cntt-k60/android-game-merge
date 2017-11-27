package kstn.game.logic.state_event;

import kstn.game.logic.event.GameEventData;

public class TransitToWaitRoom extends GameEventData {
    public TransitToWaitRoom() {
        super(StateEventType.WAIT_ROOM);
    }

}
