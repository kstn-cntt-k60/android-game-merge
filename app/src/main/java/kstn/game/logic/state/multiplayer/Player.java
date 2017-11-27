package kstn.game.logic.state.multiplayer;

public class Player {
    private int score = 0;
    private boolean isActive = true;
    private String name;
    private int avatarId;

    public String getName() {
        return name;
    }

    public int getAvatarId() {
        return avatarId;
    }

    public Player() {
    }

    public Player(String name, int avatarId) {
        this.name = name;
        this.avatarId = avatarId;
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
