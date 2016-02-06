package com.example.hii.smarteducation;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

/**
 * Created by Saylani_IT on 18/10/2015.
 */
public class ViewPagerAdapter extends FragmentStatePagerAdapter {
    String[] mTitles;

    public ViewPagerAdapter(FragmentManager fm, int NumOfTabs, String[] titles) {
        super(fm);
        mTitles = titles;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return new TodoFragment();
            case 1:
                return new ProgressFragment();
            case 2:
                return new DoneFragment();
        }

        return null;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return mTitles[position];
    }

    @Override
    public int getCount() {
        return mTitles.length;
    }
}
