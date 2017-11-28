package kstn.game.view.state;

import kstn.game.logic.state_event.TransitToMenuState;
import kstn.game.view.state.singleplayer.CharCellManager;
import kstn.game.view.state.singleplayer.KeyboardManager;
import kstn.game.view.state.singleplayer.LifeManager;
import kstn.game.view.state.singleplayer.PlayFragment;
import kstn.game.view.state.singleplayer.ScoreManager;
import kstn.game.view.state.singleplayer.SongManager;

public class ViewSinglePlayerState extends ViewGameState {
    private PlayFragment fragment;
    private SongManager songManager;
    private ScoreManager scoreManager;
    private LifeManager lifeManager;
    private CharCellManager charCellManager;
    private KeyboardManager keyboardManager;

    public ViewSinglePlayerState(final ViewStateManager stateManager) {
        super(stateManager);
        songManager = new SongManager(stateManager);
        scoreManager = new ScoreManager(stateManager, songManager);
        lifeManager = new LifeManager(stateManager, songManager);
        charCellManager = new CharCellManager(stateManager);
        keyboardManager = new KeyboardManager(stateManager);
   }

    @Override
    public void entry() {
        fragment = new PlayFragment();
        fragment.setStateManager(stateManager);
        fragment.setSongManager(songManager);
        fragment.setScoreManager(scoreManager);
        fragment.setLifeManager(lifeManager);
        fragment.setCharCellManager(charCellManager);
        fragment.setKeyboardManager(keyboardManager);
        stateManager.activity.addFragment(fragment);
        fragment.entry();

        keyboardManager.entry();
        songManager.entry();
        scoreManager.entry();
        lifeManager.entry();
        charCellManager.entry();
        super.postEntry();
    }

    @Override
    public boolean onBack() {
        stateManager.eventManager.queue(new TransitToMenuState());
        return false;
    }

    @Override
    public void exit() {
        super.preExit();
        fragment.exit();
        keyboardManager.exit();
        lifeManager.exit();
        scoreManager.exit();
        songManager.exit();
        charCellManager.exit();
    }
}
