package kstn.game.view.thang.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

import kstn.game.R;
import kstn.game.view.thang.adapter.NotifiAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class RomPlayFragment extends Fragment {


    public RomPlayFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_rom_play, container, false);
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ListView LvNotiFi = view.findViewById(R.id.LvNotiFi);
        ArrayList<String> data= new ArrayList<String>();
        data.add("Thắng đã mời bạn");
        data.add("Quý đã mời bạn");
        data.add("Tùng đã mời bạn");
        data.add("Hùng đã mời bạn");

        NotifiAdapter adapter = new NotifiAdapter(data,getActivity());
        LvNotiFi.setAdapter(adapter);
        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // send lời mời

            }
        });
    }

}
