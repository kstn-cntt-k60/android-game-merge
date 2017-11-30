package kstn.game.logic.playing_event;

import kstn.game.logic.event.GameEventData;

public class BonusEvent extends GameEventData {
    private final int bonus;

    public BonusEvent(int bonus) {
        super(PlayingEventType.BONUS);
        this.bonus = bonus;
    }

    public int getBonus() {
        return bonus;
    }
}
