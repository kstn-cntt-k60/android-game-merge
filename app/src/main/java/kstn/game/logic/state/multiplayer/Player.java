package kstn.game.logic.state.multiplayer;

public class Player {
    private final int ipAddress;
    private final String name;
    private final int avatarId;

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
