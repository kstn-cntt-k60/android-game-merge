package kstn.game.logic.state_event;

import kstn.game.logic.event.GameEventData;

public class TransitToResultState extends GameEventData {
    public TransitToResultState() {
        super(StateEventType.RESULT);
    }
}

