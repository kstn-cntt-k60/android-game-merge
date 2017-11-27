package kstn.game.view.thang.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import kstn.game.R;
import kstn.game.view.thang.fragment.SlideFragment;

public class SlideAdapter extends FragmentPagerAdapter {
    public SlideAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position%4){
            case 0:
                return SlideFragment.newInstance(R.drawable.index1);
            case 1:
                return SlideFragment.newInstance(R.drawable.index2);
            case 2:
                return SlideFragment.newInstance(R.drawable.index3);
            case 3:
                return SlideFragment.newInstance(R.drawable.index4);
            default:
                return SlideFragment.newInstance(R.drawable.index1);
        }
    }

    @Override
    public int getCount() {
        return 4;// co 4 anh
    }
}
