package kstn.game.logic.state.multiplayer;

import junit.framework.Assert;

import org.junit.Test;

import kstn.game.logic.event.EventData;
import kstn.game.logic.event.EventListener;
import kstn.game.logic.event.EventManager;
import kstn.game.logic.playing_event.NextQuestionEvent;
import kstn.game.logic.playing_event.PlayingEventType;
import kstn.game.logic.playing_event.cell.OpenCellEvent;
import kstn.game.logic.playing_event.cell.OpenMultipleCellEvent;

import static kstn.game.logic.event.EventUtil.*;

public class CellManagerTest {
    private CellManager createCellManager(EventManager eventManager) {
        return new CellManager(eventManager);
    }

    private CellManager createCellManager() {
        return createCellManager(getMockedEventManager());
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

    private void assertOpenCellEquals(EventData a, EventData b) {
        OpenCellEvent event = (OpenCellEvent) a;
        OpenCellEvent other = (OpenCellEvent) b;
        Assert.assertEquals(event.getIndex(), other.getIndex());
    }

    @Test
    public void openCell_SendOpenCellEvent() {
        EventManager eventManager = getEventManager();
        CellManager manager = createCellManager(eventManager);
        EventListener listener = getMockedListener(eventManager,
                PlayingEventType.OPEN_CELL);
        manager.entry();
        manager.isOpenedCells = new boolean[10];

        final int index = 4;
        manager.openCell(index);
        EventData event = assertTriggeredReturn(listener);
        assertOpenCellEquals(event, new OpenCellEvent(index));
    }

    @Test
    public void listenTo_OpenCellEvent_AndChangeIsOpenedCells() {
        EventManager eventManager = getMockedEventManager();
        CellManager manager = createCellManager(eventManager);
        EventListener listener = assertSetUpListener(
                eventManager, PlayingEventType.OPEN_CELL, manager);

        manager.isOpenedCells = new boolean[10];
        Assert.assertFalse(manager.isOpenedCells[2]);
        listener.onEvent(new OpenCellEvent(2));
        Assert.assertTrue(manager.isOpenedCells[2]);
    }

    private void assertOpenMultipleCellEventEquals(EventData a, EventData b) {
        OpenMultipleCellEvent event = (OpenMultipleCellEvent) a;
        OpenMultipleCellEvent other = (OpenMultipleCellEvent) b;
        Assert.assertEquals(event.getCharacter(), other.getCharacter());
    }

    @Test
    public void openMultipleCells_SendOpenMultipleCellEvent() {
        EventManager eventManager = getEventManager();
        CellManager manager = createCellManager(eventManager);
        EventListener listener = getMockedListener(eventManager,
                PlayingEventType.OPEN_MULTIPLE_CELL);
        manager.entry();
        manager.isOpenedCells = new boolean[10];
        manager.nonSpaceAnswer = "ADEXXU";

        manager.openMultipleCells('X');
        EventData event = assertTriggeredReturn(listener);
        assertOpenMultipleCellEventEquals(event, new OpenMultipleCellEvent('X'));
    }

    @Test
    public void listenTo_OpenMultipleCellEvent_AndChangeIsOpenedCells() {
        EventManager eventManager = getMockedEventManager();
        CellManager manager = createCellManager(eventManager);
        EventListener listener = assertSetUpListener(
                eventManager, PlayingEventType.OPEN_MULTIPLE_CELL, manager);

        manager.nonSpaceAnswer = "ADEXXU";
        manager.isOpenedCells = new boolean[10];

        Assert.assertFalse(manager.isOpenedCells[0]);
        Assert.assertFalse(manager.isOpenedCells[3]);
        Assert.assertFalse(manager.isOpenedCells[4]);
        listener.onEvent(new OpenMultipleCellEvent('X'));
        Assert.assertFalse(manager.isOpenedCells[0]);
        Assert.assertTrue(manager.isOpenedCells[3]);
        Assert.assertTrue(manager.isOpenedCells[4]);
    }

    @Test
    public void openMultipleCells_ReturnNumberOfCellsJustOpened() {
        CellManager cellManager = createCellManager();
        cellManager.isOpenedCells = new boolean[20];
        cellManager.isOpenedCells[3] = true;
        cellManager.nonSpaceAnswer = "ADEXXUUX";
        int count = cellManager.openMultipleCells('X');
        Assert.assertEquals(count, 2);
    }

    @Test
    public void allCellsAreOpened_ReturnTrue() {
        CellManager manager = createCellManager();
        manager.nonSpaceAnswer = "XAZ";
        manager.isOpenedCells = new boolean[5];

        manager.isOpenedCells[0] = true;
        manager.isOpenedCells[1] = true;
        manager.isOpenedCells[2] = true;
        manager.isOpenedCells[3] = false;
        Assert.assertTrue(manager.allCellsAreOpened());
    }

    @Test
    public void allCellsAreOpened_ReturnFalse() {
        CellManager manager = createCellManager();
        manager.nonSpaceAnswer = "XAZ";
        manager.isOpenedCells = new boolean[5];

        manager.isOpenedCells[0] = true;
        manager.isOpenedCells[1] = false;
        manager.isOpenedCells[2] = true;
        manager.isOpenedCells[3] = false;
        Assert.assertFalse(manager.allCellsAreOpened());
    }
}
