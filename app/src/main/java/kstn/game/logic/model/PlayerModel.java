package kstn.game.logic.model;

import java.io.Serializable;

/**
 * Created by thang on 10/1/2017.
 */

public class PlayerModel implements Serializable {
    private final String ten;
    private final int avatarId;
    private final int point;

    public PlayerModel(String name, int avatarId, int point) {
        this.ten = name;
        this.avatarId = avatarId;
        this.point = point;
    }

    public PlayerModel(String ten, int idAnh) {
        this.ten = ten;
        avatarId = idAnh;
        point = 0;
    }

    public String getName() {
        return ten;
    }

    public int getAvatarId() {
        return avatarId;
    }

    public int getPoint() {
        return point;
    }

    public PlayerModel setPoint(int point) {
        return new PlayerModel(ten, avatarId, point);
    }
}
