package kstn.game.view.thang.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

import kstn.game.MainActivity;
import kstn.game.R;
import kstn.game.view.thang.fragment.MutiPlayFragment;

/**
 * Created by thang on 11/9/2017.
 */

public class NotifiAdapter extends BaseAdapter {
    ArrayList<String> data;
    Context context;

    public NotifiAdapter(ArrayList<String> data, Context context) {
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
    public View getView(final int i, View view, ViewGroup viewGroup) {
        LayoutInflater inflater = LayoutInflater.from(context);
        view = inflater.inflate(R.layout.notification,viewGroup,false);
        TextView txtNoti = (TextView) view.findViewById(R.id.txtNoti);
        Button btnHide = (Button)view.findViewById(R.id.btnHide);
        Button btnAccept = (Button)view.findViewById(R.id.btnAccept);
        txtNoti.setText(data.get(i));
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
                ((MainActivity)context.getApplicationContext()).addFragment(R.id.myLayout, new MutiPlayFragment());
            }
        });

        return view;
    }
}
