package kstn.game.view.state.multiplayer;

import android.media.MediaPlayer;

import kstn.game.R;
import kstn.game.logic.cone.ConeEventType;
import kstn.game.logic.event.EventData;
import kstn.game.logic.event.EventListener;
import kstn.game.logic.event.EventManager;
import kstn.game.logic.playing_event.PlayingEventType;
import kstn.game.logic.state.IEntryExit;
import kstn.game.logic.state_event.StateEventType;
import kstn.game.view.state.ViewStateManager;

public class MultiPlayerSongManager implements IEntryExit {
    private final ViewStateManager stateManager;

    private final EventListener coneAccelListener;
    private final EventListener coneStopListener;
    private final EventListener transitToMenuListener;
    private final EventListener songTingTingListner;
    private final EventListener songFailListener;

    private MediaPlayer coneRotation = null;

    public MultiPlayerSongManager(ViewStateManager stateManager) {
        this.stateManager = stateManager;

        coneAccelListener = new EventListener() {
            @Override
            public void onEvent(EventData event) {
                startConeRotation();
            }
        };

        coneStopListener = new EventListener() {
            @Override
            public void onEvent(EventData event) {
                endConeRotation();
            }
        };

        transitToMenuListener = new EventListener() {
            @Override
            public void onEvent(EventData event) {
                endConeRotation();
            }
        };

        songTingTingListner = new EventListener() {
            @Override
            public void onEvent(EventData event) {
                startTingTing();
            }
        };

        songFailListener = new EventListener() {
            @Override
            public void onEvent(EventData event) {
                startFail();
            }
        };
    }

    @Override
    public void entry() {
        EventManager eventManager = stateManager.eventManager;

        eventManager.addListener(ConeEventType.ACCELERATE, coneAccelListener);
        eventManager.addListener(ConeEventType.STOP, coneStopListener);
        eventManager.addListener(StateEventType.MENU, transitToMenuListener);
        eventManager.addListener(PlayingEventType.SONG_TINGTING, songTingTingListner);
        eventManager.addListener(PlayingEventType.SONG_FAIL, songFailListener);
    }

    @Override
    public void exit() {
        if (coneRotation != null)
            coneRotation.stop();

        EventManager eventManager = stateManager.eventManager;

        eventManager.removeListener(PlayingEventType.SONG_FAIL, songFailListener);
        eventManager.removeListener(PlayingEventType.SONG_TINGTING, songTingTingListner);
        eventManager.removeListener(StateEventType.MENU, transitToMenuListener);
        eventManager.removeListener(ConeEventType.STOP, coneStopListener);
        eventManager.removeListener(ConeEventType.ACCELERATE, coneAccelListener);
    }

    private void startConeRotation() {
        coneRotation = MediaPlayer.create(stateManager.activity, R.raw.nhac_hieu);
        coneRotation.start();
    }

    private void endConeRotation() {
        if (coneRotation != null)
            coneRotation.stop();
    }

    private void startTingTing() {
        MediaPlayer tingTing = MediaPlayer.create(stateManager.activity, R.raw.tingting);
        tingTing.start();
    }

    private void startFail() {
        MediaPlayer fail = MediaPlayer.create(stateManager.activity, R.raw.failure);
        fail.start();
    }
}
