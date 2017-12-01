package kstn.game.view.thang.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import kstn.game.R;
import kstn.game.logic.state_event.TransitToMenuState;
import kstn.game.view.state.ViewStateManager;

/**
 * A simple {@link Fragment} subclass.
 */
public class ResultFragment extends Fragment {
    private int score=10000;
    private Button btnDontSave,btnSaveScore;
    private EditText edtSaveName;
    private TextView txtSaveScore = null;
    private ViewStateManager stateManager;

    public void setStateManager(ViewStateManager stateManager) {
        this.stateManager = stateManager;
    }
    public void setScore(int s){
        score = s;
        if (txtSaveScore != null)
            txtSaveScore.setText("" + score);
    }

    public ResultFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =inflater.inflate(R.layout.fragment_result, container, false);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        btnDontSave= (Button)view.findViewById(R.id.btnDontSave);
        btnSaveScore = (Button)view.findViewById(R.id.btnSaveScore);
        edtSaveName = (EditText)view.findViewById(R.id.edtSaveName);
        txtSaveScore = (TextView)view.findViewById(R.id.txtSaveScore);
        txtSaveScore.setText("" + score);

        btnSaveScore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String name = edtSaveName.getText().toString();
                if(!name.isEmpty()){
                    //TODO

                    stateManager.eventManager.queue(new TransitToMenuState());
                }else Toast.makeText(getActivity(),"Nhập tên để lưu điểm",Toast.LENGTH_SHORT).show();
            }
        });
        btnDontSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stateManager.eventManager.queue(new TransitToMenuState());
            }
        });

    }


}
