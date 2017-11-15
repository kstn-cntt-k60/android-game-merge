package kstn.game.logic.model;

public class SerializePlayer implements java.io.Serializable {
    private int score;

    SerializePlayer(int score) {
        this.score = score;
    }

    public int getScore() {
        return score;
    }
}
