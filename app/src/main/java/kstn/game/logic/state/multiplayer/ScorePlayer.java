package kstn.game.logic.state.multiplayer;

public class ScorePlayer {
    private Player player;
    private int score;
    private boolean isReady = false;
    private boolean isActive = true;

    public ScorePlayer(Player player) {
        this.player = player;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getScore() {
        return score;
    }

    public Player getPlayer() {
        return player;
    }

    public void ready() {
        isReady = true;
    }

    public boolean isReady() {
        return isReady;
    }

    public void deactivate() {
        isActive = true;
    }

    public boolean isActive() {
        return isActive;
    }
}
