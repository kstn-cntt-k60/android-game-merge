package kstn.game.logic.state_event;

import kstn.game.logic.event.GameEventData;

public class TransitToSinglePlayerState extends GameEventData {
    public TransitToSinglePlayerState() {
        super(StateEventType.SINGLE_PLAYER);
    }
}
