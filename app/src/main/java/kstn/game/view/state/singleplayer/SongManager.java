package kstn.game.view.state.singleplayer;

import android.media.MediaPlayer;

import kstn.game.R;
import kstn.game.logic.cone.ConeEventType;
import kstn.game.logic.event.EventData;
import kstn.game.logic.event.EventListener;
import kstn.game.view.state.ViewStateManager;

public class SongManager {
    private ViewStateManager stateManager;
    private MediaPlayer coneRotation;
    private MediaPlayer fail;
    private MediaPlayer tingTing;
    private EventListener coneAccelListener;

    public SongManager(ViewStateManager stateManager) {
        this.stateManager = stateManager;
        coneAccelListener = new EventListener() {
            @Override
            public void onEvent(EventData event) {
                startConeRotation();
            }
        };
    }

    public void onViewCreated(android.view.View view) {
        fail = MediaPlayer.create(stateManager.activity, R.raw.failure);
        tingTing = MediaPlayer.create(stateManager.activity, R.raw.tingting);
        coneRotation = MediaPlayer.create(stateManager.activity, R.raw.quay);

    }

    public void startFail() {
        fail.start();
    }
    public void startConeRotation() {
        coneRotation.start();
    }
    public void endConeRotation() {
        coneRotation.stop();
    }
    public void startTingTing() {
        tingTing.start();
    }

    public void entry() {
        stateManager.eventManager.addListener(ConeEventType.ACCELERATE, coneAccelListener);

    }

    public void exit() {
        stateManager.eventManager.removeListener(ConeEventType.ACCELERATE, coneAccelListener);
    }

}
