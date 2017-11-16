package kstn.game.logic.state;

import android.graphics.Bitmap;

import java.io.IOException;

import kstn.game.logic.cone.Cone;
import kstn.game.logic.model.SinglePlayerManager;
import kstn.game.view.screen.ImageView;

public class LogicSinglePlayerState extends LogicGameState {
    private Cone cone;
    private ImageView backgroundView;
    private SinglePlayerManager playerManager;

    public LogicSinglePlayerState(LogicStateManager stateManager) {
        super(stateManager);

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
        // playerManager.setQuestion();
        playerManager.entry();
    }

    @Override
    public void exit() {
        playerManager.entry();
        cone.exit();
        stateManager.root.removeView(backgroundView);
    }
}
