package com.github.skittlesdev.kubrick.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import com.github.skittlesdev.kubrick.ui.fragments.FragmentHome;
import com.github.skittlesdev.kubrick.ui.fragments.TimelineFragment;

import java.util.LinkedList;
import java.util.List;

public class HomePagerAdapter extends FragmentPagerAdapter {
    private List<Fragment> fragments;
    private List<CharSequence> titles;

    public HomePagerAdapter(FragmentManager fm) {
        super(fm);
        this.fragments = new LinkedList<>();
        this.titles = new LinkedList<>();

        this.fragments.add(new FragmentHome());
        this.titles.add("Home");

        this.fragments.add(new TimelineFragment());
        this.titles.add("Timeline");
    }

    @Override
    public Fragment getItem(int position) {
        return this.fragments.get(position);
    }

    @Override
    public int getCount() {
        return this.fragments.size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return this.titles.get(position);
    }
}
