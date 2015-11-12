package com.github.skittlesdev.kubrick.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.github.skittlesdev.kubrick.ui.fragments.FragmentHome;
import com.github.skittlesdev.kubrick.ui.fragments.TimelineFragment;

public class HomePagerAdapter extends FragmentPagerAdapter {
    public static final int NUM_ITEMS = 2;

    public HomePagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return new FragmentHome();
        }

        if (position == 1) {
            return new TimelineFragment();
        }

        return null;
    }

    @Override
    public int getCount() {
        return NUM_ITEMS;
    }
}
