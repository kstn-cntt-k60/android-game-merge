package kstn.game.view.thang.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.GridView;
import android.widget.TextView;

import java.util.ArrayList;

import kstn.game.R;
import kstn.game.logic.state.multiplayer.Player;
import kstn.game.view.state.ViewStateManager;
import kstn.game.view.state.multiplayer.IPlayerManager;
import kstn.game.view.state.singleplayer.CharCellManager;
import kstn.game.view.state.singleplayer.KeyboardManager;
import kstn.game.view.state.singleplayer.SongManager;
import kstn.game.view.thang.adapter.MultiAdapter;

public class MutiPlayFragment extends Fragment implements IPlayerManager{
    private Button btnDoan;
    private TextView txtNoiDungKim;
    private TextView txtCauHoi;
    private ArrayList<Player> data;
    private MultiAdapter adapter;
    int height;
    View guessView, giveAnswerView;
    private Animation scaleAnimation;
    private ViewStateManager stateManager;
    private SongManager songManager;
    private CharCellManager charCellManager;
    private  KeyboardManager keyboardManager;

    public MutiPlayFragment() {
        // Required empty public constructor
        songManager = new SongManager(stateManager);
        charCellManager = new CharCellManager(stateManager);
        keyboardManager = new KeyboardManager(stateManager);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_muti_play, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        songManager.onViewCreated(view);
        charCellManager.onViewCreated(view);

        // grid view
        GridView gv = (GridView) view.findViewById(R.id.gv);
         adapter = new MultiAdapter(data,getActivity());
        adapter.notifyDataSetChanged();
        // lay tham so man hinh
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        // anh xa
        txtCauHoi = view.findViewById(R.id.txtCauHoi);
        btnDoan = view.findViewById(R.id.btnDoan);
        txtNoiDungKim = view.findViewById(R.id.txtNoiDungKim);
        // alert
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        guessView = inflater.inflate(R.layout.giaodien_alert_doan, null);
        giveAnswerView = inflater.inflate(R.layout.giaodien_alert_letter, null);

        keyboardManager.onViewGuessKeyboard(guessView, height);
        keyboardManager.onViewGiveAnswer(giveAnswerView, height);

        btnDoan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                keyboardManager.requestGuess();
            }
        });

        scaleAnimation = AnimationUtils.loadAnimation(getActivity(),R.anim.scale);
    }

    @Override
    public void setNumberPlayer(int num) {
            data = new ArrayList<>();
            for(int i=0;i<num;i++){
                data.add(new Player());
            }
    }

    @Override
    public void setAvatar(int playerIndex, int avatarId) {
            data.get(playerIndex).setAvatarId(avatarId);
            adapter.notifyDataSetChanged();
    }

    @Override
    public void setName(int playerIndex, String name) {
        data.get(playerIndex).setName(name);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void nextPlayer(int playerIndex) {

    }

    @Override
    public void setScore(int value) {

    }

    @Override
    public void deactivatePlayer(int playerIndex) {

    }
}
