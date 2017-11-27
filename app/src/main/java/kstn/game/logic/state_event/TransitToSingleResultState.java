package kstn.game.logic.state_event;

import kstn.game.logic.event.GameEventData;

public class TransitToSingleResultState extends GameEventData {
    public TransitToSingleResultState() {
        super(StateEventType.SINGLE_RESULT);
    }
}
