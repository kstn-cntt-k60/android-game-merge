package kstn.game.logic.state;

import android.graphics.Bitmap;
import android.util.Log;

import java.io.IOException;

import kstn.game.view.screen.ImageView;

/**
 * Created by qi on 14/11/2017.
 */

public class LogicLoginState extends LogicGameState {

    public LogicLoginState(LogicStateManager stateManager) {
        super(stateManager);
    }

    @Override
    public void entry() {
//        stateManager.root.remove();
        Bitmap background = null;
        try {
            background = stateManager.assetManager.getBitmap("cute.png");
        } catch (IOException e) {
            e.printStackTrace();
        }
        ImageView backgroundView = new ImageView(0, 0, 2, 1.8f * 2, background);
        stateManager.root.addView(backgroundView);

        Log.i(this.getClass().getName(), "LogicLoginState");
    }

    @Override
    public void exit() {

    }
}
