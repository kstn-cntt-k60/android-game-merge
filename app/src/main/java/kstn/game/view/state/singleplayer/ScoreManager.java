package kstn.game.view.state.singleplayer;

import android.view.View;
import android.widget.TextView;

import kstn.game.R;
import kstn.game.view.state.ViewStateManager;

public class ScoreManager {
    private ViewStateManager stateManager;
    private TextView txtScore;
    private int score;

    public ScoreManager(ViewStateManager stateManager) {
        this.stateManager = stateManager;
    }

    public void onViewCreated(View view) {
        txtScore = view.findViewById(R.id.txtMoney);
        setScore(0);
    }

    public void setScore(int score) {
        this.score = score;
        txtScore.setText(Integer.toString(score));
    }

    public void divideByHalf() {
        this.score = (score + 1) / 2;
        txtScore.setText(Integer.toString(score));
    }

    public void x2() {
        this.score *= 2;
        txtScore.setText(Integer.toString(score));
    }

    public void increase(int value) {
        assert (value > 0);
        this.score += value;
        txtScore.setText(Integer.toString(score));
    }

    public void decrease(int value) {
        assert (value > 0);
        if (score > value)
            score -= value;
        else
            score = 0;
        txtScore.setText(Integer.toString(score));
    }

    public void entry() {
    }

    public void exit() {
    }

}
