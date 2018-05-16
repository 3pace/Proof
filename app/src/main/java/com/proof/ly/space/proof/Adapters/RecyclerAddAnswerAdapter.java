package com.proof.ly.space.proof.Adapters;

import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.proof.ly.space.proof.Data.NewAnswers;
import com.proof.ly.space.proof.Interfaces.OnItemClickView;
import com.proof.ly.space.proof.R;
import com.proof.ly.space.proof.Utils.ClickEffect;

import java.util.ArrayList;

/**
 * Created by aman on 25.03.18.
 */

public class RecyclerAddAnswerAdapter extends RecyclerView.Adapter<RecyclerAddAnswerAdapter.AnswerHolder> {

    private ArrayList<NewAnswers> mArrayList;
    private Typeface mTypeface;
    private OnItemClickView onItemClick;
    private int disabledColor = 0;


    public RecyclerAddAnswerAdapter() {
        mArrayList = new ArrayList<>();
    }

    @Override
    public AnswerHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.add_answer_container, parent, false);
        return new AnswerHolder(view);


    }

    @Override
    public void onBindViewHolder(AnswerHolder h, int position) {


        NewAnswers newAnswers = mArrayList.get(position);
        String answer = (position + 1) + " " + newAnswers.getAnswer();
        Spannable spannable = new SpannableString(answer);
        int start = answer.indexOf(" ");

        h.txt_answer.setTextColor(disabledColor);
        if (answer.length() > 0)
            if (newAnswers.isCorrect()) {
                spannable.setSpan(new ForegroundColorSpan(h.itemView.getContext().getResources().getColor(R.color.colorPrimary)),
                        start,
                        answer.length(),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            } else {
                spannable.setSpan(new ForegroundColorSpan(h.itemView.getContext().getResources().getColor(R.color.colorAccent)),
                        start,
                        answer.length(),
                        Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
        h.txt_answer.setText(spannable);

    }


    @Override
    public int getItemCount() {
        return mArrayList.size();
    }


    class AnswerHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView txt_answer;
        private ImageView img_clear;

        AnswerHolder(View itemView) {
            super(itemView);
            txt_answer = itemView.findViewById(R.id.txt_answer);
            img_clear = itemView.findViewById(R.id.img_clear);
            txt_answer.setTypeface(mTypeface);
            ClickEffect.setView(txt_answer);
            ClickEffect.setView(img_clear);
            txt_answer.setOnClickListener(this);
            img_clear.setOnClickListener(this);
            img_clear.setColorFilter(disabledColor, PorterDuff.Mode.SRC_ATOP);

        }

        @Override
        public void onClick(View view) {
            onItemClick.onClick(getAdapterPosition(), view);
            img_clear.setEnabled(false);
        }
    }


    public void setTypeface(Typeface typeface) {
        mTypeface = typeface;
    }

    public void addAnswer(String answer) {
        mArrayList.add(new NewAnswers(answer, false));
        notifyItemInserted(mArrayList.size());


    }

    public void removeAnswer(int pos) {
        mArrayList.remove(pos);
        notifyItemRemoved(pos);
    }

    public void setAnswerCorrect(int pos) {

        mArrayList.get(pos).setCorrect(!mArrayList.get(pos).isCorrect());
        notifyDataSetChanged();
    }

    public ArrayList<NewAnswers> getArrayList() {
        return mArrayList;
    }


    public void setDisabledColor(int disabledColor) {
        this.disabledColor = disabledColor;
    }

    public void setOnItemClick(OnItemClickView onItemClick) {
        this.onItemClick = onItemClick;
    }
}
