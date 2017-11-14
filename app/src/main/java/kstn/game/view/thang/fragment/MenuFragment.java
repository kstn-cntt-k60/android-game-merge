package kstn.game.view.thang.fragment;


import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import kstn.game.R;
import kstn.game.logic.state_event.TransiteToLoginState;
import kstn.game.logic.state_event.TransiteToSinglePlayerState;
import kstn.game.view.state.ViewStateManager;

public class MenuFragment extends Fragment {
    private ViewStateManager stateManager;

    public MenuFragment() {
    }

    public void setStateManager(ViewStateManager stateManager) {
        this.stateManager = stateManager;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View result = inflater.inflate(R.layout.fragment_menu, container, false);
//        result.setBackgroundColor(Color.parseColor("#CC0000"));
        result.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return false;
            }
        });
        return result;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Button btnChoiThoi =(Button) view.findViewById(R.id.btnChoiThoi);
        Button btnTranhDau =(Button) view.findViewById(R.id.btnTranhDau);
        Button btnBangXepHang =(Button)view.findViewById(R.id.btnBangXepHang);

        btnChoiThoi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stateManager.eventManager.queue(new TransiteToSinglePlayerState());
            }
        });

        btnTranhDau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stateManager.eventManager.queue(new TransiteToLoginState());
            }
        });

        btnBangXepHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
            }
        });
    }
}
