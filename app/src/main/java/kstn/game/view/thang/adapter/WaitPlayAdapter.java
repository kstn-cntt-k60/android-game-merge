package kstn.game.view.thang.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

import kstn.game.R;
import kstn.game.logic.state.multiplayer.Player;
import kstn.game.view.state.ViewStateManager;

/**
 * Created by thang on 11/24/2017.
 */

public class WaitPlayAdapter extends BaseAdapter {
    private ArrayList<Player> data;
    private ViewStateManager stateManager;

    public WaitPlayAdapter(ArrayList<Player> data, ViewStateManager stateManager) {
        this.data = data;
        this.stateManager = stateManager;
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
        LayoutInflater inflater = LayoutInflater.from(stateManager.activity);
        view = inflater.inflate(R.layout.player_waitting,viewGroup,false);
        ImageView idAvatar =(ImageView) view.findViewById(R.id.idAvatar);
        TextView PlayerName = (TextView) view.findViewById(R.id.PlayerName);
       //

        return view;
    }
}
