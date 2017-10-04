package kstn.game.view.thang.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import kstn.game.R;
import kstn.game.view.thang.fragment.SlideFragment;

/**
 * Created by thang on 10/1/2017.
 */

public class MyAdapter extends FragmentPagerAdapter {
    public MyAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return SlideFragment.doiTuongMoi(R.drawable.index1);
            case 1:
                return SlideFragment.doiTuongMoi(R.drawable.index2);
            case 2:
                return SlideFragment.doiTuongMoi(R.drawable.index3);
            case 3:
                return SlideFragment.doiTuongMoi(R.drawable.index4);
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 4;// co 4 anh
    }
}
