package kstn.game.logic.state.multiplayer;

public class Player {
    private int score = 0;
    private boolean isActive = true;

    public Player() {
    }

    int getScore() {
        return score;
    }

    void setScore(int value) {
        score = value;
    }

    boolean isActive() {
        return this.isActive;
    }

    void deactivate() {
        isActive = false;
    }
}
