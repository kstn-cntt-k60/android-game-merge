package kstn.game.logic.state_event;

import kstn.game.logic.event.GameEventData;

public class TransitToMenuState extends GameEventData {
    public TransitToMenuState() {
        super(StateEventType.MENU);
    }
}
