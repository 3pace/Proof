package com.proof.ly.space.proof.Fragments.windows;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.proof.ly.space.proof.Adapters.RecyclerSettingsAdapter;
import com.proof.ly.space.proof.Helpers.SettingsManager;
import com.proof.ly.space.proof.Interfaces.FragmentInterface;
import com.proof.ly.space.proof.MainActivity;
import com.proof.ly.space.proof.R;

import static com.proof.ly.space.proof.Helpers.SettingsManager.currentLanguage;
import static com.proof.ly.space.proof.Helpers.SettingsManager.cycleMode;
import static com.proof.ly.space.proof.Helpers.SettingsManager.autoflip;
import static com.proof.ly.space.proof.Helpers.SettingsManager.nighmode;
import static com.proof.ly.space.proof.Helpers.SettingsManager.quesCount;

/**
 * Created by aman on 4/21/18.
 */

public class MSettingsFragment extends Fragment implements FragmentInterface {
    private RecyclerSettingsAdapter mAdapter;
    private SettingsManager mSettingsManager;
    private MainActivity mActivity;
    private Toolbar mToolbar;
    private TextView mTextViewToolbar;
    private RecyclerView mRecyclerView;

    public MSettingsFragment() {
    }
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (MainActivity) getActivity();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSettingsManager = new SettingsManager(mActivity.getApplicationContext());
        cycleMode = mSettingsManager.getCycleModeState();
        autoflip = mSettingsManager.getAutoflipState();
        nighmode = mSettingsManager.getNightmodeState();
        quesCount = mSettingsManager.getQuesCount();
        currentLanguage = mSettingsManager.langIsRussian();
        mAdapter = new RecyclerSettingsAdapter(mActivity);
        setHasOptionsMenu(true);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.settings_fragment, container, false);
    }



    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
       initViews(view);
       initObjects();
       initSetters();
       initTypeface();
       initOnClick();


    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_settings, menu);
        if (menu.getItem(0).getIcon() != null && mActivity  != null)
            menu.getItem(0).getIcon().setColorFilter(mActivity.getClickedColor(), PorterDuff.Mode.SRC_ATOP);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.update:
                mActivity.getDatabaseManager().loadDataRetrofit();
                if (mActivity != null)
                    mActivity.getDatabaseManager().checkNewDatabaseVersionFromClick();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void initViews(View itemView) {
        mToolbar = itemView.findViewById(R.id.tbar);
        mTextViewToolbar = mToolbar.findViewById(R.id.txt_tbar);
        mActivity.setSupportActionBar(mToolbar);
        mRecyclerView = itemView.findViewById(R.id.rview);
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
        mTextViewToolbar.setText(getResources().getString(R.string.tag_settings));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.addSetting(getResources().getString(R.string.cycle_mode), cycleMode);
        mAdapter.addSetting(getResources().getString(R.string.autoflipping), autoflip);
        mAdapter.addSetting(getResources().getString(R.string.question_count), quesCount);
        mAdapter.addSetting(getResources().getString(R.string.nightmode), nighmode);
        mAdapter.addSetting(getResources().getString(R.string.language), currentLanguage);
        mAdapter.setTypeface(mActivity.getTypeface());
        mAdapter.setSettingsManager(mSettingsManager);
        mAdapter.setDatabaseManager(mActivity.getDatabaseManager());
    }
}
