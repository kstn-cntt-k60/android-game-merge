package kstn.game.view.thang.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import kstn.game.R;
import kstn.game.logic.state.multiplayer.Player;

/**
 * Created by thang on 12/5/2017.
 */

public class MultiAdapter extends BaseAdapter {
    private ArrayList<Player> data;
    private Context context;


    public MultiAdapter(ArrayList<Player> data, Context context) {
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
        TextView txtName = view.findViewById(R.id.txtNamePlayer);
        TextView txtScore = view.findViewById(R.id.txtScorePlayer);
        ImageView img = view.findViewById(R.id.imgPlayer);
        img.setImageResource(data.get(i).getAvatarId());
        img.setBackgroundColor(data.get(i).getIdColor());
        txtName.setText(data.get(i).getName());
        txtScore.setText(data.get(i).getScore()+"");
        return view;
    }
}
