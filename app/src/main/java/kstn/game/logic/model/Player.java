package kstn.game.logic.model;

public class Player {
    private int playerId;
    private String name;
    private int avatarId;
    private boolean isHost;
    private int score;

    public Player(int playerId, String name, int avatarId, boolean isHost) {
        this.playerId = playerId;
        this.name = name;
        this.avatarId = avatarId;
        this.isHost = isHost;
        score = 0;
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

    public void increaseScore(int value) {
        assert (value > 0);
        assert (value % 100 == 0);
        score += value;
    }

    public void divideScoreByHalf() {
        score = (score + 1) / 2;
    }

    public void lostScore() {
        score = 0;
    }

    public void x2Score() {
        score *= 2;
    }

}
