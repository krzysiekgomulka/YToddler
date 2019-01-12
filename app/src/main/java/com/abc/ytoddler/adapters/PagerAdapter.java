package com.abc.ytoddler.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.abc.ytoddler.fragments.ChannelFragment;
import com.abc.ytoddler.fragments.LiveFragment;
import com.abc.ytoddler.fragments.PlayListFragment;

public class PagerAdapter extends FragmentStatePagerAdapter {
    private int mNumberOfTabs;

    public PagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumberOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                ChannelFragment tab1 = new ChannelFragment();
                return tab1;
            case 1:
                PlayListFragment tab2 = new PlayListFragment();
                return tab2;
            case 2:
                LiveFragment tab3 = new LiveFragment();
                return tab3;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumberOfTabs;
    }
}

