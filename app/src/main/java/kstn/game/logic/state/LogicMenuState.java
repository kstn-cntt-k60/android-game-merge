package kstn.game.logic.state;

import android.graphics.Bitmap;
import android.util.Log;

import java.io.IOException;

import kstn.game.logic.cone.Cone;
import kstn.game.view.screen.ImageView;

/**
 * Created by qi on 13/11/2017.
 */

public class LogicMenuState extends LogicGameState {
    ImageView backgroundView;
    private Cone cone;

    public LogicMenuState(LogicStateManager manager) {
        super(manager);
        Bitmap background = null;
        try {
            background = stateManager.assetManager.getBitmap("bg.jpg");
        } catch (IOException e) {
            e.printStackTrace();
        }
        backgroundView = new ImageView(0, 0, 2, 1.8f * 2, background);
        cone = new Cone(stateManager.processManager, stateManager.assetManager,
                stateManager.eventManager,stateManager.timeManager,stateManager.root);

    }

    @Override
    public void entry() {
        Log.i("LogicMenuState", "");
        stateManager.root.addView(backgroundView);
        cone.entry();
    }

    @Override
    public void exit() {
        cone.exit();
        stateManager.root.removeView(backgroundView);

    }
}
