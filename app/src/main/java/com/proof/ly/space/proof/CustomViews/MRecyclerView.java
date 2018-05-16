package com.proof.ly.space.proof.CustomViews;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by aman on 4/24/18.
 */

public class MRecyclerView extends RecyclerView {

    private View mEmptyView;
    private boolean mIsFirst = false;

    final private AdapterDataObserver observer = new AdapterDataObserver() {
        @Override
        public void onChanged() {
            checkIfEmpty();
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            checkIfEmpty();
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            checkIfEmpty();
        }
    };

    public MRecyclerView(Context context) {
        super(context);
    }

    public MRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MRecyclerView(Context context, AttributeSet attrs,
                         int defStyle) {
        super(context, attrs, defStyle);
    }

    void checkIfEmpty() {
        if (mEmptyView != null && getAdapter() != null) {
            final boolean emptyViewVisible =
                    getAdapter().getItemCount() == 0;
            if (mIsFirst) mEmptyView.setAlpha(0);
            mEmptyView.animate().alpha(1).setDuration(500).start();
            mEmptyView.setVisibility(emptyViewVisible ? VISIBLE : GONE);

            setVisibility(emptyViewVisible ? INVISIBLE : VISIBLE);
        }
    }

    @Override
    public void setAdapter(Adapter adapter) {
        final Adapter oldAdapter = getAdapter();
        if (oldAdapter != null) {
            oldAdapter.unregisterAdapterDataObserver(observer);
        }
        super.setAdapter(adapter);
        if (adapter != null) {
            adapter.registerAdapterDataObserver(observer);
        }

        checkIfEmpty();
    }

    public void setEmptyView(View emptyView) {
        this.mEmptyView = emptyView;
        this.mEmptyView.setAlpha(1);
        this.mEmptyView.setVisibility(GONE);
        checkIfEmpty();
        mIsFirst = true;
    }
}