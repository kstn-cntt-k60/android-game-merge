package kstn.game.logic.playing_event;

import kstn.game.logic.event.BaseEventData;
import kstn.game.logic.event.EventType;

/**
 * Created by tung on 14/11/2017.
 */

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
