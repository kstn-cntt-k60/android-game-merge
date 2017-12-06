package kstn.game.logic.state.multiplayer;

import kstn.game.logic.event.EventData;
import kstn.game.logic.event.EventListener;
import kstn.game.logic.event.EventManager;
import kstn.game.logic.playing_event.NextQuestionEvent;
import kstn.game.logic.playing_event.PlayingEventType;
import kstn.game.logic.state.IEntryExit;

public class CellManager implements IEntryExit {
    private final EventManager eventManager;

    private String answer;
    private String nonSpaceAnswer;

    String getAnswer() { return answer; }
    String getNonSpaceAnswer() { return nonSpaceAnswer; }

    boolean[] isOpenedCells;

    private final EventListener nextQuestionListener;

    CellManager(EventManager eventManager) {
        this.eventManager = eventManager;

        nextQuestionListener = new EventListener() {
            @Override
            public void onEvent(EventData event) {
                NextQuestionEvent event1 = (NextQuestionEvent) event;
                answer = event1.getAnswer();
                String tmp = answer.replaceAll("\\s+", "");
                nonSpaceAnswer = tmp.toUpperCase();

                isOpenedCells = new boolean[nonSpaceAnswer.length()];
                for (int i = 0; i < isOpenedCells.length; i++)
                    isOpenedCells[i] = false;
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

    public void openCell(int cellIndex) {

    }

    public void openMultipleCells(char ch) {

    }

    public boolean allCellsAreOpened() {
        return false;
    }
}
