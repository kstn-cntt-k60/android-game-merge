package kstn.game.view.thang.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import kstn.game.R;
import kstn.game.view.state.multiplayer.GameResultInfo;
import kstn.game.view.thang.adapter.ResultMultiAdapter;
import kstn.game.view.thang.model.Player;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 */
public class ResultMultiPlayerFragment extends Fragment {
    private List<Player> data = new ArrayList<>();
    private ResultMultiAdapter adapter;
    private GameResultInfo gameResultInfo;

    public void setGameResultInfo(GameResultInfo gameResultInfo) {
        this.gameResultInfo = gameResultInfo;
    }

    public ResultMultiPlayerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_result_multi_player, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ListView lvWinner = view.findViewById(R.id.lvWinner);
        data = gameResultInfo.getPlayerList();
        adapter = new ResultMultiAdapter(data,getActivity());
        lvWinner.setAdapter(adapter);

    }
}
