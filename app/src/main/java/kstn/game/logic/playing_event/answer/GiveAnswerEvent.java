package kstn.game.logic.playing_event.answer;

import kstn.game.logic.event.GameEventData;
import kstn.game.logic.playing_event.PlayingEventType;

public class GiveAnswerEvent extends GameEventData {
    public GiveAnswerEvent() {
        super(PlayingEventType.GIVE_ANSWER);
    }
}
