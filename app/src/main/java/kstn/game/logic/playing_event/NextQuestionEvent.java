package kstn.game.logic.playing_event;

import kstn.game.logic.event.BaseEventData;
import kstn.game.logic.event.EventType;

/**
 * Created by qi on 14/11/2017.
 */

public class NextQuestionEvent extends BaseEventData {
    private final int idQuestion;

    @Override
    public EventType getEventType() {
        return PlayingEventType.NEXT_QUESTION;
    }

    public int getIdQuestion() {
        return idQuestion;
    }

    public NextQuestionEvent(int idQuestion) {

        super(0);
        this.idQuestion = idQuestion;
    }

    @Override
    public String getName() {
        return null;
    }
}
