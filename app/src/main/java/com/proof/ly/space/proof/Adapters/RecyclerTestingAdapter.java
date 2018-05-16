package com.proof.ly.space.proof.Adapters;

import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.proof.ly.space.proof.Data.Answers;
import com.proof.ly.space.proof.Helpers.SettingsManager;
import com.proof.ly.space.proof.Interfaces.OnItemClick;
import com.proof.ly.space.proof.R;
import com.proof.ly.space.proof.Utils.ClickEffect;

import java.util.ArrayList;

/**
 * Created by aman on 3/12/18.
 */

public class RecyclerTestingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<Answers> mArrayList;
    private Typeface mTypeface;
    private OnItemClick mOnItemClick;
    private final int QUESTION = 0, ANSWER = 1;
    private final int ADS = 2;
    private int mAdsPos = 0;
    private String mQuestion;
    private int mDisabledColor, mClickedColor;
    private boolean mAdsShow;

    public RecyclerTestingAdapter(ArrayList<Answers> arrayList) {
        this.mArrayList = arrayList;
        mAdsPos = (int) (Math.random() * (arrayList.size())) + 1;
        mAdsShow = ((int) (Math.random() * 5)) == 1;


    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case QUESTION:
                return new QuestionHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.question_container, parent, false));
            case ANSWER:
                return new ItemHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.answer_container, parent, false));
            case ADS:
                return new AdsHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.ads_container, parent, false));
        }

        return new ItemHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.answer_container, parent, false));

    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemHolder) {
            ItemHolder itemHolder = (ItemHolder) holder;
            int pos = mAdsShow && position > mAdsPos ? position - 2 : position - 1;
            Log.d("test", pos + " : " + position + " * " + mArrayList.size());
            Answers answers = mArrayList.get(pos);
            itemHolder.mTextViewAnswer.setText(answers.getAnswer());
            itemHolder.mTextViewAnswer.setEnabled(answers.isEnabled());
            switch (answers.getIsChecked()) {
                case 1:
                    if (SettingsManager.colored) {
                        if (answers.isCorrectChecked())
                            itemHolder.mTextViewAnswer.setTextColor(itemHolder.itemView.getContext().getResources().getColor(R.color.colorPrimary));
                        else
                            itemHolder.mTextViewAnswer.setTextColor(itemHolder.itemView.getContext().getResources().getColor(R.color.colorAccent));
                    } else {
                        if (answers.isClicked())
                            itemHolder.mTextViewAnswer.setTextColor(mClickedColor);
                        else
                            itemHolder.mTextViewAnswer.setTextColor(mDisabledColor);
                    }
                    break;
                case 2:

                    itemHolder.mTextViewAnswer.setTextColor(mDisabledColor);
                    break;
                case 3:
                    if (SettingsManager.colored) {
                        if (answers.isCorrectChecked())
                            itemHolder.mTextViewAnswer.setTextColor(itemHolder.itemView.getContext().getResources().getColor(R.color.colorPrimary));
                        else
                            itemHolder.mTextViewAnswer.setTextColor(itemHolder.itemView.getContext().getResources().getColor(R.color.colorAccent));
                    } else {
                        itemHolder.mTextViewAnswer.setTextColor(mDisabledColor);
                    }
                    break;
                case 4:
                    if (SettingsManager.colored) {
                        if (answers.isCorrectChecked())
                            itemHolder.mTextViewAnswer.setTextColor(itemHolder.itemView.getContext().getResources().getColor(R.color.colorPrimary));
                        else
                            itemHolder.mTextViewAnswer.setTextColor(itemHolder.itemView.getContext().getResources().getColor(R.color.colorAccent));
                    } else {
                        itemHolder.mTextViewAnswer.setTextColor(mClickedColor);
                    }

                    break;
            }

        }
        if (holder instanceof QuestionHolder) {
            QuestionHolder q = (QuestionHolder) holder;
            q.mTextViewQuestion.setText(mQuestion);
        }

    }

    @Override
    public int getItemCount() {
        return mAdsShow ? mArrayList.size() + 2 : mArrayList.size() + 1;
    }

    private class QuestionHolder extends RecyclerView.ViewHolder {
        private TextView mTextViewQuestion;

        QuestionHolder(View itemView) {
            super(itemView);
            mTextViewQuestion = itemView.findViewById(R.id.txt_question);
            mTextViewQuestion.setTypeface(mTypeface);
        }
    }

    public class ItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView mTextViewAnswer;

        ItemHolder(View itemView) {
            super(itemView);
            mTextViewAnswer = itemView.findViewById(R.id.txt_answer);
            mTextViewAnswer.setOnClickListener(this);
            ClickEffect.setViewFast(mTextViewAnswer);
            mTextViewAnswer.setTypeface(mTypeface);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.txt_answer:
                    int pos = mAdsShow && getAdapterPosition() > mAdsPos ? getAdapterPosition() - 2 : getAdapterPosition() - 1;
                    mOnItemClick.onClick(pos);
                    break;
            }
        }
    }

    class AdsHolder extends RecyclerView.ViewHolder {
        private AdView mAdView;

        public AdsHolder(View itemView) {
            super(itemView);
            mAdView = itemView.findViewById(R.id.banner_AdView);
            mAdView.setVisibility(View.GONE);
            AdRequest adRequest = new AdRequest.Builder().build();
            mAdView.setAdListener(new AdListener() {
                @Override
                public void onAdLoaded() {
                    super.onAdLoaded();
                    mAdView.setVisibility(View.VISIBLE);
                }


            });
            mAdView.loadAd(adRequest);

        }
    }

    @Override
    public int getItemViewType(int position) {

        if (position == mAdsPos && mAdsShow)
            return ADS;
        else
            return (position == QUESTION) ? QUESTION : ANSWER;
    }

    public void setTypeface(Typeface typeface) {
        this.mTypeface = typeface;
    }

    public void setOnItemClick(OnItemClick onItemClick) {
        this.mOnItemClick = onItemClick;
    }

    public void setQuestion(String question) {
        this.mQuestion = question;
    }

    public void setDisabledColor(int disabledColor, int clickedColor) {
        this.mDisabledColor = disabledColor;
        this.mClickedColor = clickedColor;
    }


}
