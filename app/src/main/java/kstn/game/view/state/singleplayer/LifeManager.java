package kstn.game.view.state.singleplayer;

import android.view.View;
import android.widget.TextView;

import kstn.game.R;
import kstn.game.view.state.ViewStateManager;

/**
 * Created by tung on 15/11/2017.
 */

public class LifeManager {
    private ViewStateManager stateManager;
    private int lifeCount;
    private TextView txtLife;

    public LifeManager(ViewStateManager stateManager) {
        this.stateManager = stateManager;
    }

    public void onViewCreated(View view) {
        txtLife = view.findViewById(R.id.txtMang);
        lifeCount = 4;
        txtLife.setText("" + lifeCount);
    }

    public void entry() {
    }

    public void exit() {
    }

    public int count() {
        return lifeCount;
    }

    public void increase(int value) {
        assert (value > 0);
        lifeCount += value;
        txtLife.setText("" + lifeCount);
    }

    public void decrease(int value) {
        assert (value > 0);
        if (lifeCount >= value) {
            lifeCount -= value;
            txtLife.setText("" + lifeCount);
        }
    }
}
