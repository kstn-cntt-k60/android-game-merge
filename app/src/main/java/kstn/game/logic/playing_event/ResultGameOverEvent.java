package kstn.game.logic.playing_event;

import kstn.game.logic.event.BaseEventData;
import kstn.game.logic.event.EventType;

/**
 * Created by thang on 11/30/2017.
 */

public class ResultGameOverEvent extends BaseEventData {
    private int score;

    public ResultGameOverEvent(int score) {
        super(0);
        this.score = score;
    }

    public int getScore() {
        return score;
    }

    @Override
    public EventType getEventType() {
        return PlayingEventType.GAME_OVER;
    }

    @Override
    public String getName() {
        return null;
    }
}
