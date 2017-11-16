package kstn.game.logic.playing_event;

import kstn.game.logic.event.BaseEventData;
import kstn.game.logic.event.EventType;

public class BonusEvent extends BaseEventData {
    private final int bonus;

    public BonusEvent(int bonus) {
        super(0);
        this.bonus = bonus;
    }

    @Override
    public EventType getEventType() {
        return PlayingEventType.BONUS;
    }

    public int getBonus() {
        return bonus;
    }

    @Override
    public String getName() {
        return null;
    }
}
