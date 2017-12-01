package kstn.game.view.thang.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import kstn.game.MainActivity;
import kstn.game.R;
import kstn.game.logic.data.ManagerDAO;
import kstn.game.logic.state.multiplayer.ScoreDb;
import kstn.game.view.thang.adapter.RankingAdapter;

/**
 * A simple {@link Fragment} subclass.
 */
public class RankingFragment extends Fragment {


    public RankingFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.ranking, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ListView lvRanking = view.findViewById(R.id.lvRanking);
        TextView txtNameRank1 = (TextView) view.findViewById(R.id.txtNameRank1);
        TextView txtNameRank2 = (TextView) view.findViewById(R.id.txtNameRank2);
        TextView txtNameRank3 = (TextView) view.findViewById(R.id.txtNameRank3);
        TextView txtScoreR1 = (TextView) view.findViewById(R.id.txtScoreR1);
        TextView txtScoreR2 = (TextView) view.findViewById(R.id.txtScoreR2);
        TextView txtScoreR3 = (TextView) view.findViewById(R.id.txtScoreR3);
        ManagerDAO rankingManager = new ManagerDAO((MainActivity)getActivity());
        rankingManager.open();
        ArrayList<ScoreDb> data = rankingManager.getRanking();
        RankingAdapter adapter = new RankingAdapter(getActivity(),data);
        lvRanking.setAdapter(adapter);
        txtNameRank1.setText(data.get(0).getName());
        txtNameRank2.setText(data.get(1).getName());
        txtNameRank3.setText(data.get(2).getName());
        txtScoreR1.setText(data.get(0).getScore()+"");
        txtScoreR2.setText(data.get(1).getScore()+"");
        txtScoreR3.setText(data.get(2).getScore()+"");


    }
}
