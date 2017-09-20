package kstn.game.app.db;

import kstn.game.logic.db.Question;

public class BaseQuestion implements Question {
    private final String question;
    private final String answer;

    public BaseQuestion(String question, String answer) {
        this.question = question;
        this.answer = answer;
    }

    @Override
    public String getQuestion() {
        return question;
    }

    @Override
    public String getAnswer() {
        return answer;
    }
}
