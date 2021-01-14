package com.example.jobfinder.login.fragments;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;


public class LoginPageAdapter extends FragmentPagerAdapter {


    public LoginPageAdapter(FragmentManager fm) {
        super(fm, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            default:
            case 0:
                return new SignInFragment();
            case 1:
                return new CreateFragment();

        }
    }

    @Override
    public int getCount() {
        return 2;
    }
}

