package kstn.game.logic.state;

import android.graphics.Bitmap;

import java.io.IOException;

import kstn.game.view.screen.ImageView;

public class LogicMenuState extends LogicGameState {
    ImageView backgroundView;

    public LogicMenuState(LogicStateManager manager) {
        super(manager);
        Bitmap background = null;
        try {
            background = stateManager.assetManager.getBitmap("bg.jpg");
        } catch (IOException e) {
            e.printStackTrace();
        }
        backgroundView = new ImageView(0, 0, 2, 1.8f * 2, background);
    }

    @Override
    public void entry() {
        stateManager.root.addView(backgroundView);
    }

    @Override
    public void exit() {
        stateManager.root.removeView(backgroundView);
    }
}
