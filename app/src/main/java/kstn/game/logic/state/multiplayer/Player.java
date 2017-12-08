package kstn.game.logic.state.multiplayer;

import kstn.game.R;

public class Player {
    private  int ipAddress;
    private String name="No Name";
    private  int avatarId = R.drawable.unknown_avatar;
    private int Score =0;

    public Player(int ipAddress, String name, int avatarId) {
        this.ipAddress = ipAddress;
        this.name = name;
        this.avatarId = avatarId;
    }

    public Player() {
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAvatarId(int avatarId) {
        this.avatarId = avatarId;
    }

    public int getScore() {
        return Score;
    }

    public void setScore(int score) {

        Score = score;
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
                    && name.equals(other.getName())
                    && avatarId == other.getAvatarId();
        }
        return false;
    }
}
