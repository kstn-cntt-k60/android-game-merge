package kstn.game.logic.state.multiplayer;

import android.util.Log;

import kstn.game.logic.data.ManagerDAO;
import kstn.game.logic.data.QuestionModel;
import kstn.game.logic.event.EventData;
import kstn.game.logic.event.EventListener;
import kstn.game.logic.event.EventManager;
import kstn.game.logic.playing_event.NextQuestionEvent;
import kstn.game.logic.playing_event.PlayingEventType;
import kstn.game.logic.state.IEntryExit;

public class QuestionManager implements IEntryExit {
    private final EventManager eventManager;
    private final ManagerDAO managerDAO;

    String question;
    String answer;

    public String getQuestion() {
        return question;
    }

    public String getAnswer() {
        return answer;
    }

    private EventListener nextQuestionListener;

    public QuestionManager(EventManager eventManager, ManagerDAO managerDAO) {
        this.eventManager = eventManager;
        this.managerDAO = managerDAO;

        nextQuestionListener = new EventListener() {
            @Override
            public void onEvent(EventData event) {
                NextQuestionEvent event1 = (NextQuestionEvent) event;
                question = event1.getQuestion();
                answer = event1.getAnswer();
            }
        };
    }

    @Override
    public void entry() {
        eventManager.addListener(PlayingEventType.NEXT_QUESTION, nextQuestionListener);
    }

    @Override
    public void exit() {
        eventManager.removeListener(PlayingEventType.NEXT_QUESTION, nextQuestionListener);
    }

    public void nextQuestion() {
        Log.i("QuestionManager", "Next Question");
        QuestionModel questionModel = managerDAO.getRandomQuestion();
        eventManager.trigger(new NextQuestionEvent(
                questionModel.getQuestion(), questionModel.getAnswer()));
    }

    public boolean sameAsAnswer(String answer) {
        String tmp = answer.toUpperCase();
        String tmp2 = this.answer.toUpperCase();
        return tmp.equals(tmp2);
    }

}
