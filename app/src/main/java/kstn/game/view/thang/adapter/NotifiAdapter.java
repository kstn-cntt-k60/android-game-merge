package kstn.game.view.thang.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import kstn.game.R;
import kstn.game.logic.state_event.TransitToWaitRoom;
import kstn.game.view.state.ViewStateManager;
import kstn.game.view.thang.Model.Room;

/**
 * Created by thang on 11/9/2017.
 */

public class NotifiAdapter extends BaseAdapter {
    private  ArrayList<Room> data;

    private ViewStateManager stateManager;

    public NotifiAdapter(ArrayList<Room> data, ViewStateManager stateManager) {
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
    public View getView(final int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = LayoutInflater.from(stateManager.activity);
        view = inflater.inflate(R.layout.notification,viewGroup,false);
        TextView txtNoti = (TextView) view.findViewById(R.id.txtNoti);
        Button btnHide = (Button)view.findViewById(R.id.btnHide);
        Button btnAccept = (Button)view.findViewById(R.id.btnAccept);
        txtNoti.setText(data.get(i).getRoomName());


        btnHide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                data.remove(i);
                notifyDataSetChanged();
            }
        });
        btnAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stateManager.eventManager.queue(new TransitToWaitRoom());
            }
        });

        return view;
    }
}
