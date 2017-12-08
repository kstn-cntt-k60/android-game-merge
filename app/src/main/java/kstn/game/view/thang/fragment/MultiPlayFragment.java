package kstn.game.view.thang.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
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
import kstn.game.logic.cone.ConeResult;
import kstn.game.logic.event.EventData;
import kstn.game.logic.event.EventListener;
import kstn.game.logic.playing_event.ConeResultEvent;
import kstn.game.logic.playing_event.NextQuestionEvent;
import kstn.game.logic.playing_event.PlayingEventType;
import kstn.game.logic.state.multiplayer.Player;
import kstn.game.view.state.ViewStateManager;
import kstn.game.view.state.multiplayer.IPlayerManager;
import kstn.game.view.state.singleplayer.CharCellManager;
import kstn.game.view.state.singleplayer.KeyboardManager;
import kstn.game.view.thang.adapter.MultiAdapter;

public class MultiPlayFragment extends Fragment implements IPlayerManager{
    private int currentPlayerIndex =0;
    private Button btnDoan;
    private TextView txtNoiDungKim;
    private TextView txtCauHoi;
    private ArrayList<Player> data = new ArrayList<>();
    private MultiAdapter adapter;
    private GridView gv;

    int height;
    View guessView, giveAnswerView;
    private Animation scaleAnimation;

    private ViewStateManager stateManager;
    private CharCellManager charCellManager;
    private  KeyboardManager keyboardManager;

    // Listeners
    private EventListener rotateResultListener;
    private EventListener nextQuestionListener;

    public MultiPlayFragment() {
        // Required empty public constructor
        rotateResultListener = new EventListener() {
            @Override
            public void onEvent(EventData event_) {
                ConeResultEvent event = (ConeResultEvent)event_;
                handleRotateResult(event.getResult());
            }
        };

        nextQuestionListener = new EventListener() {
            @Override
            public void onEvent(EventData event_) {
                NextQuestionEvent event = (NextQuestionEvent)event_;
                handleNextQuestion(event.getQuestion(), event.getAnswer());
            }
        };
    }

    public void setCurrentPlayerIndex(int currentPlayerIndex) {
        this.currentPlayerIndex = currentPlayerIndex;
    }

    public void setCharCellManager(CharCellManager charCellManager) {
        this.charCellManager = charCellManager;
    }

    public void setKeyboardManager(KeyboardManager keyboardManager) {
        this.keyboardManager = keyboardManager;
    }

    public void setStateManager(ViewStateManager stateManager) {
        this.stateManager = stateManager;
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
        charCellManager.onViewCreated(view);
        // grid view
        gv = (GridView) view.findViewById(R.id.gv);

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
        for(int i=0;i<num;i++){
            data.add(new Player());
        }
        data.get(0).setIdColor(Color.YELLOW);
        adapter = new MultiAdapter(data,getActivity());
        gv.setAdapter(adapter);
        adapter.notifyDataSetChanged();
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
        currentPlayerIndex = playerIndex;
        data.get(playerIndex).setIdColor(Color.YELLOW);
        for(int i=0;i<data.size();i++){
            if(i!=playerIndex)
               data.get(i).setIdColor(Color.parseColor("#752c74"));
        }
        adapter.notifyDataSetChanged();

    }

    @Override
    public void setScore(int value) {
        data.get(currentPlayerIndex).setScore(value);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void deactivatePlayer(int playerIndex){
            data.get(playerIndex).setIdColor(Color.parseColor("#FF66655F"));
            adapter.notifyDataSetChanged();
    }

    @Override
    public void activatePlayer(int playerIndex) {
            data.get(playerIndex).setIdColor(Color.parseColor("#752c74"));
            adapter.notifyDataSetChanged();
    }

    public void entry(){
        currentPlayerIndex =0;
        stateManager.eventManager.addListener(PlayingEventType.CONE_RESULT, rotateResultListener);
        stateManager.eventManager.addListener(PlayingEventType.NEXT_QUESTION, nextQuestionListener);
    }

    public void exit(){
        stateManager.eventManager.removeListener(PlayingEventType.NEXT_QUESTION, nextQuestionListener);
        stateManager.eventManager.removeListener(PlayingEventType.CONE_RESULT, rotateResultListener);
    }

    public void handleNextQuestion(String question, String answer) {
        txtCauHoi.setText(question);
    }

    private void handleRotateResult(int result) {
        final String text = ConeResult.getString(result);
        scaleAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                txtNoiDungKim.setText(text);
            }

            @Override
            public void onAnimationEnd(Animation animation) {}

            @Override
            public void onAnimationRepeat(Animation animation) {}
        });
        txtNoiDungKim.startAnimation(scaleAnimation);
    }
}
