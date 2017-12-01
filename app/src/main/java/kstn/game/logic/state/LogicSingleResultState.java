package kstn.game.logic.state;

import android.graphics.Bitmap;

import java.io.IOException;

import kstn.game.view.screen.ImageView;

public class LogicSingleResultState extends LogicGameState {
    ImageView backgroundView;
    private int score = 0;

    public LogicSingleResultState(LogicStateManager stateManager) {
        super(stateManager);
        Bitmap background = null;
        try {
            background = stateManager.assetManager.getBitmap("bg.jpg");
        } catch (IOException e) {
            e.printStackTrace();
        }
        backgroundView = new ImageView(0, 0, 2, 1.8f * 2, background);
    }

    public void setScore(int score) {
        this.score = score;
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
