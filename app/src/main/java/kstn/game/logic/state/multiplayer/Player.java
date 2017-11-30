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

    @Override
    public boolean equals(Object object) {
        if (object instanceof Player) {
            Player other = (Player) object;
            return ipAddress == other.getIpAddress()
                    && name == other.getName()
                    && avatarId == other.getAvatarId();
        }
        return false;
    }
}
