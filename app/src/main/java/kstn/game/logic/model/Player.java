package kstn.game.logic.model;

public class Player {
    private int playerId;
    private String name;
    private int avatarId;
    private boolean isHost;
    private int score;
    private int life;

    public Player(int playerId, String name, int avatarId, boolean isHost) {
        this.playerId = playerId;
        this.name = name;
        this.avatarId = avatarId;
        this.isHost = isHost;
        score = 0;
        life = 4;
    }

    public Player(int playerId, String name, int avatarId) {
        this(playerId, name, avatarId, false);
    }

    public Player() {
        this(0, "", 0, true);
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return playerId;
    }

    public boolean isHost() {
        return this.isHost;
    }

    public int getAvatarId() {
        return avatarId;
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
