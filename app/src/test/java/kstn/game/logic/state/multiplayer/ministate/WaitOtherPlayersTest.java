package kstn.game.logic.state.multiplayer.ministate;

import org.junit.Test;

import kstn.game.logic.state.multiplayer.MultiPlayerManager;
import kstn.game.logic.state.multiplayer.ScorePlayerManager;

import static kstn.game.logic.state.multiplayer.ministate.StateUtil.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class WaitOtherPlayersTest {
    private ScorePlayerManager getMockedScoreManager() {
        return mock(ScorePlayerManager.class);
    }

    private MultiPlayerManager getMockedMultiPlayerManager() {
        return mock(MultiPlayerManager.class);
    }

    WaitOtherPlayersState createState(State rotatableState,
                                      ScorePlayerManager scorePlayerManager,
                                      MultiPlayerManager multiPlayerManager) {
        WaitOtherPlayersState state = new WaitOtherPlayersState(
                scorePlayerManager,
                multiPlayerManager
        );
        state.setRotatableState(rotatableState);
        return state;
    }

    @Test
    public void nextPlayer_ToRotatableState_WhenRightTurn() {
        State rotatableState = getMockedState();
        ScorePlayerManager scorePlayerManager = getMockedScoreManager();
        MultiPlayerManager multiPlayerManager = getMockedMultiPlayerManager();

        WaitOtherPlayersState state = createState(
                rotatableState,
                scorePlayerManager, multiPlayerManager
        );
        when(scorePlayerManager.currentIsThisPlayer()).thenReturn(true);
        verify(multiPlayerManager, never()).makeTransitionTo(any(State.class));

        state.nextPlayer(1);

        verify(multiPlayerManager).makeTransitionTo(rotatableState);
    }

    @Test
    public void onNextPlayerEvent_DoNothing_WhenNotRightTurn() {
        State rotatableState = getMockedState();
        ScorePlayerManager scorePlayerManager = getMockedScoreManager();
        MultiPlayerManager multiPlayerManager = getMockedMultiPlayerManager();

        WaitOtherPlayersState state = createState(
                rotatableState,
                scorePlayerManager, multiPlayerManager
        );
        state.entry();
        when(scorePlayerManager.currentIsThisPlayer()).thenReturn(false);
        verify(multiPlayerManager, never()).makeTransitionTo(any(State.class));

        state.nextPlayer(2);

        verify(multiPlayerManager, never()).makeTransitionTo(any(State.class));
    }
}
