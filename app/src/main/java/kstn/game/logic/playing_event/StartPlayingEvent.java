package kstn.game.logic.playing_event;

import kstn.game.logic.event.GameEventData;

public class StartPlayingEvent extends GameEventData {

    public StartPlayingEvent() {
        super(PlayingEventType.START_PLAYING);
    }

}
