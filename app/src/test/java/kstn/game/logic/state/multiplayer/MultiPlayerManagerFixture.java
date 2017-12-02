package kstn.game.logic.state.multiplayer;

import kstn.game.logic.event.EventManager;

import static org.mockito.Mockito.mock;

public class MultiPlayerManagerFixture {
    EventManager eventManager = mock(EventManager.class);
    ScorePlayerManager scorePlayerManager = mock(ScorePlayerManager.class);

    MultiPlayerManager build() {
        return new MultiPlayerManager(
                eventManager,
                scorePlayerManager);
    }
}
