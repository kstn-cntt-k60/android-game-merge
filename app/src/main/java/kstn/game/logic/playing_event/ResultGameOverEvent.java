package kstn.game.logic.playing_event;

import kstn.game.logic.event.GameEventData;

public class ResultGameOverEvent extends GameEventData {
    private int score;

    public ResultGameOverEvent(int score) {
        super(PlayingEventType.GAME_OVER);
        this.score = score;
    }

    public int getScore() {
        return score;
    }
}
