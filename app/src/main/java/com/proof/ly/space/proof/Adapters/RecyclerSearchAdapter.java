package com.proof.ly.space.proof.Adapters;

import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
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
    private ArrayList<SearchData> arrayList;
    private ArrayList<SearchData> filterList;
    private ArrayList<SearchData> fullList;
    private Typeface typeface;


    public RecyclerSearchAdapter(ArrayList<SearchData> arrayList) {
        this.filterList = new ArrayList<>();
        this.arrayList = arrayList;
        this.filterList.addAll(arrayList);


    }

    @Override
    public ItemHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ItemHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.search_container, parent, false));

    }

    @Override
    public void onBindViewHolder(ItemHolder h, int position) {

        h.txt_q.setText(filterList.get(position).getQuestion().toUpperCase().trim());
        h.txt_a.setText(filterList.get(position).getAnswer().toUpperCase().trim());

    }

    @Override
    public int getItemCount() {
        return filterList.size();
    }


    class ItemHolder extends RecyclerView.ViewHolder {
        private TextView txt_q, txt_a;

        ItemHolder(View itemView) {
            super(itemView);
            txt_q = itemView.findViewById(R.id.txt_question);
            txt_a = itemView.findViewById(R.id.txt_answer);
            txt_q.setTypeface(typeface);
            txt_a.setTypeface(typeface);
        }
    }


    public void setTypeface(Typeface typeface) {
        this.typeface = typeface;
    }

    public void addLoadedItems(ArrayList<SearchData> arrayList, int start, int end) {
        int max = (start + end);
        if (arrayList.size() <= (start + end)) max = arrayList.size();
        for (int i = start; i < max; i++) {
            filterList.add(new SearchData(arrayList.get(i).getQuestion(), arrayList.get(i).getAnswer()));
        }
    }

    public void filter(String inputText) {
        boolean find = false;
        inputText = inputText.toLowerCase(Locale.getDefault()).replace(",", " ");
        String[] inputArray = inputText.split(" ");
        filterList.clear();

        if (inputText.length() == 0) {
            filterList.addAll(arrayList);
        } else {
            for (int i = 0; i < fullList.size(); i++) {
                SearchData searchData = fullList.get(i);
                if (inputArray.length == 1 && searchData.getQuestion().toLowerCase().replace(",", " ").contains(inputText)) {
                    filterList.add(searchData);
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
                    filterList.add(searchData);
                }

            }
        }
        notifyDataSetChanged();
    }

    public void setFull(ArrayList<SearchData> arrayList) {
        filterList.clear();
        for (int i = 0; i < arrayList.size(); i++) {
            filterList.add(new SearchData(arrayList.get(i).getQuestion(), arrayList.get(i).getAnswer()));
            notifyItemInserted(i);
        }
    }

    public void setFullList(ArrayList<SearchData> fullList) {
        this.fullList = fullList;
    }
}
