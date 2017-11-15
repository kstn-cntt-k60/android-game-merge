package kstn.game.logic.playing_event;

import kstn.game.logic.event.BaseEventData;
import kstn.game.logic.event.EventType;

public class NextQuestionEvent extends BaseEventData {
    private final String question;
    private final String answer;

    public NextQuestionEvent(String question, String answer) {
        super(0);
        this.question = question;
        this.answer = answer;
    }

    @Override
    public EventType getEventType() {
        return PlayingEventType.NEXT_QUESTION;
    }

    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }

    @Override
    public String getName() {
        return null;
    }
}
