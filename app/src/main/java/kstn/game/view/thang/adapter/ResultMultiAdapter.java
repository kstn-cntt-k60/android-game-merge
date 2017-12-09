package kstn.game.view.thang.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import kstn.game.R;
import kstn.game.view.thang.model.Player;


/**
 * Created by thang on 12/9/2017.
 */

public class ResultMultiAdapter extends BaseAdapter {
    private List<Player> data;
    private Context context;

    public ResultMultiAdapter(List<Player> data, Context context) {
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
        view= inflater.inflate(R.layout.giao_dien_result_multi_each_player,viewGroup,false);
        ImageView img = view.findViewById(R.id.img);
        ImageView imgStar = view.findViewById(R.id.imgStar);
        TextView txtSTT = view.findViewById(R.id.txtSTT);
        TextView txtName = view.findViewById(R.id.txtName);
        TextView txtScore = view.findViewById(R.id.txtScore);
        if(i==0) imgStar.setImageResource(R.drawable.winnerstar);
        img.setImageResource(data.get(i).getAvatarId());
        txtSTT.setText(" "+(i+1));
        txtName.setText(data.get(i).getName());
        txtScore.setText(data.get(i).getScore()+"");
        return view;
    }
}
