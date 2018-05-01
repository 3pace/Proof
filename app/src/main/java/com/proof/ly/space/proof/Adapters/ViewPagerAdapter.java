package com.proof.ly.space.proof.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import com.proof.ly.space.proof.Fragments.ResultFragment;
import com.proof.ly.space.proof.Fragments.StartFragment;
import com.proof.ly.space.proof.Fragments.TestingFragment;

/**
 * Created by aman on 3/12/18.
 */

public class ViewPagerAdapter extends FragmentStatePagerAdapter {

    private int count = 0;
    public ViewPagerAdapter(FragmentManager fm,int count) {
        super(fm);
        this.count = count;
    }

    @Override
    public Fragment getItem(int position) {

        if (position==0) {
            return StartFragment.getInstance(position);
        }
        if (position==count+1 && count > 2) {
            return ResultFragment.getInstance(position);
        }
        else if (position <= count) {
            return TestingFragment.getInstance(position-1);


        }
        return StartFragment.getInstance(position);
    }

    public void setCount(int count) {
        this.count = count;
        notifyDataSetChanged();
    }

    @Override
    public int getItemPosition(Object object) {
        return POSITION_NONE;
    }

    @Override
    public int getCount() {
        return count+2;
    }

    @Override
    public CharSequence getPageTitle(int position) {

        return super.getPageTitle(position);
    }


}
