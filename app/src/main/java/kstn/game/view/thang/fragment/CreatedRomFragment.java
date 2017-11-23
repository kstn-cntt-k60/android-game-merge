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
import kstn.game.logic.model.Room;
import kstn.game.logic.state_event.TransitToRoomCreator;
import kstn.game.view.state.ViewStateManager;
import kstn.game.view.thang.adapter.NotifiAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class CreatedRomFragment extends Fragment {
    private ViewStateManager stateManager;

    public CreatedRomFragment() {
        // Required empty public constructor
    }

    public void setStateManager(ViewStateManager stateManager) {
        this.stateManager = stateManager;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_rom_created, container, false);
    }
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ListView LvNotiFi = view.findViewById(R.id.LvNotiFi);
        ArrayList<Room> data= new ArrayList<>();
        NotifiAdapter adapter = new NotifiAdapter(data,stateManager);
        LvNotiFi.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // send lời mời
                stateManager.eventManager.queue(new TransitToRoomCreator());


            }
        });
    }

}
