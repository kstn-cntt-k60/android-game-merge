package kstn.game.view.state;

import kstn.game.logic.state_event.TransiteToMenuState;
import kstn.game.view.state.singleplayer.SongManager;
import kstn.game.view.thang.fragment.PlayFragment;

public class ViewSinglePlayerState extends ViewGameState {
    private PlayFragment fragment;
    private SongManager songManager;

    public ViewSinglePlayerState(ViewStateManager stateManager) {
        super(stateManager);
        songManager = new SongManager(stateManager);
    }

    @Override
    public void entry() {
        PlayFragment playFragment = new PlayFragment();
        playFragment.setStateManager(stateManager);
        stateManager.activity.addFragment(playFragment);
        songManager.entry();
    }

    @Override
    public boolean onBack() {
        songManager.exit();
        stateManager.eventManager.queue(new TransiteToMenuState());
        return false;
    }

    @Override
    public void exit() {

    }
}
