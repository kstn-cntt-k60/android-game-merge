package kstn.game.view.thang.fragment;


import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

import kstn.game.MainActivity;
import kstn.game.R;
import kstn.game.view.thang.activity.PlayActivity;


/**
 * A simple {@link Fragment} subclass.
 */
public class AFragment extends Fragment {
    ImageView img;

    public AFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View result = inflater.inflate(R.layout.fragment_a, container, false);
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
        final MediaPlayer song = MediaPlayer.create(getActivity(), R.raw.nhac_hieu);
        song.start();
        Button btnChoiThoi =(Button) view.findViewById(R.id.btnChoiThoi);
        Button btnTranhDau =(Button) view.findViewById(R.id.btnTranhDau);
        Button btnBangXepHang =(Button)view.findViewById(R.id.btnBangXepHang);

        btnChoiThoi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                song.stop();
                Intent i = new Intent(getActivity(), PlayActivity.class);
                startActivity(i);
            }
        });
        btnTranhDau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                song.stop();
                ((MainActivity) getActivity()).AddFragment(R.id.myLayout, BFragment.newObj("Hello Tranh Dau"));
            }
        });
        btnBangXepHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                song.stop();
                ((MainActivity) getActivity()).AddFragment(R.id.myLayout, BFragment.newObj("Hello BXH"));
            }
        });
    }
}
