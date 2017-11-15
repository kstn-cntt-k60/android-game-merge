package kstn.game.view.state.singleplayer;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import kstn.game.R;
import kstn.game.view.state.ViewStateManager;

public class SinglePlayerFragment extends Fragment {
    private ViewStateManager stateManager;
    private List<String> coneCells;

    void initConeCells() {
        coneCells = new ArrayList<>(20);
        coneCells.add("800");
        coneCells.add("Mất điểm");
        coneCells.add("100");
        coneCells.add("200");
        coneCells.add("Nhân 2");
        coneCells.add("300");
        coneCells.add("400");
        coneCells.add("May Mắn");
        coneCells.add("300");
        coneCells.add("700");
        coneCells.add("Mất Lượt");
        coneCells.add("600");
        coneCells.add("Chia 2");
        coneCells.add("500");
        coneCells.add("100");
        coneCells.add("Thêm Lượt");
        coneCells.add("200");
        coneCells.add("300");
        coneCells.add("Thưởng");
        coneCells.add("900");
    }

    public SinglePlayerFragment() {}

    public void setStateManager(ViewStateManager stateManager) {
        this.stateManager = stateManager;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View result = inflater.inflate(R.layout.fragment_play, container, false);
        result.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                return false;
            }
        });
        return result;
    }

}
