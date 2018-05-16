package com.proof.ly.space.proof.Fragments.windows;

import android.content.Context;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.proof.ly.space.proof.Adapters.RecyclerSettingsAdapter;
import com.proof.ly.space.proof.Helpers.SettingsManager;
import com.proof.ly.space.proof.MainActivity;
import com.proof.ly.space.proof.R;

import static com.proof.ly.space.proof.Helpers.SettingsManager.cycleMode;
import static com.proof.ly.space.proof.Helpers.SettingsManager.autoflip;
import static com.proof.ly.space.proof.Helpers.SettingsManager.nighmode;
import static com.proof.ly.space.proof.Helpers.SettingsManager.quesCount;

/**
 * Created by aman on 4/21/18.
 */

public class MSettingsFragment extends Fragment {
    private RecyclerSettingsAdapter mAdapter;
    private SettingsManager mSettingsManager;
    private Context mContext;

    public MSettingsFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSettingsManager = new SettingsManager(mContext);
        cycleMode = mSettingsManager.getCycleModeState();
        autoflip = mSettingsManager.getAutoflipState();
        nighmode = mSettingsManager.getNightmodeState();
        quesCount = mSettingsManager.getQuesCount();
        mAdapter = new RecyclerSettingsAdapter((MainActivity) getActivity());
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
        RecyclerView mRecyclerView = view.findViewById(R.id.rview);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.addSetting(getResources().getString(R.string.cycle_mode), cycleMode);
        mAdapter.addSetting(getResources().getString(R.string.autoflipping), autoflip);
        mAdapter.addSetting(getResources().getString(R.string.question_count), quesCount);
        mAdapter.addSetting(getResources().getString(R.string.nightmode), nighmode);
        mAdapter.setTypeface(((MainActivity) getActivity()).getTypeface());
        mAdapter.setSettingsManager(mSettingsManager);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
        inflater.inflate(R.menu.menu_settings, menu);
        if (menu.getItem(0).getIcon() != null && getActivity()  != null)
            menu.getItem(0).getIcon().setColorFilter(((MainActivity) getActivity()).getClickedColor(), PorterDuff.Mode.SRC_ATOP);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.update:
                if (getActivity() != null)
                    ((MainActivity) getActivity()).getmDBManager().checkNewDatabaseVersionFromClick();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }
}
