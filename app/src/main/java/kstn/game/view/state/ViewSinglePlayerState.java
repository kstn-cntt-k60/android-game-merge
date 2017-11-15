package kstn.game.view.state;

import kstn.game.logic.state_event.TransiteToMenuState;
import kstn.game.view.state.singleplayer.CharCellManager;
import kstn.game.view.state.singleplayer.LifeManager;
import kstn.game.view.state.singleplayer.ScoreManager;
import kstn.game.view.state.singleplayer.SongManager;
import kstn.game.view.thang.fragment.PlayFragment;

public class ViewSinglePlayerState extends ViewGameState {
    private PlayFragment fragment;
    private SongManager songManager;
    private ScoreManager scoreManager;
    private LifeManager lifeManager;
    private CharCellManager charCellManager;

    public ViewSinglePlayerState(ViewStateManager stateManager) {
        super(stateManager);
        songManager = new SongManager(stateManager);
        scoreManager = new ScoreManager(stateManager);
        lifeManager = new LifeManager(stateManager);
        charCellManager = new CharCellManager(stateManager);
    }

    @Override
    public void entry() {
        fragment = new PlayFragment();

        fragment.setStateManager(stateManager);
        fragment.setSongManager(songManager);
        fragment.setScoreManager(scoreManager);
        fragment.setLifeManager(lifeManager);
        fragment.setCharCellManager(charCellManager);

        stateManager.activity.addFragment(fragment);

        songManager.entry();
        scoreManager.entry();
        lifeManager.entry();
        charCellManager.entry();
    }

    @Override
    public boolean onBack() {
        songManager.exit();
        stateManager.eventManager.queue(new TransiteToMenuState());
        return false;
    }

    @Override
    public void exit() {
        lifeManager.exit();
        scoreManager.exit();
        songManager.exit();
        charCellManager.exit();
    }
}
