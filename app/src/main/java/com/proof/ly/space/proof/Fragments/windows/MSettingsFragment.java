package com.proof.ly.space.proof.Fragments.windows;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
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
    private RecyclerView rview;
    private RecyclerSettingsAdapter adapter;
    private SettingsManager settingsManager;

    public MSettingsFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        settingsManager = new SettingsManager(getContext());
        cycleMode = settingsManager.getCycleModeState();
        autoflip = settingsManager.getAutoflipState();
        nighmode = settingsManager.getNightmodeState();
        quesCount = settingsManager.getQuesCount();
        adapter = new RecyclerSettingsAdapter(getActivity());
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
        rview = view.findViewById(R.id.rview);
        rview.setLayoutManager(new LinearLayoutManager(getContext()));
        rview.setAdapter(adapter);
        adapter.addSetting(getResources().getString(R.string.cycle_mode), cycleMode);
        adapter.addSetting(getResources().getString(R.string.autoflipping), autoflip);
        adapter.addSetting(getResources().getString(R.string.question_count), quesCount);
        adapter.addSetting(getResources().getString(R.string.nightmode), nighmode);
        adapter.setTypeface(((MainActivity) getActivity()).getTypeface());
        adapter.setSettingsManager(settingsManager);
    }

    @Override
    public void onPrepareOptionsMenu(Menu menu) {
        menu.clear();
    }
}
