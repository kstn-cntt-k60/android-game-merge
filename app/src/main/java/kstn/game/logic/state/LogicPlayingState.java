package kstn.game.logic.state;

import kstn.game.logic.cone.Cone;
import kstn.game.logic.process.ProcessManager;
import kstn.game.logic.state.multiplayer.MultiPlayerManager;
import kstn.game.view.screen.ImageView;
import kstn.game.view.state.ViewGameState;

/**
 * Created by thang on 12/1/2017.
 */

public class LogicPlayingState extends LogicGameState {
    private Cone cone;
    private ImageView backgroundView;
    private MultiPlayerManager multiPlayerManager;

    public LogicPlayingState(final LogicStateManager stateManager,
                             ProcessManager processManager,
                             ViewGameState viewPlayingState,
                             Cone cone) {
        super(stateManager, processManager,viewPlayingState);
        //TODO


    }

    @Override
    public void entry() {
        cone.entry();
    }

    @Override
    public void exit() {
        cone.exit();
    }
}
