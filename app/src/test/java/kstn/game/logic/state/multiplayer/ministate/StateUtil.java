package kstn.game.logic.state.multiplayer.ministate;

import static org.mockito.Mockito.mock;

public class StateUtil {
    public static State getMockedState() {
        return mock(State.class);
    }
}
