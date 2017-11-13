package kstn.game.view.thang.fragment;


import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import kstn.game.MainActivity;
import kstn.game.R;
import kstn.game.logic.event.EventData;
import kstn.game.logic.event.EventListener;
import kstn.game.logic.event.EventManager;
import kstn.game.view.state.ViewStateManager;


/**
 * A simple {@link Fragment} subclass.
 */
public class MenuFragment extends Fragment {
    static public EventManager uiEventManager = null;
    private MediaPlayer song;
    private ViewStateManager stateManager;

    public MenuFragment() {

    }

    public void setStateManager(ViewStateManager stateManager) {
        this.stateManager = stateManager;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View result = inflater.inflate(R.layout.fragment_menu, container, false);
        result.setBackgroundColor(Color.parseColor("#00000000"));
        result.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return false;
            }
        });
        return result;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        song = MediaPlayer.create(getActivity(), R.raw.nhac_hieu);

        Button btnChoiThoi =(Button) view.findViewById(R.id.btnChoiThoi);
        Button btnTranhDau =(Button) view.findViewById(R.id.btnTranhDau);
        Button btnBangXepHang =(Button)view.findViewById(R.id.btnBangXepHang);


        btnChoiThoi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                song.stop();
                ((MainActivity) getActivity()).addFragment(R.id.myLayout, new PlayFragment());
            }
        });

        btnTranhDau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                song.stop();
                ((MainActivity) getActivity()).addFragment(R.id.myLayout, new LoginFragment());
            }
        });

        btnBangXepHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                song.stop();
                ((MainActivity) getActivity()).addFragment(R.id.myLayout, BFragment.newObj("Hello BXH"));
            }
        });
    }

    @Override
    public void onPause() {
        song.stop();
        super.onPause();
    }
    @Override
    public void onStart() {
        song.start();
        super.onStart();
    }
}
