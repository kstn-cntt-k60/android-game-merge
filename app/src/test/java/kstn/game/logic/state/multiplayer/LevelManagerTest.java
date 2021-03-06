package kstn.game.logic.state.multiplayer;

import junit.framework.Assert;

import org.junit.Test;

import kstn.game.logic.event.EventManager;
import kstn.game.logic.playing_event.NextQuestionEvent;
import kstn.game.logic.playing_event.PlayingEventType;

import static kstn.game.logic.event.EventUtil.*;
import static org.mockito.Mockito.mock;

public class LevelManagerTest {
    private ScorePlayerManager getMockedScoreManager()  {
        return mock(ScorePlayerManager.class);
    }

    private QuestionManager getMockedQuestionManager() {
        return mock(QuestionManager.class);
    }

    private LevelManager createManager(EventManager eventManager) {
        return new LevelManager(
                eventManager,
                getMockedQuestionManager(),
                getMockedScoreManager()
        );
    }

    @Test
    public void listenTo_NextQuestionEvent() {
        EventManager eventManager = getMockedEventManager();
        LevelManager manager = createManager(eventManager);
        assertSetUpListener(eventManager, PlayingEventType.NEXT_QUESTION, manager);
    }

    @Test
    public void entry_setLevel_toZero() {
        EventManager eventManager = getMockedEventManager();
        LevelManager manager = createManager(eventManager);
        manager.level = 3;
        manager.entry();
        Assert.assertEquals(manager.level, 0);
    }

    @Test
    public void onNextQuestionEvent_IncreaseLevel() {
        EventManager eventManager = getEventManager();
        LevelManager manager = createManager(eventManager);
        manager.entry();
        manager.level = 4;
        eventManager.trigger(new NextQuestionEvent("", ""));
        Assert.assertEquals(manager.level, 5);
    }
}
