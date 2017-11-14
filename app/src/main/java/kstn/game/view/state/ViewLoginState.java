package kstn.game.view.state;

import android.util.Log;

import kstn.game.MainActivity;
import kstn.game.logic.event.EventManager;
import kstn.game.logic.state_event.TransiteToMenuState;

/**
 * Created by qi on 14/11/2017.
 */

public class ViewLoginState extends ViewGameState {

    public ViewLoginState(ViewStateManager stateManager) {
        super(stateManager);
    }
    @Override
    public void entry() {

        Log.i(this.getClass().getName(), "ViewLoginState");
    }

    @Override
    public boolean onBack() {
        stateManager.eventManager.queue(new TransiteToMenuState());
        return false;
    }

    @Override
    public void exit() {

    }
}
