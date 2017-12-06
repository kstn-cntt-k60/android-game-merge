package kstn.game.logic.state.multiplayer;

import junit.framework.Assert;

import org.junit.Test;

import kstn.game.logic.event.EventListener;
import kstn.game.logic.event.EventManager;
import kstn.game.logic.playing_event.NextQuestionEvent;
import kstn.game.logic.playing_event.PlayingEventType;

import static kstn.game.logic.event.EventUtil.*;

public class CellManagerTest {
    CellManager createCellManager(EventManager eventManager) {
        return new CellManager(eventManager);
    }

    private String question = "ACC";
    private String answer = "xa Bc";
    private String nonSpaceAnswer = "XABC";

    @Test
    public void setNewIsOpenedCells_WithFalse_OnNextQuestionEvent() {
        EventManager eventManager = getEventManager();
        CellManager manager = createCellManager(eventManager);
        manager.entry();

        boolean isOpenedCells[] = new boolean[100];
        manager.isOpenedCells = isOpenedCells;

        eventManager.trigger(new NextQuestionEvent(question, answer));

        Assert.assertNotSame(manager.isOpenedCells, isOpenedCells);
        Assert.assertEquals(manager.isOpenedCells.length, 4);

        for (int i = 0; i < 4; i++) {
            Assert.assertEquals(manager.isOpenedCells[i], false);
        }
    }

    @Test
    public void storeAnswer_nonSpaceAnswer_OnNextQuestionEvent() {
        EventManager eventManager = getMockedEventManager();
        CellManager manager = createCellManager(eventManager);
        EventListener listener = assertSetUpListener(
                eventManager, PlayingEventType.NEXT_QUESTION, manager);

        listener.onEvent(new NextQuestionEvent(question, answer));
        Assert.assertEquals(manager.getAnswer(), answer);
        Assert.assertEquals(manager.getNonSpaceAnswer(), nonSpaceAnswer);
    }
}
