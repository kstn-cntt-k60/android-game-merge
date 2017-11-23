package kstn.game.view.state.singleplayer;

import android.media.MediaPlayer;

import kstn.game.R;
import kstn.game.logic.cone.ConeEventType;
import kstn.game.logic.event.EventData;
import kstn.game.logic.event.EventListener;
import kstn.game.logic.state_event.StateEventType;
import kstn.game.view.state.ViewStateManager;

public class SongManager {
    private ViewStateManager stateManager;
    private MediaPlayer coneRotation = null;
    private MediaPlayer fail = null;
    private MediaPlayer tingTing = null;
    private EventListener coneAccelListener;
    private EventListener coneStopListener;
    private EventListener transmitToMenuListener;
    public SongManager(ViewStateManager stateManager) {
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
        transmitToMenuListener = new EventListener() {
            @Override
            public void onEvent(EventData event) {
                endConeRotation();
            }
        };
    }

    public void onViewCreated(android.view.View view) {

    }

    public void startConeRotation() {
        coneRotation = MediaPlayer.create(stateManager.activity, R.raw.nhac_hieu);
        coneRotation.start();
    }

    public void endConeRotation()
    {
        if (coneRotation != null)
            coneRotation.stop();
    }

    public void startTingTing() {
        tingTing = MediaPlayer.create(stateManager.activity, R.raw.tingting);
        tingTing.start();
    }

    public void startFail() {
        fail = MediaPlayer.create(stateManager.activity, R.raw.failure);
        fail.start();
    }

    public void entry() {
        stateManager.eventManager.addListener(ConeEventType.ACCELERATE, coneAccelListener);
        stateManager.eventManager.addListener(ConeEventType.STOP, coneStopListener);
        stateManager.eventManager.addListener(StateEventType.MENU, transmitToMenuListener);
    }

    public void exit() {
        if (coneRotation != null)
            coneRotation.stop();
        stateManager.eventManager.removeListener(ConeEventType.STOP, coneStopListener);
        stateManager.eventManager.removeListener(ConeEventType.ACCELERATE, coneAccelListener);
        stateManager.eventManager.removeListener(StateEventType.MENU, transmitToMenuListener);
    }

}
