package kstn.game.logic.state;

import android.graphics.Bitmap;
import android.util.Log;

import java.io.IOException;

import kstn.game.view.screen.ImageView;

/**
 * Created by qi on 14/11/2017.
 */

public class LogicLoginState extends LogicGameState {
    private ImageView backgroundView;

    public LogicLoginState(LogicStateManager stateManager) {
        super(stateManager);

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
