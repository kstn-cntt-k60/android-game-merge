package kstn.game.view.thang.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

import kstn.game.R;
import kstn.game.logic.playing_event.player.SetThisPlayerEvent;
import kstn.game.logic.state_event.TransitToCreatedRoomsState;
import kstn.game.view.state.ViewStateManager;
import kstn.game.view.thang.adapter.ViewPaperAdapter;

public class LoginFragment extends Fragment {
    private ViewStateManager stateManager;
    private Random random = new Random();

    public void setStateManager(ViewStateManager stateManager) {
        this.stateManager = stateManager;
    }

    public LoginFragment() {}


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.fragment_login, container, false);
        //v.setBackgroundColor(Color.parseColor("#3F51B5"));
        return v;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        final ArrayList<Integer> data = new ArrayList<>();
        data.add(R.drawable.index1);
        data.add(R.drawable.index2);
        data.add(R.drawable.index3);
        data.add(R.drawable.index4);
        final EditText nameEditText = (EditText) view.findViewById(R.id.ten);
        final ViewPager vpPager = view.findViewById(R.id.vpPager);
        ViewPaperAdapter adapter = new ViewPaperAdapter(data,getContext());
        vpPager.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        Button btnDau = (Button) view.findViewById(R.id.btnDau);
        btnDau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int avatarId = data.get(vpPager.getCurrentItem());
                String playerName;
                if (nameEditText.getText().toString().isEmpty()) {
                    playerName = "GUEST" + random.nextInt(100);
                }
                else {
                    playerName = nameEditText.getText().toString();
                }
                Toast.makeText(getActivity(), playerName,Toast.LENGTH_SHORT).show();
                stateManager.eventManager.queue(new SetThisPlayerEvent(playerName, avatarId));
                stateManager.eventManager.queue(new TransitToCreatedRoomsState());
            }
        });
    }
}
