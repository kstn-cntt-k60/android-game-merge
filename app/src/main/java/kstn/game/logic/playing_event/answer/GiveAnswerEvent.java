package kstn.game.logic.playing_event.answer;

import kstn.game.logic.event.BaseEventData;
import kstn.game.logic.event.EventType;
import kstn.game.logic.playing_event.PlayingEventType;

public class GiveAnswerEvent extends BaseEventData {
    public GiveAnswerEvent() {
        super(0);
    }

    @Override
    public EventType getEventType() {
        return PlayingEventType.GIVE_ANSWER;
    }

    @Override
    public String getName() {
        return null;
    }
}
