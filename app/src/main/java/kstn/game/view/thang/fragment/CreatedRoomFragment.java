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
import java.util.ListIterator;

import kstn.game.R;
import kstn.game.logic.playing_event.room.SetThisRoomEvent;
import kstn.game.logic.state_event.TransitToCreatedRoomsState;
import kstn.game.logic.state_event.TransitToRoomCreator;
import kstn.game.logic.state_event.TransitToWaitRoom;
import kstn.game.view.state.ViewStateManager;
import kstn.game.view.state.multiplayer.ICreatedRooms;
import kstn.game.view.state.singleplayer.DialogManager;
import kstn.game.view.thang.model.Room;
import kstn.game.view.thang.adapter.NotifiAdapter;

public class CreatedRoomFragment extends Fragment implements ICreatedRooms{
    private ViewStateManager stateManager;
    private ArrayList<Room> data;
    private NotifiAdapter adapter;

    public CreatedRoomFragment() {
    }

    public void setStateManager(ViewStateManager stateManager) {
        this.stateManager = stateManager;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_rom_created, container, false);
    }

    public void init() {
        data = new ArrayList<>();
        adapter = new NotifiAdapter(data,stateManager);
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
                        stateManager.eventManager.queue(new TransitToCreatedRoomsState());
                    }
                });
                btnOK.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String roomName = edtRoomName.getText().toString();
                        if(!roomName.isEmpty()){
                            dialogManager.getHopthoai().dismiss();
                            stateManager.eventManager.queue(
                                    new SetThisRoomEvent(roomName, stateManager.wifiInfo.getIP()));
                            stateManager.eventManager.queue(
                                    new TransitToWaitRoom());
                        }
                        else Toast.makeText(stateManager.activity,"Vui long nhap ten phong",
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    public void entry() {}

    @Override
    public void addRoom(int ipAddress, String roomName, int numberOfPlayers) {
        Room newRoom = new Room(ipAddress, roomName, numberOfPlayers);
        ListIterator<Room> it = data.listIterator();
        while (it.hasNext()) {
            Room room = it.next();
            if (room.getIpAddress() == ipAddress) {
                it.set(newRoom);
                return;
            }
        }

        data.add(newRoom);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void removeRoom(int ipAddress) {
        for(Room r:data){
            if(r.getIpAddress()==ipAddress){
                data.remove(r);
                adapter.notifyDataSetChanged();
                break;
            }
        }
    }
}
