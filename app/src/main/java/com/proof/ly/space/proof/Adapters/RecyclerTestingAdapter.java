package com.proof.ly.space.proof.Adapters;

import android.annotation.SuppressLint;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


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

    private ArrayList<Answers> arrayList;
    private Typeface typeface;
    private OnItemClick onItemClick;
    private final int QUESTION = 0, ANSWER = 1;
    private String question;
    private int disabledColor,clickedColor;

    public RecyclerTestingAdapter(ArrayList<Answers> arrayList) {
        this.arrayList = arrayList;



    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case QUESTION:
                return new QuestionHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.question_container, parent, false));
            case ANSWER:
                return new ItemHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.answer_container, parent, false));
        }
        return new ItemHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.answer_container, parent, false));

    }


    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemHolder) {
            ItemHolder itemHolder = (ItemHolder) holder;
            Answers answers = arrayList.get(position - 1);
            itemHolder.txt_answer.setText(answers.getAnswer());
            itemHolder.txt_answer.setEnabled(answers.isEnabled());
            switch (answers.getChecked()) {
                case 1:
                    if (SettingsManager.colored) {
                        if (answers.isCorrectChecked())
                            itemHolder.txt_answer.setTextColor(itemHolder.itemView.getContext().getResources().getColor(R.color.colorPrimary));
                        else
                            itemHolder.txt_answer.setTextColor(itemHolder.itemView.getContext().getResources().getColor(R.color.colorAccent));
                    } else {
                        if (answers.isClicked())
                            itemHolder.txt_answer.setTextColor(clickedColor);
                        else
                            itemHolder.txt_answer.setTextColor(disabledColor);
                    }
                    break;
                case 2:

                    itemHolder.txt_answer.setTextColor(disabledColor);
                    break;
                case 3:
                    if (SettingsManager.colored) {
                        if (answers.isCorrectChecked())
                            itemHolder.txt_answer.setTextColor(itemHolder.itemView.getContext().getResources().getColor(R.color.colorPrimary));
                        else
                            itemHolder.txt_answer.setTextColor(itemHolder.itemView.getContext().getResources().getColor(R.color.colorAccent));
                    } else {
                        itemHolder.txt_answer.setTextColor(disabledColor);
                    }
                    break;
                case 4:
                    if (SettingsManager.colored) {
                        if (answers.isCorrectChecked())
                            itemHolder.txt_answer.setTextColor(itemHolder.itemView.getContext().getResources().getColor(R.color.colorPrimary));
                        else
                            itemHolder.txt_answer.setTextColor(itemHolder.itemView.getContext().getResources().getColor(R.color.colorAccent));
                    } else {
                        itemHolder.txt_answer.setTextColor(clickedColor);
                    }

                    break;
            }

        }
        if (holder instanceof QuestionHolder) {
            QuestionHolder q = (QuestionHolder) holder;
            q.txt_q.setText(question);
        }

    }

    @Override
    public int getItemCount() {
        return arrayList.size() + 1;
    }

    private class QuestionHolder extends RecyclerView.ViewHolder {
        private TextView txt_q;

        QuestionHolder(View itemView) {
            super(itemView);
            txt_q = itemView.findViewById(R.id.txt_question);
            txt_q.setTypeface(typeface);
        }
    }

    public class ItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView txt_answer;

        ItemHolder(View itemView) {
            super(itemView);
            txt_answer = itemView.findViewById(R.id.txt_answer);
            txt_answer.setOnClickListener(this);
            ClickEffect.setViewFast(txt_answer);
            txt_answer.setTypeface(typeface);
        }

        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.txt_answer:
                    onItemClick.onClick(getAdapterPosition() - 1);
                    break;
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        return (position == QUESTION) ? QUESTION : ANSWER;
    }

    public void setTypeface(Typeface typeface) {
        this.typeface = typeface;
    }

    public void setOnItemClick(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public void setDisabledColor(int disabledColor,int clickedColor) {
        this.disabledColor = disabledColor;
        this.clickedColor = clickedColor;
    }
}
