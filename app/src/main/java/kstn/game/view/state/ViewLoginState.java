package kstn.game.view.state;

import kstn.game.logic.state_event.TransitToMenuState;
import kstn.game.view.thang.fragment.LoginFragment;

public class ViewLoginState extends ViewGameState {

    public ViewLoginState(ViewStateManager stateManager) {
        super(stateManager);
    }
    @Override
    public void entry() {
        stateManager.activity.addFragment(new LoginFragment());
    }

    @Override
    public boolean onBack() {
        stateManager.eventManager.queue(new TransitToMenuState());
        return true;
    }

    @Override
    public void exit() {

    }
}
