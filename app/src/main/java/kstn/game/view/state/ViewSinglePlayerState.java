package kstn.game.view.state;

import android.util.Log;

import kstn.game.logic.state_event.TransiteToMenuState;
import kstn.game.view.thang.fragment.PlayFragment;

public class ViewSinglePlayerState extends ViewGameState {

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
        Log.i(this.toString(),  "onBack");
        return false;
    }

    @Override
    public void exit() {

    }
}
