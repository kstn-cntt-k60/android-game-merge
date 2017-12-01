package kstn.game.logic.state.multiplayer;

import kstn.game.logic.cone.Cone;
import kstn.game.logic.process.ProcessManager;
import kstn.game.logic.state.LogicGameState;
import kstn.game.logic.state.LogicStateManager;
import kstn.game.view.screen.View;
import kstn.game.view.screen.ViewManager;
import kstn.game.view.state.ViewGameState;

public class LogicPlayingState extends LogicGameState {
    private final ViewManager root;
    private final View backgroundView;
    private final Cone cone;

    public LogicPlayingState(
            LogicStateManager stateManager,
            ProcessManager processManager,
            ViewGameState viewPlayingState,
            ViewManager root,
            View backgroundView,
            Cone cone
    ) {
        super(stateManager, processManager, viewPlayingState);
        this.root = root;
        this.backgroundView = backgroundView;
        this.cone = cone;
    }

    @Override
    public void entry() {
        root.addView(backgroundView);
        cone.entry();
    }

    @Override
    public void exit() {
        cone.exit();
        root.addView(backgroundView);
    }
}
