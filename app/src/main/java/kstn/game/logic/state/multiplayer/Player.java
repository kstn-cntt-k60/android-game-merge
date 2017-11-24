package kstn.game.logic.state.multiplayer;

public class Player {
    private int ipAddress;
    private String name;
    private int avatarId;

    public Player(int ipAddress, String name, int avatarId) {
        this.ipAddress = ipAddress;
        this.name = name;
        this.avatarId = avatarId;
    }

    public int getIpAddress() {
        return ipAddress;
    }

    public String getName() {
        return name;
    }

    public int getAvatarId() {
        return avatarId;
    }
}
