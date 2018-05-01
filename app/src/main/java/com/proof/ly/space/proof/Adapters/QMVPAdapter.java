package com.proof.ly.space.proof.Adapters;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.proof.ly.space.proof.Fragments.AddAnswersFragment;
import com.proof.ly.space.proof.Fragments.AddQAConfirmFragment;
import com.proof.ly.space.proof.Fragments.AddQuestionFragment;
import com.proof.ly.space.proof.Fragments.MyQuestionFragment;

/**
 * Created by aman on 25.03.18.
 */

public class QMVPAdapter extends FragmentPagerAdapter {

    public QMVPAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 0:
                return MyQuestionFragment.getInstance();
            case 1:
                return AddQuestionFragment.getInstance();
            case 2:
                return AddAnswersFragment.getInstance();
            case 3:
                return AddQAConfirmFragment.getInstance();

        }
        return null;
    }

    @Override
    public int getCount() {
        return 4;
    }
}
