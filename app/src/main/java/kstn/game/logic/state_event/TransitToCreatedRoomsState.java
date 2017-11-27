package kstn.game.logic.state_event;

import kstn.game.logic.event.GameEventData;

public class TransitToCreatedRoomsState extends GameEventData {
    public TransitToCreatedRoomsState() {
        super(StateEventType.CREATED_ROOMS);
    }
}

