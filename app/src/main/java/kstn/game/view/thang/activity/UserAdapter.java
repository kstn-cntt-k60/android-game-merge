package kstn.game.view.thang.activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;


import kstn.game.R;
import kstn.game.logic.model.PlayerModel;

/**
 * Created by thang on 10/1/2017.
 */

public class UserAdapter extends BaseAdapter {
    private ArrayList<PlayerModel> data;
    private Context context;

    public UserAdapter(ArrayList<PlayerModel> data, Context context) {
        this.data = data;
        this.context = context;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return data.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.giao_dien_user,viewGroup,false);
        android.widget.ImageView img = (android.widget.ImageView) view.findViewById(R.id.img);
        TextView txtMang = (TextView) view.findViewById(R.id.txtMang);
        TextView txtDiem = (TextView) view.findViewById(R.id.txtDiem);
        TextView txtTen = (TextView) view.findViewById(R.id.txtTen);
//        txtMang.setText("2");
//        txtDiem.setText("0");
        txtTen.setText(data.get(i).getName());
        img.setImageResource(data.get(i).getAvatarId());
        return view;
    }
}
