package kstn.game.logic.playing_event.player;

import kstn.game.logic.event.GameEventData;
import kstn.game.logic.playing_event.PlayingEventType;

public class SinglePlayerStateChangeEvent extends GameEventData {
    private int score;
    private int life;

    public SinglePlayerStateChangeEvent(int score, int life) {
        super(PlayingEventType.PLAYER_STATE_CHANGE);
        this.score = score;
        this.life = life;
    }
    public int getScore() {
        return score;
    }

    public int getLife() {
        return life;
    }
}
