package kstn.game.logic.state.multiplayer;

import kstn.game.logic.event.EventData;
import kstn.game.logic.event.EventListener;
import kstn.game.logic.event.EventManager;
import kstn.game.logic.playing_event.NextQuestionEvent;
import kstn.game.logic.playing_event.PlayingEventType;
import kstn.game.logic.playing_event.cell.OpenCellEvent;
import kstn.game.logic.playing_event.cell.OpenMultipleCellEvent;
import kstn.game.logic.state.IEntryExit;

public class CellManager implements IEntryExit {
    private final EventManager eventManager;

    private String answer;
    String nonSpaceAnswer;

    String getAnswer() { return answer; }
    String getNonSpaceAnswer() { return nonSpaceAnswer; }

    boolean[] isOpenedCells;

    private final EventListener nextQuestionListener;
    private final EventListener openCellListener;
    private final EventListener openMultipleCellListener;

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

        openCellListener = new EventListener() {
            @Override
            public void onEvent(EventData event) {
                OpenCellEvent event1 = (OpenCellEvent) event;
                isOpenedCells[event1.getIndex()] = true;
            }
        };

        openMultipleCellListener = new EventListener() {
            @Override
            public void onEvent(EventData event) {
                char ch = ((OpenMultipleCellEvent) event).getCharacter();
                for (int i = 0; i < nonSpaceAnswer.length(); i++) {
                    if (nonSpaceAnswer.charAt(i) == ch)
                        isOpenedCells[i] = true;
                }
            }
        };
    }

    @Override
    public void entry() {
        eventManager.addListener(PlayingEventType.NEXT_QUESTION, nextQuestionListener);
        eventManager.addListener(PlayingEventType.OPEN_CELL, openCellListener);
        eventManager.addListener(PlayingEventType.OPEN_MULTIPLE_CELL, openMultipleCellListener);
    }

    @Override
    public void exit() {
        eventManager.removeListener(PlayingEventType.OPEN_MULTIPLE_CELL, openMultipleCellListener);
        eventManager.removeListener(PlayingEventType.OPEN_CELL, openCellListener);
        eventManager.removeListener(PlayingEventType.NEXT_QUESTION, nextQuestionListener);
    }

    public void openCell(int cellIndex) {
        eventManager.trigger(new OpenCellEvent(cellIndex));
    }

    public int openMultipleCells(char ch) {
        int count = 0;
        for (int i = 0; i < nonSpaceAnswer.length(); i++) {
            if (nonSpaceAnswer.charAt(i) == ch)
                count++;
        }
        eventManager.trigger(new OpenMultipleCellEvent(ch));
        return count;
    }

    public boolean allCellsAreOpened() {
        if (nonSpaceAnswer == null)
            return false;

        for (int i = 0; i < nonSpaceAnswer.length(); i++) {
            if (!isOpenedCells[i])
                return false;
        }
        return true;
    }
}
