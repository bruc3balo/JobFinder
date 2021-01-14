package com.example.jobfinder.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.jobfinder.fragments.job.JobMarketFragment;
import com.example.jobfinder.fragments.notifications.NotificationsFragment;
import com.example.jobfinder.fragments.profile.ProfileFragment;


public class MainPageAdapter extends FragmentPagerAdapter {


    public MainPageAdapter(FragmentManager fm) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            default:
            case 0:
                return new ProfileFragment();
            case 1:
                return new JobMarketFragment();
            case 2:
                return new NotificationsFragment();
        }
    }

    @Override
    public int getCount() {
        return 3;
    }
}

