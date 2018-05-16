package com.proof.ly.space.proof.Adapters;

import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.proof.ly.space.proof.Data.SettingData;
import com.proof.ly.space.proof.Fragments.windows.MTestingFragment;
import com.proof.ly.space.proof.Helpers.SettingsManager;
import com.proof.ly.space.proof.MainActivity;
import com.proof.ly.space.proof.R;

import java.util.ArrayList;

import static com.proof.ly.space.proof.Helpers.SettingsManager.cycleMode;
import static com.proof.ly.space.proof.Helpers.SettingsManager.autoflip;
import static com.proof.ly.space.proof.Helpers.SettingsManager.nighmode;
import static com.proof.ly.space.proof.Helpers.SettingsManager.quesCount;

/**
 * Created by aman on 4/22/18.
 */

public class RecyclerSettingsAdapter extends RecyclerView.Adapter<RecyclerSettingsAdapter.ItemHolder> {
    private ArrayList<SettingData> mTitleList = new ArrayList<>();
    private Typeface mTypeface;
    private SettingsManager mSettingsManager;
    private MainActivity mMainActivity;


    public RecyclerSettingsAdapter(MainActivity activity) {
        this.mMainActivity = activity;
    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.settings_item_container, parent, false));
    }

    @Override
    public void onBindViewHolder(ItemHolder holder, int position) {
        Resources resources = holder.mResources;
        SettingData settingData = mTitleList.get(position);
        holder.mTextViewSettingTitle.setText(settingData.getTitle());
        if (position == 3 /*2 == ques count pos*/) {
            if (settingData.getState()) {
                holder.mButtonSettingAction.setText(resources.getText(R.string.night));
                holder.mButtonSettingAction.setTextColor(resources.getColor(R.color.colorPrimaryDark));
            } else {
                holder.mButtonSettingAction.setText(resources.getText(R.string.day));
                holder.mButtonSettingAction.setTextColor(resources.getColor(R.color.colorAccent));
            }
        }
        else if (position == 2){
            if (settingData.getState()) {
                holder.mButtonSettingAction.setText(String.valueOf(MTestingFragment.BIGI));
                holder.mButtonSettingAction.setTextColor(resources.getColor(R.color.colorPrimaryDark));
            } else {
                holder.mButtonSettingAction.setText(String.valueOf(MTestingFragment.MINI));
                holder.mButtonSettingAction.setTextColor(resources.getColor(R.color.colorAccent));
            }
        }else {
            if (settingData.getState()) {
                holder.mButtonSettingAction.setText(resources.getText(R.string.settings_on));
                holder.mButtonSettingAction.setTextColor(resources.getColor(R.color.colorPrimaryDark));
            } else {
                holder.mButtonSettingAction.setText(resources.getText(R.string.settings_off));
                holder.mButtonSettingAction.setTextColor(resources.getColor(R.color.colorAccent));
            }
        }

    }

    @Override
    public int getItemCount() {
        return mTitleList.size();
    }

    class ItemHolder extends RecyclerView.ViewHolder {
        private TextView mTextViewSettingTitle;
        private Button mButtonSettingAction;
        private Resources mResources;


        ItemHolder(View itemView) {
            super(itemView);
            mResources = itemView.getResources();
            mTextViewSettingTitle = itemView.findViewById(R.id.txt_settings);
            mButtonSettingAction = itemView.findViewById(R.id.btn_settings);
            mTextViewSettingTitle.setTypeface(mTypeface);
            mButtonSettingAction.setTypeface(mTypeface);

            if (mSettingsManager != null)
                mButtonSettingAction.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switch (getAdapterPosition()) {
                            case 0:
                                cycleMode = !cycleMode;
                                mSettingsManager.saveModeState(cycleMode);
                                mTitleList.get(getAdapterPosition()).setState(cycleMode);
                                break;
                            case 1:
                                autoflip = !autoflip;
                                mSettingsManager.saveAutoflipState(autoflip);
                                mTitleList.get(getAdapterPosition()).setState(autoflip);
                                break;
                            case 2:
                                quesCount = !quesCount;
                                mSettingsManager.saveQuesCount(quesCount);
                                mTitleList.get(getAdapterPosition()).setState(quesCount);
                                break;
                            case 3:
                                nighmode = !nighmode;
                                mSettingsManager.saveNightmodeState(nighmode);
                                mTitleList.get(getAdapterPosition()).setState(nighmode);
                                if (mMainActivity != null) {
                                    mMainActivity.startActivity(new Intent(mMainActivity, MainActivity.class));
                                    mMainActivity.finish();
                                }

                                break;

                        }
                        notifyDataSetChanged();

                    }
                });


        }
    }

    public void addSetting(String title, boolean state) {
        mTitleList.add(new SettingData(title, state));
        notifyDataSetChanged();
    }


    public void setTypeface(Typeface typeface) {
        this.mTypeface = typeface;
    }

    public void setSettingsManager(SettingsManager settingsManager) {
        this.mSettingsManager = settingsManager;
    }
}
