package kstn.game.logic.state.multiplayer;

import junit.framework.Assert;

import org.junit.Test;

import kstn.game.logic.data.ManagerDAO;
import kstn.game.logic.data.QuestionModel;
import kstn.game.logic.event.EventListener;
import kstn.game.logic.event.EventManager;
import kstn.game.logic.playing_event.NextQuestionEvent;
import kstn.game.logic.playing_event.PlayingEventType;

import static kstn.game.logic.event.EventUtil.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class QuestionManagerTest {
    private final String question = "ABCC";
    private final String answer = "XXX";

    private ManagerDAO getMockedManagerDAO() {
        return mock(ManagerDAO.class);
    }

    private QuestionManager createQuestionManager(EventManager eventManager,
                                          ManagerDAO managerDAO) {
        return new QuestionManager(eventManager, managerDAO);
    }

    private QuestionManager createQuestionManager(EventManager eventManager) {
        return createQuestionManager(eventManager, getMockedManagerDAO());
    }

    private QuestionManager createQuestionManager() {
        return createQuestionManager(getMockedEventManager());
    }

    @Test
    public void nextQuestion_SendNextQuestionEvent_DataFromManagerDAO() {
        EventManager eventManager = getEventManager();
        ManagerDAO managerDAO = getMockedManagerDAO();
        QuestionManager questionManager = createQuestionManager(eventManager, managerDAO);

        EventListener listener = getMockedListener(eventManager, PlayingEventType.NEXT_QUESTION);

        when(managerDAO.getRandomQuestion()).thenReturn(new QuestionModel(question, answer));
        questionManager.nextQuestion();
        update(eventManager);

        NextQuestionEvent event = (NextQuestionEvent) assertTriggeredReturn(listener);
        Assert.assertEquals(event.getQuestion(), question);
        Assert.assertEquals(event.getAnswer(), answer);
    }

    @Test
    public void listenToNextQuestionEvent() {
        EventManager eventManager = getMockedEventManager();
        QuestionManager manager = createQuestionManager(eventManager);
        assertSetUpListener(eventManager, PlayingEventType.NEXT_QUESTION, manager);
    }

    @Test
    public void onNextQuestionEvent_SetItsOwn_Question_Answer() {
        EventManager eventManager = getEventManager();
        QuestionManager questionManager = createQuestionManager(eventManager);
        questionManager.entry();
        Assert.assertNull(questionManager.getQuestion());
        Assert.assertNull(questionManager.getAnswer());

        eventManager.trigger(new NextQuestionEvent(question, answer));

        Assert.assertEquals(questionManager.getQuestion(), question);
        Assert.assertEquals(questionManager.getAnswer(), answer);
    }

    @Test
    public void sameAsAnswer_WhenTrue() {
        QuestionManager questionManager = createQuestionManager();
        questionManager.answer = "XX aa uu";
        Assert.assertTrue(questionManager.sameAsAnswer("XX Aa uU"));
    }

    @Test
    public void sameAsAnswer_WhenFalse() {
        QuestionManager questionManager = createQuestionManager();
        questionManager.answer = "XX xa uu";
        Assert.assertFalse(questionManager.sameAsAnswer("XX Aa uU"));
    }

}
