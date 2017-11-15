package kstn.game.view.state;

import kstn.game.logic.state_event.TransiteToMenuState;
import kstn.game.view.thang.fragment.PlayFragment;

public class ViewSinglePlayerState extends ViewGameState {
    private PlayFragment fragment;

    public ViewSinglePlayerState(ViewStateManager stateManager) {
        super(stateManager);
    }

    @Override
    public void entry() {
        PlayFragment playFragment = new PlayFragment();
        playFragment.setStateManager(stateManager);
        stateManager.activity.addFragment(playFragment);
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
