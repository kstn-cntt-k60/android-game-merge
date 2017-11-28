package kstn.game.logic.state;

import android.graphics.Bitmap;

import java.io.IOException;

import kstn.game.logic.cone.Cone;
import kstn.game.logic.process.ProcessManager;
import kstn.game.logic.state.singleplayer.SinglePlayerManager;
import kstn.game.view.screen.ImageView;
import kstn.game.view.state.ViewGameState;

public class LogicSinglePlayerState extends LogicGameState {
    private Cone cone;
    private ImageView backgroundView;
    private SinglePlayerManager playerManager;

    public LogicSinglePlayerState(final LogicStateManager stateManager,
                                  ProcessManager processManager,
                                  ViewGameState viewSinglePlayerState) {
        super(stateManager, processManager, viewSinglePlayerState);

        Bitmap background = null;
        try {
            background = stateManager.assetManager.getBitmap("bg.jpg");
        } catch (IOException e) {
            e.printStackTrace();
        }
        backgroundView = new ImageView(0, 0, 2, 1.8f * 2, background);
        cone = new Cone(stateManager.processManager, stateManager.assetManager,
                        stateManager.eventManager, stateManager.timeManager, stateManager.root);

        playerManager = new SinglePlayerManager(cone, stateManager);
    }

    @Override
    public void entry() {
        stateManager.root.addView(backgroundView);
        cone.entry();
        playerManager.entry();
        super.postEntry();
    }

    @Override
    protected void onViewReady() {
        playerManager.onViewReady();
    }

    @Override
    public void exit() {
        super.preExit();
        playerManager.exit();
        cone.exit();
        stateManager.root.removeView(backgroundView);
    }
}
