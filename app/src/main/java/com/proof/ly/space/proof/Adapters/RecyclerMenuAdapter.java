package com.proof.ly.space.proof.Adapters;

import android.content.res.Resources;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
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
    private ArrayList<String> mArrayList;
    private Typeface mTypeface;
    private OnItemClick mOnItemClick;
    private static final int LESSON = 0;
    private static final int MENU = 1;
    private boolean mLessonHolderView;
    private int mSize = 0;
    private int mMenuItemPos = 0;
    private LessonManager mLessonManager;
    private OnItemClickView mOnHeaderClick;

    public RecyclerMenuAdapter(ArrayList<String> arrayList) {
        this.mArrayList = arrayList;
        mSize = arrayList.size();


    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case LESSON:
                return new LessonHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.lesson_select_container, parent, false));
            case MENU:
                return new ItemHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_item_container, parent, false));
        }
        return new ItemHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_item_container, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemHolder) {
            ItemHolder h = (ItemHolder) holder;
            h.mButton.setText(mArrayList.get(position - mMenuItemPos));
        }

    }

    @Override
    public int getItemViewType(int position) {
        if (!mLessonHolderView) return MENU;
        else return (position == 0) ? LESSON : MENU;


    }

    @Override
    public int getItemCount() {
        return mSize;
    }

    private class ItemHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private Button mButton;

        ItemHolder(View itemView) {
            super(itemView);
            mButton = itemView.findViewById(R.id.btn_menu);
            mButton.setTypeface(mTypeface);
            mButton.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            mOnItemClick.onClick(getAdapterPosition() - mMenuItemPos);
        }
    }

    private class LessonHolder extends RecyclerView.ViewHolder {
        private Button mButton;
        private Resources mRes;
        private Drawable mIconDrwbl;

        LessonHolder(View itemView) {
            super(itemView);
            mRes = itemView.getResources();
            mIconDrwbl = mRes.getDrawable(R.drawable.round_swap_horiz_black_24);
            mIconDrwbl.setColorFilter(mRes.getColor(R.color.colorPrimaryDark), PorterDuff.Mode.SRC_ATOP);

            mButton = itemView.findViewById(R.id.btn_lesson);
            if (mLessonManager.getCount() > 0)
                mButton.setCompoundDrawablesWithIntrinsicBounds(mIconDrwbl,
                        null, null, null);
            else
                mButton.setTextColor(mRes.getColor(R.color.colorAccent));
            mButton.setTypeface(mTypeface);
            mButton.setText(mLessonManager.setCurrentLesson(LessonManager.CURRENT_LESSON, LessonManager.CURRENT_DB));
            mButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mOnHeaderClick.onClick(getAdapterPosition(), v);

                }
            });
        }
    }

    public void addLessonHolder(boolean lessonHolderView) {
        int c = 1;
        mSize = mArrayList.size() + c;
        mMenuItemPos = c;
        this.mLessonHolderView = lessonHolderView;
    }

    public void setTypeface(Typeface typeface) {
        this.mTypeface = typeface;
    }

    public void setOnItemClick(OnItemClick onItemClick) {
        this.mOnItemClick = onItemClick;
    }

    public void setLessonManager(LessonManager lessonManager) {
        this.mLessonManager = lessonManager;
    }

    public void setOnHeaderClick(OnItemClickView onHeaderClick) {
        this.mOnHeaderClick = onHeaderClick;
    }
}
