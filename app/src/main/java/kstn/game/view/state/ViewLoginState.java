package kstn.game.view.state;

import android.util.Log;

import kstn.game.logic.state_event.TransiteToMenuState;
import kstn.game.view.thang.fragment.LoginFragment;

public class ViewLoginState extends ViewGameState {

    public ViewLoginState(ViewStateManager stateManager) {
        super(stateManager);
    }
    @Override
    public void entry() {
        Log.i(this.getClass().getName(), "ViewLoginState");
        stateManager.activity.addFragment(new LoginFragment());
    }

    @Override
    public boolean onBack() {
        stateManager.eventManager.queue(new TransiteToMenuState());
        return true;
    }

    @Override
    public void exit() {

    }
}
