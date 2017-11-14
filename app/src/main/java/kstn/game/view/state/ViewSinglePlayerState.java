package kstn.game.view.state;

import android.util.Log;

import kstn.game.logic.cone.ConeEventType;
import kstn.game.logic.cone.ConeStopEventData;
import kstn.game.logic.event.EventData;
import kstn.game.logic.event.EventListener;
import kstn.game.logic.state_event.TransiteToMenuState;

/**
 * Created by qi on 13/11/2017.
 */

public class ViewSinglePlayerState extends ViewGameState {

    public ViewSinglePlayerState(ViewStateManager stateManager) {
        super(stateManager);
    }

    @Override
    public void entry() {
        git p

    }

    @Override
    public boolean onBack() {
        stateManager.eventManager.queue(new TransiteToMenuState());
        Log.i(this.toString(),  "onBack");
        return false;
    }

    @Override
    public void exit() {

    }
}
