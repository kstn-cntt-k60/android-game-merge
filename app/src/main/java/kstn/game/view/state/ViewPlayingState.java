package kstn.game.view.state;

import kstn.game.view.state.singleplayer.CharCellManager;
import kstn.game.view.state.singleplayer.KeyboardManager;
import kstn.game.view.thang.fragment.MultiPlayFragment;
import kstn.game.logic.state_event.TransitToCreatedRoomsState;
import kstn.game.view.state.multiplayer.MultiPlayerSongManager;
import kstn.game.view.state.multiplayer.PlayerProxy;
import kstn.game.view.state.multiplayer.ToastManager;

public class ViewPlayingState extends ViewGameState {
    private final MultiPlayerSongManager multiPlayerSongManager;
    private final ToastManager toastManager;
    private PlayerProxy playerProxy = null;

    private CharCellManager charCellManager;
    private KeyboardManager keyboardManager;
    private MultiPlayFragment fragment;

    public ViewPlayingState(ViewStateManager stateManager) {
        super(stateManager);
        charCellManager = new CharCellManager(stateManager);
        keyboardManager = new KeyboardManager(stateManager);
        multiPlayerSongManager = new MultiPlayerSongManager(stateManager);
        toastManager = new ToastManager(stateManager.eventManager, stateManager.activity);
    }

    @Override
    public void entry() {
        toastManager.entry();
        multiPlayerSongManager.entry();

        fragment = new MultiPlayFragment();
        fragment.setStateManager(stateManager);
        fragment.setCharCellManager(charCellManager);
        fragment.setKeyboardManager(keyboardManager);
        fragment.entry();

        playerProxy = new PlayerProxy(stateManager.eventManager, fragment);

        keyboardManager.entry();
        charCellManager.entry();
        playerProxy.entry();
        stateManager.activity.addFragment(fragment);

        super.postEntry();
    }

    @Override
    public boolean onBack() {
        stateManager.eventManager.queue(new TransitToCreatedRoomsState());
        return true;
    }

    @Override
    public void exit() {
        super.preExit();

        if (playerProxy != null)
            playerProxy.exit();

        charCellManager.exit();
        keyboardManager.exit();
        fragment.exit();

        multiPlayerSongManager.exit();
        toastManager.exit();
    }
}
