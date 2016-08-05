package com.uwimonacs.fstmobile.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v7.app.AppCompatActivity;

import com.uwimonacs.fstmobile.fragments.InfoFragment;
import com.uwimonacs.fstmobile.fragments.PlacesFragment;
import com.uwimonacs.fstmobile.fragments.NewsFragment;

public class TabPagerAdapter extends FragmentPagerAdapter {
    private static final CharSequence[] TAB_TITLES = {"News", "Info", "Places"};
    private AppCompatActivity activity;

    public TabPagerAdapter(FragmentManager fm, AppCompatActivity activity) {
        super(fm);
        this.activity = activity;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                NewsFragment newsFragment = new NewsFragment();
                newsFragment.setActivity(activity);
                return newsFragment;
            case 1:
                return new InfoFragment();
            case 2:
                return new PlacesFragment();
        }
        return null;
    }

    @Override
    public int getCount() {
        return TAB_TITLES.length;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return TAB_TITLES[position];
    }
}
