package kstn.game.logic.playing_event.player;

import kstn.game.logic.event.BaseEventData;
import kstn.game.logic.event.EventType;
import kstn.game.logic.playing_event.PlayingEventType;

public class SinglePlayerStateChangeEvent extends BaseEventData {
    private int score;
    private int life;

    public SinglePlayerStateChangeEvent(int score, int life) {
        super(0);
        this.score = score;
        this.life = life;
    }

    @Override
    public EventType getEventType() {
        return PlayingEventType.PLAYER_STATE_CHANGE;
    }

    public int getScore() {
        return score;
    }

    public int getLife() {
        return life;
    }

    @Override
    public String getName() {
        return null;
    }
}
