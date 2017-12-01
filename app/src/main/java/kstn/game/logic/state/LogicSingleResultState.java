package kstn.game.logic.state;

import android.graphics.Bitmap;
import android.util.Log;

import java.io.IOException;

import kstn.game.logic.playing_event.ResultGameOverEvent;
import kstn.game.view.screen.ImageView;

public class LogicSingleResultState extends LogicGameState {
    ImageView backgroundView;
    private int score = 0;

    public LogicSingleResultState(LogicStateManager stateManager) {
        super(stateManager, stateManager.processManager,
                stateManager.viewStateManager.singleResultState);
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
    public void onViewReady() {
        ResultGameOverEvent event = new ResultGameOverEvent(score);
        stateManager.eventManager.trigger(event);
        Log.i("SingleResult", "" + score);
    }

    @Override
    public void entry() {
        stateManager.root.addView(backgroundView);
        super.postEntry();
    }

    @Override
    public void exit() {
        super.preExit();
        stateManager.root.removeView(backgroundView);
    }
}
