package kstn.game.view.state;

import kstn.game.logic.state_event.TransiteToMenuState;
import kstn.game.view.state.singleplayer.LifeManager;
import kstn.game.view.state.singleplayer.ScoreManager;
import kstn.game.view.state.singleplayer.SongManager;
import kstn.game.view.thang.fragment.PlayFragment;

public class ViewSinglePlayerState extends ViewGameState {
    private PlayFragment fragment;
    private SongManager songManager;
    private ScoreManager scoreManager;
    private LifeManager lifeManager;

    public ViewSinglePlayerState(ViewStateManager stateManager) {
        super(stateManager);
        songManager = new SongManager(stateManager);
        scoreManager = new ScoreManager(stateManager);
        lifeManager = new LifeManager(stateManager);
    }

    @Override
    public void entry() {
        fragment = new PlayFragment();

        fragment.setStateManager(stateManager);
        fragment.setSongManager(songManager);
        fragment.setScoreManager(scoreManager);
        fragment.setLifeManager(lifeManager);

        stateManager.activity.addFragment(fragment);

        songManager.entry();
        scoreManager.entry();
        lifeManager.entry();
    }

    @Override
    public boolean onBack() {
        stateManager.eventManager.queue(new TransiteToMenuState());
        return false;
    }

    @Override
    public void exit() {
        lifeManager.entry();
        scoreManager.exit();
        songManager.exit();
    }
}
