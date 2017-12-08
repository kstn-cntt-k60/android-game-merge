package kstn.game.logic.state.multiplayer;

public class ScoreDb {
    private int id;
    private  String name;
    private int score;

    public ScoreDb(String name, int score) {
        this.name = name;
        this.score = score;
    }

    public ScoreDb(int id, String name, int score) {
        this.id = id;
        this.name = name;
        this.score = score;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }
}
