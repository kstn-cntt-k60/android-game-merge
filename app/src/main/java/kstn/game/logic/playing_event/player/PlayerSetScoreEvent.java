package kstn.game.logic.playing_event.player;

import kstn.game.logic.event.GameEventData;
import kstn.game.logic.playing_event.PlayingEventType;

public class PlayerSetScoreEvent extends GameEventData {
    private final int score;

    public PlayerSetScoreEvent(int score) {
        super(PlayingEventType.PLAYER_SET_SCORE);
        this.score = score;
    }

    public int getScore() {
        return score;
    }
}
