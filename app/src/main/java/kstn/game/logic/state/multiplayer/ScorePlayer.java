package kstn.game.logic.state.multiplayer;

public class ScorePlayer {
    private Player player;
    private int score;

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
}
