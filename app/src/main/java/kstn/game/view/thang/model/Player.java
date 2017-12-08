package kstn.game.view.thang.model;

import android.graphics.Color;

import kstn.game.R;

public class Player {
    private int ipAddress;
    private String name = "No Name";
    private int avatarId = R.drawable.unknown_avatar;
    private int Score = 0;
    private int IdColor = Color.parseColor("#752c74");

    public int getIdColor() {
        return IdColor;
    }

    public void setIdColor(int idColor) {
        IdColor = idColor;
    }

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
}
