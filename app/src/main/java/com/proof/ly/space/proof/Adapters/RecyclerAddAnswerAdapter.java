package com.proof.ly.space.proof.Adapters;

import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
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

    private ArrayList<NewAnswers> arrayList;
    private Typeface typeface;
    private OnItemClickView onItemClick;


    public RecyclerAddAnswerAdapter() {
        arrayList = new ArrayList<>();
    }

    @Override
    public AnswerHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.add_answer_container, parent, false);
        return new AnswerHolder(view);


    }

    @Override
    public void onBindViewHolder(AnswerHolder h, int position) {

            NewAnswers newAnswers = arrayList.get(position);
            if (newAnswers.isCorrect()) {
                h.txt_answer.setTextColor(h.itemView.getContext().getResources().getColor(R.color.colorPrimary));
            } else {
                h.txt_answer.setTextColor(h.itemView.getContext().getResources().getColor(R.color.colorAccent));
            }
            StringBuilder stringBuilder = new StringBuilder();
            h.txt_answer.setText(stringBuilder.append("#").append((position + 1)).append(" ").append(newAnswers.getAnswer()));

    }



    @Override
    public int getItemCount() {
        return arrayList.size();
    }



    class AnswerHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView txt_answer;
        private ImageView img_clear;
        AnswerHolder(View itemView) {
            super(itemView);
            txt_answer = itemView.findViewById(R.id.txt_answer);
            img_clear = itemView.findViewById(R.id.img_clear);
            txt_answer.setTypeface(typeface);
            ClickEffect.setView(txt_answer);
            ClickEffect.setView(img_clear);
            txt_answer.setOnClickListener(this);
            img_clear.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            onItemClick.onClick(getAdapterPosition(),view);
            img_clear.setEnabled(false);
        }
    }


    public void setTypeface(Typeface typeface) {
        this.typeface = typeface;
    }

    public void addAnswer(String answer) {
        arrayList.add(new NewAnswers(answer, false));
        notifyItemInserted(arrayList.size());



    }

    public void removeAnswer(int pos){
        arrayList.remove(pos);
        notifyItemRemoved(pos);
    }

    public void setAnswerCorrect(int pos) {

        arrayList.get(pos).setCorrect(!arrayList.get(pos).isCorrect());
        notifyDataSetChanged();
    }

    public ArrayList<NewAnswers> getArrayList() {
        return arrayList;
    }



    public void setOnItemClick(OnItemClickView onItemClick) {
        this.onItemClick = onItemClick;
    }
}
