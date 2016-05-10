package com.example.assignmentfour;

/**
 * Created by dade on 10/02/16.
 */

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v13.app.FragmentPagerAdapter;

public class ProfileAdapter extends FragmentPagerAdapter {
    public ProfileAdapter(FragmentManager mgr) {
        super(mgr);
    }

    @Override
    public int getCount() {
        return (MainFragment.db.getCount());
    }

    @Override
    public Fragment getItem(int position) {
        return (ProfileFragment.newInstance(position));
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }
    //Inspired by: http://stackoverflow.com/questions/7263291/viewpager-pageradapter-not-updating-the-view
}
