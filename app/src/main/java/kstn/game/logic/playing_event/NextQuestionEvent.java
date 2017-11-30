package kstn.game.logic.playing_event;

import kstn.game.logic.event.GameEventData;

public class NextQuestionEvent extends GameEventData {
    private final String question;
    private final String answer;

    public NextQuestionEvent(String question, String answer) {
        super(PlayingEventType.NEXT_QUESTION);
        this.question = question;
        this.answer = answer;
    }
    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }
}
