package kstn.game.view.state;

import kstn.game.logic.event.EventData;
import kstn.game.logic.event.EventListener;
import kstn.game.logic.playing_event.PlayingEventType;
import kstn.game.logic.playing_event.sync.LogicPlayingReadyEvent;
import kstn.game.logic.playing_event.sync.ViewPlayingReadyEvent;
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

    private EventListener logicReadyListener;

    public ViewSinglePlayerState(final ViewStateManager stateManager) {
        super(stateManager);
        songManager = new SongManager(stateManager);
        scoreManager = new ScoreManager(stateManager, songManager);
        lifeManager = new LifeManager(stateManager, songManager);
        charCellManager = new CharCellManager(stateManager);
        keyboardManager = new KeyboardManager(stateManager);

        logicReadyListener = new EventListener() {
            @Override
            public void onEvent(EventData event) {
                LogicPlayingReadyEvent event1 = (LogicPlayingReadyEvent)event;
                stateManager.eventManager.queue(
                        new ViewPlayingReadyEvent(true));
            }
        };
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

        stateManager.eventManager.addListener(
                PlayingEventType.LOGIC_SINGLE_PLAYER_READY,
                logicReadyListener
        );

        stateManager.eventManager.queue(
                new ViewPlayingReadyEvent(false));
    }

    @Override
    public boolean onBack() {
        stateManager.eventManager.queue(new TransitToMenuState());
        return false;
    }

    @Override
    public void exit() {
        fragment.exit();
        keyboardManager.exit();
        lifeManager.exit();
        scoreManager.exit();
        songManager.exit();
        charCellManager.exit();

        stateManager.eventManager.removeListener(
                PlayingEventType.LOGIC_SINGLE_PLAYER_READY,
                logicReadyListener
        );
    }
}
