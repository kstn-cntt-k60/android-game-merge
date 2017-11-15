package kstn.game.logic.playing_event;

import kstn.game.logic.event.BaseEventData;
import kstn.game.logic.event.EventType;

/**
 * Created by qi on 14/11/2017.
 */

public class NextTurnEvent extends BaseEventData {
    private final int nextUserId;
    public NextTurnEvent(int nextUserId) {
        super(0);
        this.nextUserId = nextUserId;
    }

    public int getNextUserId() {
        return nextUserId;
    }

    @Override
    public EventType getEventType() {
        return PlayingEventType.NEXT_TURN;
    }

    @Override
    public String getName() {
        return null;
    }
}
