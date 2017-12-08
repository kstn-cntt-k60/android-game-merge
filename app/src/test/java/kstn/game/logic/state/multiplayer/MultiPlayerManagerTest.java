package kstn.game.logic.state.multiplayer;

import junit.framework.Assert;

import org.junit.Test;

import kstn.game.logic.cone.ConeAccelerateEventData;
import kstn.game.logic.cone.ConeEventType;
import kstn.game.logic.cone.ConeStopEventData;
import kstn.game.logic.event.EventData;
import kstn.game.logic.event.EventListener;
import kstn.game.logic.event.EventManager;
import kstn.game.logic.network.WifiInfo;
import kstn.game.logic.playing_event.PlayingEventType;
import kstn.game.logic.playing_event.answer.AnswerEvent;
import kstn.game.logic.playing_event.cell.CellChosenEvent;
import kstn.game.logic.playing_event.guess.CancelGuessEvent;
import kstn.game.logic.playing_event.guess.GuessResultEvent;
import kstn.game.logic.playing_event.guess.RequestGuessEvent;
import kstn.game.logic.playing_event.player.NextPlayerEvent;
import kstn.game.logic.playing_event.player.PlayerReadyEvent;
import kstn.game.logic.state.EntryExitUtil;
import kstn.game.logic.state.multiplayer.ministate.State;

import static org.mockito.Mockito.mock;
import static kstn.game.logic.event.EventUtil.*;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static kstn.game.logic.state.multiplayer.ministate.StateUtil.*;

public class MultiPlayerManagerTest {
    private ScorePlayerManager getMockedScoreManager() {
        return mock(ScorePlayerManager.class);
    }

    private QuestionManager getMockedQuestionManager() { return mock(QuestionManager.class); }

    private CellManager getMockedCellManager() { return mock(CellManager.class); }

    private LevelManager getMockedLevelManager() { return mock(LevelManager.class); }

    private WifiInfo getMockedWifiInfo() {
        return mock(WifiInfo.class);
    }

    private MultiPlayerManager createManager(EventManager eventManager,
                                             ScorePlayerManager scoreManager,
                                             QuestionManager questionManager,
                                             CellManager cellManager,
                                             LevelManager levelManager,
                                             WifiInfo wifiInfo) {
        MultiPlayerManager manager =  new MultiPlayerManager(
                eventManager, scoreManager,
                questionManager, cellManager,
                levelManager, wifiInfo
        );
        manager.setWaitOtherPlayersState(getMockedState());
        manager.setRotatableState(getMockedState());
        return manager;
    }

    private MultiPlayerManager createManager(LevelManager levelManager) {
        return createManager(
                getMockedEventManager(),
                getMockedScoreManager(),
                getMockedQuestionManager(),
                getMockedCellManager(),
                levelManager,
                getMockedWifiInfo()
        );
    }

    private MultiPlayerManager createManager(EventManager eventManager,
                                             ScorePlayerManager scoreManager,
                                             QuestionManager questionManager,
                                             CellManager cellManager,
                                             WifiInfo wifiInfo) {
        return createManager(
                eventManager, scoreManager,
                questionManager, cellManager,
                getMockedLevelManager(), wifiInfo
        );
    }

    private MultiPlayerManager createManager(CellManager cellManager) {
        return createManager(
                getMockedEventManager(),
                getMockedScoreManager(),
                getMockedQuestionManager(),
                cellManager,
                getMockedWifiInfo()
        );
    }

    private MultiPlayerManager createManager(EventManager eventManager,
                                             ScorePlayerManager scoreManager,
                                             QuestionManager questionManager,
                                             WifiInfo wifiInfo) {
        return createManager(
                eventManager, scoreManager, questionManager,
                getMockedCellManager(), wifiInfo
        );
    }

    private MultiPlayerManager createManager(EventManager eventManager,
                                             ScorePlayerManager scoreManager,
                                             QuestionManager questionManager) {
        WifiInfo wifiInfo = mock(WifiInfo.class);
        return createManager(eventManager, scoreManager, questionManager, wifiInfo);
    }

    private MultiPlayerManager createManager(QuestionManager questionManager) {
        return createManager(getMockedEventManager(),
                getMockedScoreManager(), questionManager);
    }

    private MultiPlayerManager createManager(EventManager eventManager,
                                             ScorePlayerManager scoreManager,
                                             WifiInfo wifiInfo) {
        QuestionManager questionManager = mock(QuestionManager.class);
        return createManager(eventManager, scoreManager, questionManager, wifiInfo);
    }

    private MultiPlayerManager createManager(EventManager eventManager,
                                             ScorePlayerManager scoreManager) {
        QuestionManager questionManager = mock(QuestionManager.class);
        return createManager(eventManager, scoreManager, questionManager);
    }

    private MultiPlayerManager createManager(EventManager eventManager) {
        ScorePlayerManager scorePlayerManager = mock(ScorePlayerManager.class);
        return createManager(eventManager, scorePlayerManager);
    }

    private MultiPlayerManager createManager(ScorePlayerManager scoreManager) {
        EventManager eventManager = getMockedEventManager();
        return createManager(eventManager, scoreManager);
    }

    private MultiPlayerManager createManager() {
        EventManager eventManager = getMockedEventManager();
        ScorePlayerManager scoreManager = mock(ScorePlayerManager.class);
        return createManager(eventManager, scoreManager);
    }

    @Test
    public void callConeAccel_CurrenState_OnEvent() {
        EventManager eventManager = getMockedEventManager();
        MultiPlayerManager manager = createManager(eventManager);
        EventListener listener
                = assertSetUpListener(eventManager, ConeEventType.ACCELERATE, manager);

        State mockedState = getMockedState();
        manager.currentState = mockedState;
        listener.onEvent(new ConeAccelerateEventData(30, 40));
        verify(mockedState).coneAccel(30, 40);
    }

    @Test
    public void callConeStop_CurrentState_OnEvent() {
        EventManager eventManager = getMockedEventManager();
        MultiPlayerManager manager = createManager(eventManager);
        EventListener listener
                = assertSetUpListener(eventManager, ConeEventType.STOP, manager);

        State mockedState = getMockedState();
        manager.currentState = mockedState;
        listener.onEvent(new ConeStopEventData(40));
        verify(mockedState).coneStop(40);
    }

    @Test
    public void callChosenCell_CurrentState_OnEvent() {
        EventManager eventManager = getMockedEventManager();
        MultiPlayerManager manager = createManager(eventManager);
        EventListener listener = assertSetUpListener(
                eventManager, PlayingEventType.CELL_CHOSEN, manager);

        State mockedState = getMockedState();
        manager.currentState = mockedState;
        listener.onEvent(new CellChosenEvent(100));
        verify(mockedState).chooseCell(100);
    }

    @Test
    public void callAnswer_CurrentState_OnEvent() {
        EventManager eventManager = getMockedEventManager();
        MultiPlayerManager manager = createManager(eventManager);
        EventListener listener = assertSetUpListener(
                eventManager, PlayingEventType.ANSWER, manager);

        State mockedState = getMockedState();
        manager.currentState = mockedState;
        listener.onEvent(new AnswerEvent('X'));
        verify(mockedState).answer('X');
    }

    @Test
    public void callRequestGuess_CurrentState_OnEvent() {
        EventManager eventManager = getMockedEventManager();
        MultiPlayerManager manager = createManager(eventManager);
        State state = getMockedState();
        EventListener listener = assertSetUpListener(
                eventManager, PlayingEventType.REQUEST_GUESS, manager
        );

        manager.currentState = state;
        listener.onEvent(new RequestGuessEvent());
        verify(state).requestGuess();
    }

    @Test
    public void callGuessResult_CurrentState_OnEvent() {
        EventManager eventManager = getMockedEventManager();
        MultiPlayerManager manager = createManager(eventManager);
        State state = getMockedState();
        EventListener listener = assertSetUpListener(
                eventManager, PlayingEventType.GUESS_RESULT, manager
        );

        manager.currentState = state;
        listener.onEvent(new GuessResultEvent("ABC"));
        verify(state).guessResult("ABC");
    }

    @Test
    public void callCancelGuess_CurrentState_OnEvent() {
        EventManager eventManager = getMockedEventManager();
        MultiPlayerManager manager = createManager(eventManager);
        State state = getMockedState();
        EventListener listener = assertSetUpListener(
                eventManager, PlayingEventType.CANCEL_GUESS, manager
        );

        manager.currentState = state;
        listener.onEvent(new CancelGuessEvent());
        verify(state).cancelGuess();
    }

    @Test
    public void callNextPlayer_CurrentState_OnEvent() {
        EventManager eventManager = getMockedEventManager();
        MultiPlayerManager manager = createManager(eventManager);
        State state = getMockedState();
        EventListener listener = assertSetUpListener(
                eventManager, PlayingEventType.NEXT_PLAYER, manager
        );

        manager.currentState = state;
        listener.onEvent(new NextPlayerEvent(2));
        verify(state).nextPlayer(2);
    }

    @Test
    public void entry_Call_ScoreManager_LoadInfo_AndEntry() {
        ScorePlayerManager scoreManager = mock(ScorePlayerManager.class);
        MultiPlayerManager manager = createManager(scoreManager);
        manager.entry();
        verify(scoreManager).loadInfoFromThisRoom();
        verify(scoreManager).entry();
    }

    @Test
    public void entry_Set_viewIsReady_False() {
        MultiPlayerManager manager = createManager();
        manager.viewIsReady = true;
        manager.entry();
        Assert.assertFalse(manager.viewIsReady);
    }

    @Test
    public void entry_Set_DefaultState_IsWaitOtherPlayers() {
        State waitOtherPlayersState = getMockedState();
        MultiPlayerManager manager = createManager();

        Assert.assertNull(manager.currentState);
        manager.setWaitOtherPlayersState(waitOtherPlayersState);
        manager.entry();

        Assert.assertSame(manager.currentState, waitOtherPlayersState);
    }

    @Test
    public void onViewReady_WhenComplete_Set_viewIsReady_True() {
        MultiPlayerManager manager = createManager();
        manager.viewIsReady = false;
        manager.onViewReady();
        Assert.assertTrue(manager.viewIsReady);
    }

    @Test
    public void onViewReady_Call_ScoreManager_onViewReady() {
        ScorePlayerManager scoreManager = mock(ScorePlayerManager.class);
        MultiPlayerManager manager = createManager(scoreManager);
        manager.onViewReady();
        verify(scoreManager).onViewReady();
    }

    @Test
    public void listenTo_PlayerReadyEvent_WhenIsHost() {
        EventManager eventManager = getMockedEventManager();
        ScorePlayerManager scoreManager = getMockedScoreManager();
        when(scoreManager.thisPlayerIsHost()).thenReturn(true);

        MultiPlayerManager manager = createManager(eventManager, scoreManager);
        assertSetUpListener(eventManager, PlayingEventType.PLAYER_READY, manager);
    }

    @Test
    public void call_ScoreManager_playerReady_OnPlayerReadyEvent() {
        EventManager eventManager = getEventManager();
        ScorePlayerManager scoreManager = getMockedScoreManager();
        when(scoreManager.thisPlayerIsHost()).thenReturn(true);

        MultiPlayerManager manager = createManager(eventManager, scoreManager);
        manager.entry();
        eventManager.trigger(new PlayerReadyEvent(2211));
        verify(scoreManager).playerReady(2211);
    }

    @Test
    public void nextQuestion_InListener_WhenViewIsReady_AllReady_AndIsHost() {
        EventManager eventManager = getEventManager();
        ScorePlayerManager scoreManager = getMockedScoreManager();
        QuestionManager questionManager = getMockedQuestionManager();

        when(scoreManager.thisPlayerIsHost()).thenReturn(true);
        when(scoreManager.areAllPlayersReady()).thenReturn(true);

        MultiPlayerManager manager = createManager(
                eventManager, scoreManager, questionManager);
        manager.entry();

        manager.viewIsReady = true;

        eventManager.trigger(new PlayerReadyEvent(2233));
        verify(questionManager).nextQuestion();
    }

    @Test
    public void TransitRotatableState_InListener_WhenViewIsReady_AllReady_AndIsHost() {
        EventManager eventManager = getEventManager();
        ScorePlayerManager scoreManager = getMockedScoreManager();
        State rotatableState = getMockedState();
        State waitOtherPlayersState = getMockedState();

        when(scoreManager.thisPlayerIsHost()).thenReturn(true);
        when(scoreManager.areAllPlayersReady()).thenReturn(true);

        MultiPlayerManager manager = createManager(eventManager, scoreManager);
        manager.setWaitOtherPlayersState(waitOtherPlayersState);
        manager.setRotatableState(rotatableState);

        verify(waitOtherPlayersState, never()).entry();
        manager.entry();
        verify(waitOtherPlayersState).entry();

        manager.viewIsReady = true;

        verify(rotatableState, never()).entry();
        eventManager.trigger(new PlayerReadyEvent(2233));
        verify(waitOtherPlayersState).exit();
        Assert.assertSame(manager.currentState, rotatableState);
        verify(rotatableState).entry();
    }

    @Test
    public void DoNothing_InPLayerReadyListener_WhenViewIsReady_AllNotYetReady_AndIsHost() {
        EventManager eventManager = getEventManager();
        ScorePlayerManager scoreManager = getMockedScoreManager();
        State rotatableState = getMockedState();
        State waitOtherPlayersState = getMockedState();

        when(scoreManager.thisPlayerIsHost()).thenReturn(true);
        when(scoreManager.areAllPlayersReady()).thenReturn(false);

        MultiPlayerManager manager = createManager(eventManager, scoreManager);
        manager.setWaitOtherPlayersState(waitOtherPlayersState);
        manager.setRotatableState(rotatableState);
        manager.entry();

        manager.viewIsReady = true;

        eventManager.trigger(new PlayerReadyEvent(2233));
        Assert.assertSame(manager.currentState, waitOtherPlayersState);
        Assert.assertNotSame(manager.currentState, rotatableState);
    }

    @Test
    public void notListenTo_PlayerReadyEvent_WhenIsClient() {
        EventManager eventManager = getMockedEventManager();
        ScorePlayerManager scoreManager = getMockedScoreManager();
        when(scoreManager.thisPlayerIsHost()).thenReturn(false);

        MultiPlayerManager manager = createManager(eventManager, scoreManager);
        assertNotSetUpListener(eventManager, PlayingEventType.PLAYER_READY, manager);
    }

    @Test
    public void onViewReady_nextQuestion_AndTransitRotatableState_WhenAllPlayerReady_AndIsHost() {
        EventManager eventManager = getEventManager();
        ScorePlayerManager scoreManager = getMockedScoreManager();
        QuestionManager questionManager = getMockedQuestionManager();
        State rotatableState = getMockedState();

        when(scoreManager.thisPlayerIsHost()).thenReturn(true);
        when(scoreManager.areAllPlayersReady()).thenReturn(true);

        MultiPlayerManager manager = createManager(
                eventManager, scoreManager, questionManager);
        manager.setRotatableState(rotatableState);
        manager.entry();
        manager.onViewReady();

        verify(questionManager).nextQuestion();
        Assert.assertSame(manager.currentState, rotatableState);
    }

    @Test
    public void onViewReady_NotNextQuestion_WhenAllPlayerReady_AndIsClient() {
        EventManager eventManager = getEventManager();
        ScorePlayerManager scoreManager = getMockedScoreManager();
        QuestionManager questionManager = getMockedQuestionManager();
        State rotatableState = getMockedState();

        when(scoreManager.thisPlayerIsHost()).thenReturn(false);
        when(scoreManager.areAllPlayersReady()).thenReturn(true);

        MultiPlayerManager manager = createManager(
                eventManager, scoreManager, questionManager);
        manager.setRotatableState(rotatableState);
        manager.entry();
        manager.onViewReady();

        verify(questionManager, never()).nextQuestion();
        Assert.assertNotSame(manager.currentState, rotatableState);
    }

    private void assertPlayerReadyEventEquals(EventData a, EventData b) {
        PlayerReadyEvent event = (PlayerReadyEvent) a;
        PlayerReadyEvent other = (PlayerReadyEvent) b;
        Assert.assertEquals(event.getIpAddress(), other.getIpAddress());
    }

    @Test
    public void onViewReady_Send_PlayerReadyEvent_WhenIsClient() {
        final int ip = 44556;
        EventManager eventManager = getEventManager();
        ScorePlayerManager scorePlayerManager = getMockedScoreManager();
        WifiInfo wifiInfo = getMockedWifiInfo();
        when(wifiInfo.getIP()).thenReturn(ip);

        when(scorePlayerManager.thisPlayerIsHost()).thenReturn(false);

        EventListener playerReadyListener = getMockedListener(
                eventManager, PlayingEventType.PLAYER_READY);

        MultiPlayerManager manager = createManager(
                eventManager, scorePlayerManager, wifiInfo);
        manager.onViewReady();

        EventData event = assertTriggeredReturn(playerReadyListener);
        assertPlayerReadyEventEquals(event, new PlayerReadyEvent(ip));
    }

    @Test
    public void entry_NotSend_PlayerReadyEvent_WhenIsHost() {
        EventManager eventManager = getEventManager();
        ScorePlayerManager scorePlayerManager = getMockedScoreManager();

        when(scorePlayerManager.thisPlayerIsHost()).thenReturn(true);

        EventListener playerReadyListener = getMockedListener(
                eventManager, PlayingEventType.PLAYER_READY);

        MultiPlayerManager manager = createManager(
                eventManager, scorePlayerManager);
        manager.entry();

        assertNotTriggered(playerReadyListener);
    }

    @Test
    public void makeTransitionTo_Call_Exit_Entry() {
        State state1 = getMockedState();
        State state2 = getMockedState();
        MultiPlayerManager manager = createManager();
        manager.currentState = state1;
        manager.makeTransitionTo(state2);
        Assert.assertSame(manager.currentState, state2);

        verify(state1).exit();
        verify(state2).entry();
    }

    @Test
    public void setUpQuestionManager() {
        QuestionManager questionManager = getMockedQuestionManager();
        MultiPlayerManager manager = createManager(questionManager);
        EntryExitUtil.assertSetUpEntryExit(manager, questionManager);
    }

    @Test
    public void setUpCellManager() {
        CellManager cellManager = getMockedCellManager();
        MultiPlayerManager manager = createManager(cellManager);
        EntryExitUtil.assertSetUpEntryExit(manager, cellManager);
    }

    @Test
    public void setUpLevelManager() {
        LevelManager levelManager = getMockedLevelManager();
        MultiPlayerManager manager = createManager(levelManager);

        EntryExitUtil.assertSetUpEntryExit(manager, levelManager);
    }

    @Test
    public void entry_CallEntry_WaitOtherState() {
        MultiPlayerManager manager = createManager();
        State waitOthersPlayerState = getMockedState();
        manager.setWaitOtherPlayersState(waitOthersPlayerState);
        manager.entry();
        verify(waitOthersPlayerState).entry();
    }
}
