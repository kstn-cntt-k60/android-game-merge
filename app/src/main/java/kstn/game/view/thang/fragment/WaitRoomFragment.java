package kstn.game.view.thang.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

import kstn.game.R;
import kstn.game.logic.playing_event.room.ExitRoomEvent;
import kstn.game.logic.state.multiplayer.Player;
import kstn.game.view.state.ViewStateManager;
import kstn.game.view.state.multiplayer.IWaitRoom;
import kstn.game.view.thang.adapter.WaitPlayAdapter;

public class WaitRoomFragment extends Fragment implements IWaitRoom {
    private ArrayList<Player> data;
    private WaitPlayAdapter adapter;
    private ViewStateManager stateManager;

    public void setStateManager(ViewStateManager stateManager) {
        this.stateManager = stateManager;
    }

    public WaitRoomFragment() {
        // Required empty public constructor
    }
    public void init() {
        data = new ArrayList<>();
        adapter = new WaitPlayAdapter(data,stateManager);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_wait_room, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ListView lvPlayerWaiting =view.findViewById(R.id.lvPlayerWaiting);
        lvPlayerWaiting.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        Button btnExit = view.findViewById(R.id.btnExit);
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stateManager.eventManager.queue(new ExitRoomEvent(
                        stateManager.wifiInfo.getIP()
                ));
            }
        });
    }

    @Override
    public void addPlayer(Player player) {
        Log.i("RoomFragment", "add");
        Boolean flag = true;
        for(Player player1: data){
            if(player1.getIpAddress()==player.getIpAddress()){
                flag = false;
                break;
            }
        }
        if(flag) {
            data.add(player);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void removePlayer(int playerIpAddress) {
        for(Player player : data){
            if(player.getIpAddress()==playerIpAddress){
                data.remove(player);
                adapter.notifyDataSetChanged();
                break;
            }
        }
    }
}
