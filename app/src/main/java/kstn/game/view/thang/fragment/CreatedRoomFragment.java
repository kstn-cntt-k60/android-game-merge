package kstn.game.view.thang.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import kstn.game.R;
import kstn.game.logic.playing_event.room.SetThisRoomEvent;
import kstn.game.logic.state.multiplayer.Player;
import kstn.game.logic.state_event.TransitToRoomCreator;
import kstn.game.logic.state_event.TransitToWaitRoom;
import kstn.game.view.state.ViewStateManager;
import kstn.game.view.state.multiplayer.ICreatedRooms;
import kstn.game.view.state.singleplayer.DialogManager;
import kstn.game.view.thang.Model.Room;
import kstn.game.view.thang.adapter.NotifiAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class CreatedRoomFragment extends Fragment implements ICreatedRooms{
    private Player Watchingplayer;
    private ViewStateManager stateManager;
    private ArrayList<Room> data;
    private NotifiAdapter adapter;

    public void setWatchingplayer(Player watchingplayer) {
        Watchingplayer = watchingplayer;
    }

    public CreatedRoomFragment() {

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
        LvNotiFi.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stateManager.eventManager.queue(new TransitToRoomCreator());

                LayoutInflater inflater = LayoutInflater.from(stateManager.activity);
                View viewAlertCreatorRoom = inflater.inflate(R.layout.giao_dien_alert_room_creator,null);
                final DialogManager dialogManager = new DialogManager(stateManager.activity,viewAlertCreatorRoom);
                final EditText edtRoomName = viewAlertCreatorRoom.findViewById(R.id.edtRoomName);
                Button btnHUY = viewAlertCreatorRoom.findViewById(R.id.btnHUY);
                Button btnOK = viewAlertCreatorRoom.findViewById(R.id.btnOK);
                btnHUY.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialogManager.getHopthoai().dismiss();
                    }
                });
                btnOK.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String roomName = edtRoomName.getText().toString();
                        if(!roomName.isEmpty()){
                            stateManager.eventManager.queue(new SetThisRoomEvent(roomName, 0));
                            stateManager.eventManager.queue(new TransitToWaitRoom());
                        }
                        else Toast.makeText(stateManager.activity,"Vui long nhap ten phong",
                                Toast.LENGTH_SHORT).show();
                    }
                });




            }
        });
    }
    public void entry() {

    }

    @Override
    public void addRoom(int ipAddress, String roomName, int numberOfPlayers) {
        Room room= new Room(ipAddress,roomName,numberOfPlayers);
        if(data==null) data = new ArrayList<>();
        data.add(room);
        if(adapter == null) adapter = new NotifiAdapter(data,stateManager);
        adapter.notifyDataSetChanged();

    }

    @Override
    public void remoteRoom(int ipAddress) {
        for(Room r:data){
            if(r.getIpAddress()==ipAddress){
                data.remove(r);
                adapter.notifyDataSetChanged();
                break;
            }
        }
    }
}
