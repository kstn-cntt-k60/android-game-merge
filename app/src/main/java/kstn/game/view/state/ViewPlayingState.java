package kstn.game.view.state;

import kstn.game.logic.state_event.TransitToCreatedRoomsState;
import kstn.game.view.state.multiplayer.MultiPlayerSongManager;
import kstn.game.view.state.multiplayer.PlayerProxy;
import kstn.game.view.state.multiplayer.ToastManager;

public class ViewPlayingState extends ViewGameState {
    private final MultiPlayerSongManager multiPlayerSongManager;
    private final ToastManager toastManager;
    private PlayerProxy playerProxy = null;

    public ViewPlayingState(ViewStateManager stateManager) {
        super(stateManager);

        multiPlayerSongManager = new MultiPlayerSongManager(stateManager);
        toastManager = new ToastManager(stateManager.eventManager, stateManager.activity);
    }

    @Override
    public void entry() {
        toastManager.entry();
        multiPlayerSongManager.entry();

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

        multiPlayerSongManager.exit();
        toastManager.exit();
    }
}
