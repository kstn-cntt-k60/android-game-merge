package kstn.game.logic.state_event;

import kstn.game.logic.event.GameEventData;

public class TransitToPlayingState extends GameEventData {
    public TransitToPlayingState() {
        super(StateEventType.PLAYING);
    }
}
