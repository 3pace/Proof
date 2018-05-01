package com.proof.ly.space.proof.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.proof.ly.space.proof.Fragments.LoginFragment;
import com.proof.ly.space.proof.Fragments.RegisterFragment;

/**
 * Created by aman on 29.03.18.
 */

public class AAVPAdapter extends FragmentPagerAdapter {
    public AAVPAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return LoginFragment.getInstance();
            case 1:
                return RegisterFragment.getInstance();
        }
        return null;
    }

    @Override
    public int getCount() {
        return 2;
    }
}
