package com.proof.ly.space.proof.Adapters;

import android.app.Activity;
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
    private ArrayList<SettingData> titleList = new ArrayList<>();
    private Typeface typeface;
    private SettingsManager settingsManager;
    private Activity activity;


    public RecyclerSettingsAdapter(Activity activity) {
        this.activity = activity;
    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.settings_item_container, parent, false));
    }

    @Override
    public void onBindViewHolder(ItemHolder holder, int position) {
        Resources res = holder.res;
        SettingData sd = titleList.get(position);
        holder.txt.setText(sd.getTitle().toUpperCase());
        if (position == 3 /*2 == ques count pos*/) {
            if (sd.getState()) {
                holder.btn.setText(res.getText(R.string.night));
                holder.btn.setTextColor(res.getColor(R.color.colorPrimaryDark));
            } else {
                holder.btn.setText(res.getText(R.string.day));
                holder.btn.setTextColor(res.getColor(R.color.colorAccent));
            }
        }
        else if (position == 2){
            if (sd.getState()) {
                holder.btn.setText(String.valueOf(MTestingFragment.BIGI));
                holder.btn.setTextColor(res.getColor(R.color.colorPrimaryDark));
            } else {
                holder.btn.setText(String.valueOf(MTestingFragment.MINI));
                holder.btn.setTextColor(res.getColor(R.color.colorAccent));
            }
        }else {
            if (sd.getState()) {
                holder.btn.setText(res.getText(R.string.settings_on));
                holder.btn.setTextColor(res.getColor(R.color.colorPrimaryDark));
            } else {
                holder.btn.setText(res.getText(R.string.settings_off));
                holder.btn.setTextColor(res.getColor(R.color.colorAccent));
            }
        }

    }

    @Override
    public int getItemCount() {
        return titleList.size();
    }

    class ItemHolder extends RecyclerView.ViewHolder {
        private TextView txt;
        private Button btn;
        private Resources res;


        ItemHolder(View itemView) {
            super(itemView);
            res = itemView.getResources();
            txt = itemView.findViewById(R.id.txt_settings);
            btn = itemView.findViewById(R.id.btn_settings);
            txt.setTypeface(typeface);
            btn.setTypeface(typeface);

            if (settingsManager != null)
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switch (getAdapterPosition()) {
                            case 0:
                                cycleMode = !cycleMode;
                                settingsManager.saveModeState(cycleMode);
                                titleList.get(getAdapterPosition()).setState(cycleMode);
                                break;
                            case 1:
                                autoflip = !autoflip;
                                settingsManager.saveAutoflipState(autoflip);
                                titleList.get(getAdapterPosition()).setState(autoflip);
                                break;
                            case 2:
                                quesCount = !quesCount;
                                settingsManager.saveQuesCount(quesCount);
                                titleList.get(getAdapterPosition()).setState(quesCount);
                                break;
                            case 3:
                                nighmode = !nighmode;
                                settingsManager.saveNightmodeState(nighmode);
                                titleList.get(getAdapterPosition()).setState(nighmode);
                                if (activity != null) {
                                    activity.startActivity(new Intent(activity, MainActivity.class));
                                    activity.finish();
                                }
                                break;

                        }
                        notifyDataSetChanged();

                    }
                });


        }
    }

    public void addSetting(String title, boolean state) {
        titleList.add(new SettingData(title, state));
        notifyDataSetChanged();
    }


    public void setTypeface(Typeface typeface) {
        this.typeface = typeface;
    }

    public void setSettingsManager(SettingsManager settingsManager) {
        this.settingsManager = settingsManager;
    }
}
