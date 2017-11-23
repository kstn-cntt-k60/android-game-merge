package kstn.game.logic.state_event;

import kstn.game.logic.event.GameEventData;

public class TransitToLoginState extends GameEventData {

    public TransitToLoginState() {
        super(StateEventType.LOGIN);
    }
}
