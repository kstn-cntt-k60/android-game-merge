package kstn.game.view.thang.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import kstn.game.R;
import kstn.game.view.state.ViewStateManager;

/**
 * A simple {@link Fragment} subclass.
 */
public class WaitRoomFragment extends Fragment {

    private ViewStateManager stateManager;

    public void setStateManager(ViewStateManager stateManager) {
        this.stateManager = stateManager;
    }

    public WaitRoomFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_wait_room, container, false);
    }

}