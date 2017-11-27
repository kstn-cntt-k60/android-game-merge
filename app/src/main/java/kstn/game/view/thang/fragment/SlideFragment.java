package kstn.game.view.thang.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import kstn.game.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class SlideFragment extends Fragment {
   private int idAnh;
    private ImageView img;

    public SlideFragment() {
        // Required empty public constructor
    }
    public static SlideFragment newInstance(int idAnh) {
        SlideFragment sildeFragment = new SlideFragment();
        Bundle bd = new Bundle();
        bd.putInt("idAnh",idAnh);
        sildeFragment.setArguments(bd);;
        return sildeFragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_slide, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        img = (ImageView) view.findViewById(R.id.img);
        Bundle bd = getArguments();
        idAnh=bd.getInt("idAnh");
        img.setImageResource(idAnh);
    }


}
