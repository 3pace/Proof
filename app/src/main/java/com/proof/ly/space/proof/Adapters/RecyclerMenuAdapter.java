package com.proof.ly.space.proof.Adapters;

import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import com.proof.ly.space.proof.Helpers.LessonManager;
import com.proof.ly.space.proof.Interfaces.OnItemClick;
import com.proof.ly.space.proof.Interfaces.OnItemClickView;
import com.proof.ly.space.proof.R;

import java.util.ArrayList;

/**
 * Created by aman on 24.03.18.
 */

public class RecyclerMenuAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<String> arrayList;
    private Typeface typeface;
    private OnItemClick onItemClick;
    private static final int LESSON = 0;
    private static final int MENU = 1;
    private boolean lessonHolderView;
    private int size = 0;
    private int menuItemPos = 0;
    private LessonManager lessonManager;
    private OnItemClickView onHeaderClick;

    public RecyclerMenuAdapter(ArrayList<String> arrayList){
        this.arrayList = arrayList;
        size = arrayList.size();


    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType){
            case LESSON:
                return new LessonHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.lesson_select_container,parent,false));
            case MENU:
                return new ItemHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_item_container,parent,false));
        }
        return new ItemHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_item_container,parent,false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemHolder) {
            ItemHolder h = (ItemHolder) holder;
            h.btn.setText(arrayList.get(position-menuItemPos));
        }

    }

    @Override
    public int getItemViewType(int position) {
        if (!lessonHolderView) return MENU;
        else return (position == 0) ? LESSON : MENU;


    }

    @Override
    public int getItemCount() {
        return size;
    }
    private class ItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private Button btn;
        ItemHolder(View itemView) {
            super(itemView);
            btn = itemView.findViewById(R.id.btn_menu);
            btn.setTypeface(typeface);
            btn.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            onItemClick.onClick(getAdapterPosition() - menuItemPos);
        }
    }
    private class LessonHolder extends RecyclerView.ViewHolder {
        private Button btn;
        LessonHolder(View itemView) {
            super(itemView);
            btn = itemView.findViewById(R.id.btn_lesson);
            btn.setTypeface(typeface);
            btn.setText(lessonManager.setCurrentLesson(LessonManager.CURRENT_LESSON, LessonManager.CURRENT_DB));
            btn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onHeaderClick.onClick(getAdapterPosition(),v);

                }
            });
        }
    }

    public void addLessonHolder(boolean lessonHolderView){
        int c = 1;
        size = arrayList.size() + c;
        menuItemPos = c;
        this.lessonHolderView = lessonHolderView;
    }
    public void setTypeface(Typeface typeface) {
        this.typeface = typeface;
    }

    public void setOnItemClick(OnItemClick onItemClick) {
        this.onItemClick = onItemClick;
    }

    public void setLessonManager(LessonManager lessonManager) {
        this.lessonManager = lessonManager;
    }

    public void setOnHeaderClick(OnItemClickView onHeaderClick) {
        this.onHeaderClick = onHeaderClick;
    }
}
