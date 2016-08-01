package com.uwimonacs.fstmobile.adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.uwimonacs.fstmobile.fragments.InfoFragment;
import com.uwimonacs.fstmobile.fragments.PlacesFragment;
import com.uwimonacs.fstmobile.fragments.NewsFragment;

public class TabPagerAdapter extends FragmentPagerAdapter {
    private static final CharSequence[] TAB_TITLES = {"News", "Info", "Places"};

    public TabPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new NewsFragment();
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
