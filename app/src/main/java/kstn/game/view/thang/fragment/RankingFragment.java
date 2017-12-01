package kstn.game.view.thang.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import kstn.game.R;
import kstn.game.logic.data.QuestionManagerDAO;

/**
 * A simple {@link Fragment} subclass.
 */
public class RankingFragment extends Fragment {


    public RankingFragment() {
        // Required empty public constructor
    }

//    public static RankingFragment newObj (String noidung){
//        RankingFragment fragmentB = new RankingFragment();
//        Bundle bd = new Bundle();
//        bd.putString("noidung",noidung);
//        fragmentB.setArguments(bd); // luu gia tri
//        return fragmentB;
//    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_b, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        TextView txt = (TextView) view.findViewById(R.id.txt);
//        String noidungCanLay = getArguments().getString("noidung","");
//        txt.setText(noidungCanLay);
        QuestionManagerDAO questionManager = new QuestionManagerDAO(getActivity(),"CauHoiDataBase1.sqlite");
    }
}
