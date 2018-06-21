package com.proof.ly.space.proof.Fragments.windows;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.proof.ly.space.proof.Adapters.QMVPAdapter;
import com.proof.ly.space.proof.CustomViews.MViewPager;
import com.proof.ly.space.proof.Interfaces.FragmentInterface;
import com.proof.ly.space.proof.MainActivity;
import com.proof.ly.space.proof.R;

/**
 * Created by aman on 4/16/18.
 */

public class MQuestionManagerFragment extends Fragment implements FragmentInterface{
    private static MViewPager mViewPager;
    private QMVPAdapter mAdapter;
    private static Handler mHandler;
    private MainActivity mActivity;
    private Toolbar mToolbar;
    private TextView mTextViewToolbar;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (MainActivity) context;
    }

    public MQuestionManagerFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdapter = new QMVPAdapter(getChildFragmentManager());
        mHandler = new Handler();


    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.qmanager_fragment,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        initObjects();
        initTypeface();
        initSetters();
        initOnClick();

    }

    @Override
    public void initViews(View itemView) {
        mToolbar = itemView.findViewById(R.id.tbar);
        mTextViewToolbar = mToolbar.findViewById(R.id.txt_tbar);
        mActivity.setSupportActionBar(mToolbar);
        mViewPager = itemView.findViewById(R.id.vpager);
    }

    @Override
    public void initTypeface() {
        Typeface typeface = mActivity.getTypeface();
        mTextViewToolbar.setTypeface(typeface);
    }

    @Override
    public void initOnClick() {

    }

    @Override
    public void initObjects() {

    }

    @Override
    public void initSetters() {
        mTextViewToolbar.setText(getResources().getString(R.string.tag_qmanager));
        mViewPager.setAdapter(mAdapter);
        mViewPager.setAllowedSwipeDirection(MViewPager.SwipeDirection.none);
    }

    public static void nextPage(){
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mViewPager.setCurrentItem(mViewPager.getCurrentItem()+1);
            }
        },400);

    }
    public static void nextPageFast(){
        mViewPager.setCurrentItem(mViewPager.getCurrentItem()+1);
    }
}
