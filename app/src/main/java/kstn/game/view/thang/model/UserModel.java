package kstn.game.view.thang.model;

import java.io.Serializable;

/**
 * Created by thang on 10/1/2017.
 */

public class UserModel implements Serializable {
    private  String ten;
    private int IdAnh;
    private int point;

    public UserModel(String ten, int idAnh, int point) {
        this.ten = ten;
        IdAnh = idAnh;
        this.point = point;
    }

    public UserModel(String ten, int idAnh) {

        this.ten = ten;
        IdAnh = idAnh;
    }

    public String getTen() {
        return ten;
    }

    public int getIdAnh() {
        return IdAnh;
    }

    public int getPoint() {
        return point;
    }
}
