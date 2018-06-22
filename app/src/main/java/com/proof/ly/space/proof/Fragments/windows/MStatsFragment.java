package com.proof.ly.space.proof.Fragments.windows;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.proof.ly.space.proof.CustomViews.graphview.MGraphBar;
import com.proof.ly.space.proof.Data.Progress;
import com.proof.ly.space.proof.Data.StatsData;
import com.proof.ly.space.proof.Helpers.DBManager;
import com.proof.ly.space.proof.Interfaces.FragmentInterface;
import com.proof.ly.space.proof.MainActivity;
import com.proof.ly.space.proof.R;

import java.util.ArrayList;

public class MStatsFragment extends Fragment implements FragmentInterface {

    private TextView mTextViewEmptyStats;
    private MGraphBar mGraphBar;
    private ArrayList<StatsData> mArrayListStatsData;
    private final int MAX = 40;
    private MainActivity mActivity;
    private DBManager mDatabaseManager;
    private boolean mIsEmpty = false;
    private Toolbar mToolbar;
    private TextView mTextViewToolbar;

    public MStatsFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (MainActivity) getActivity();

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDatabaseManager = mActivity.getDatabaseManager();
        mArrayListStatsData = mDatabaseManager.getStats();

        mIsEmpty = mArrayListStatsData.size() <= 0;


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.stats_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        initObjects();
        initViews(view);
        initSetters();
        initTypeface();
        initOnClick();


    }

    @Override
    public void initViews(View itemView) {
        mToolbar = itemView.findViewById(R.id.tbar);
        mTextViewToolbar = mToolbar.findViewById(R.id.txt_tbar);
        mActivity.setSupportActionBar(mToolbar);
        mGraphBar = itemView.findViewById(R.id.graph_bar);
        mTextViewEmptyStats = itemView.findViewById(R.id.textViewEmptyStats);
    }

    @Override
    public void initTypeface() {
        Typeface typeface = mActivity.getTypeface();
        mTextViewToolbar.setTypeface(typeface);
        mGraphBar.setTypeface(mActivity.getTypeface());
        mTextViewEmptyStats.setTypeface(typeface);
    }

    @Override
    public void initOnClick() {

    }

    @Override
    public void initObjects() {

    }

    @Override
    public void initSetters() {
        mTextViewToolbar.setText(getResources().getString(R.string.tag_stats));
        mGraphBar.setMaxPoints(MAX);
        mGraphBar.setVerticalLineColor(mActivity.getDisabledColor());
        mGraphBar.setTextColor(mActivity.getClickedColor());
        mGraphBar.setDotColor(getResources().getColor(R.color.colorPrimary));
        mGraphBar.setHorizontalLineColor(mActivity.getClickedColor());
        mTextViewEmptyStats.setVisibility(mIsEmpty ? View.VISIBLE : View.GONE);

        mGraphBar.setDataList(mArrayListStatsData);
    }
}
