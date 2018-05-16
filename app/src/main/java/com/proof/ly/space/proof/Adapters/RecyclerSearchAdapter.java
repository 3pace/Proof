package com.proof.ly.space.proof.Adapters;

import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.proof.ly.space.proof.Data.SearchData;
import com.proof.ly.space.proof.R;

import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by aman on 4/17/18.
 */

public class RecyclerSearchAdapter extends RecyclerView.Adapter<RecyclerSearchAdapter.ItemHolder> {
    private ArrayList<SearchData> mArrayList;
    private ArrayList<SearchData> mFilterList;
    private ArrayList<SearchData> mFullList;
    private Typeface mTypeface;


    public RecyclerSearchAdapter(ArrayList<SearchData> arrayList) {
        this.mFilterList = new ArrayList<>();
        this.mArrayList = arrayList;
        this.mFilterList.addAll(arrayList);


    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.search_container, parent, false));

    }

    @Override
    public void onBindViewHolder(ItemHolder h, int position) {

        h.mTextViewQuestion.setText(mFilterList.get(position).getQuestion().trim());
        h.mTextViewAnswer.setText(mFilterList.get(position).getAnswer().trim());

    }

    @Override
    public int getItemCount() {
        return mFilterList.size();
    }


    class ItemHolder extends RecyclerView.ViewHolder {
        private TextView mTextViewQuestion, mTextViewAnswer;

        ItemHolder(View itemView) {
            super(itemView);
            mTextViewQuestion = itemView.findViewById(R.id.txt_question);
            mTextViewAnswer = itemView.findViewById(R.id.txt_answer);
            mTextViewQuestion.setTypeface(mTypeface);
            mTextViewAnswer.setTypeface(mTypeface);
        }
    }


    public void setTypeface(Typeface typeface) {
        this.mTypeface = typeface;
    }

    public void addLoadedItems(ArrayList<SearchData> arrayList, int start, int end) {

        int max = (start + end);
        if (arrayList.size() <= (start + end)) max = arrayList.size();
        for (int i = start; i < max; i++) {
            mFilterList.add(new SearchData(arrayList.get(i).getQuestion(), arrayList.get(i).getAnswer()));
        }

        Log.d("search", "size " + mFilterList.size() + " start - " + start + " end - " + end);
    }

    public void filter(String inputText) {
        boolean find = false;
        inputText = inputText.toLowerCase(Locale.getDefault()).replace(",", " ");
        String[] inputArray = inputText.split(" ");
        mFilterList.clear();

        if (inputText.length() == 0) {
            mFilterList.clear();
            mFilterList.addAll(mArrayList);
        } else {
            for (int i = 0; i < mFullList.size(); i++) {
                SearchData searchData = mFullList.get(i);
                if (inputArray.length == 1 && searchData.getQuestion().toLowerCase().replace(",", " ").contains(inputText)) {
                    mFilterList.add(searchData);
                }
                if (inputArray.length > 1) {
                    int length = inputArray.length;
                    int i2 = 0;
                    while (i2 < length) {
                        if (!searchData.getQuestion().toLowerCase().replace(",", " ").contains(inputArray[i2])) {
                            find = false;
                            break;
                        } else {
                            find = true;
                            i2++;
                        }
                    }
                }
                if (find) {
                    mFilterList.add(searchData);
                }

            }
        }
        notifyDataSetChanged();
        Log.d("search", "filter");
    }

    public void setFull(ArrayList<SearchData> arrayList) {
        mFilterList.clear();
        for (int i = 0; i < arrayList.size(); i++) {
            mFilterList.add(new SearchData(arrayList.get(i).getQuestion(), arrayList.get(i).getAnswer()));
            notifyItemInserted(i);
        }
        Log.d("search", "setFull");
    }

    public void setFullList(ArrayList<SearchData> fullList) {
        this.mFullList = fullList;
    }
}
