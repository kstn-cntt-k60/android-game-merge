package kstn.game.view.thang.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import kstn.game.R;
import kstn.game.logic.state.multiplayer.ScoreDb;

/**
 * Created by thang on 12/1/2017.
 */

public class RankingAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<ScoreDb> data;

    public RankingAdapter(Context context, ArrayList<ScoreDb> data) {
        this.context = context;
        this.data = data;
    }

    @Override
    public int getCount() {
        return data.size()-3;
    }

    @Override
    public Object getItem(int i) {
        return data.get(i+3);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.giao_dien_each_rank,viewGroup,false);
        TextView txtSTT = view.findViewById(R.id.txtSTT);
        TextView txtName = view.findViewById(R.id.txtName);
        TextView txtScore = view.findViewById(R.id.txtScore);
        txtSTT.setText((i+4)+"");
        txtName.setText(data.get(i+3).getName());
        txtScore.setText(data.get(i+3).getScore()+"");
        return view;
    }
}
