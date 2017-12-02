package kstn.game.logic.state.multiplayer;

import kstn.game.logic.cone.Cone;
import kstn.game.logic.process.ProcessManager;
import kstn.game.logic.state.LogicGameState;
import kstn.game.logic.state.LogicStateManager;
import kstn.game.view.screen.View;
import kstn.game.view.screen.ViewManager;
import kstn.game.view.state.ViewGameState;

public class LogicPlayingState extends LogicGameState {
    private final MultiPlayerManager playerManager;
    private final ViewManager root;
    private final View backgroundView;
    private final Cone cone;

    public LogicPlayingState(
            LogicStateManager stateManager,
            ProcessManager processManager,
            ViewGameState viewPlayingState,
            MultiPlayerManager playerManager,
            ViewManager root,
            View backgroundView,
            Cone cone) {
        super(stateManager, processManager, viewPlayingState);
        this.playerManager = playerManager;
        this.root = root;
        this.backgroundView = backgroundView;
        this.cone = cone;
    }

    @Override
    public void entry() {
        root.addView(backgroundView);
        cone.entry();
        playerManager.entry();

        super.postEntry();
    }

    @Override
    public void onViewReady() {
        playerManager.onViewReady();
    }

    @Override
    public void exit() {
        super.preExit();

        playerManager.exit();
        cone.exit();
        root.addView(backgroundView);
    }
}
