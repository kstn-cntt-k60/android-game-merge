package kstn.game.logic.state.singleplayer;

public class SinglePlayerModel {
    private int score;
    private int life;

    public SinglePlayerModel() {
        score = 0;
        life = 4;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int value) {
        if (value < 0)
            score = 0;
        score = value;
    }

    public int getLife() {
        return life;
    }

    public void setLife(int value) {
        if (value < 0)
            life = 0;
        life = value;
    }
}
