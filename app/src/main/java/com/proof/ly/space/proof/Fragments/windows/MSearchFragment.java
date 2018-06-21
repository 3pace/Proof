package com.proof.ly.space.proof.Fragments.windows;

import android.content.Context;
import android.graphics.PorterDuff;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.proof.ly.space.proof.Adapters.RecyclerSearchAdapter;
import com.proof.ly.space.proof.CustomViews.MRecyclerView;
import com.proof.ly.space.proof.Data.SearchData;
import com.proof.ly.space.proof.Helpers.DBManager;
import com.proof.ly.space.proof.Helpers.LessonManager;
import com.proof.ly.space.proof.Interfaces.FragmentInterface;
import com.proof.ly.space.proof.MainActivity;
import com.proof.ly.space.proof.R;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

/**
 * Created by aman on 4/17/18.
 */

public class MSearchFragment extends Fragment implements FragmentInterface{

    private MRecyclerView mRecyclerView;
    private RecyclerSearchAdapter mAdapter;
    private ArrayList<SearchData> mArrayList = new ArrayList<>();
    private EditText mSearchEditText;
    private DBManager mDBManager;
    private int mVisibleThreshold = 5;
    private int mLastVisibleItem, mTotalItemCount;
    private int mAddItemCount = 5;
    private boolean isLoading;
    private View mEmptyView;
    private MainActivity mActivity;
    private Toolbar mToolbar;
    private TextView mTextViewToolbar;

    public MSearchFragment() {
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (MainActivity) context;
    }



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.search_fragment,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        new GetData().execute();
    }

    @Override
    public void initViews(View itemView) {
        mToolbar = itemView.findViewById(R.id.tbar);
        mTextViewToolbar = mToolbar.findViewById(R.id.txt_tbar);
        mActivity.setSupportActionBar(mToolbar);
        mRecyclerView = itemView.findViewById(R.id.rview);
        mSearchEditText = itemView.findViewById(R.id.etxt_search);
        mEmptyView = itemView.findViewById(R.id.img_empty);
        ((ImageView) mEmptyView.findViewById(R.id.img_empty)).setColorFilter(mActivity.getDisabledColor(), PorterDuff.Mode.SRC_ATOP);

    }

    @Override
    public void initTypeface() {
        mSearchEditText.setTypeface(mActivity.getTypeface());
        mTextViewToolbar.setTypeface(mActivity.getTypeface());
    }

    @Override
    public void initOnClick() {
        mSearchEditText.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                mSearchEditText.setText("");
                mAdapter.filter(mSearchEditText.getText().toString());
                return true;
            }
        });
    }

    @Override
    public void initObjects() {
        mAdapter = new RecyclerSearchAdapter(mArrayList);
        mDBManager = mActivity.getDatabaseManager();
    }

    @Override
    public void initSetters() {
        mTextViewToolbar.setText(getResources().getString(R.string.tag_search));
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(false);
        mAdapter.setTypeface(mActivity.getTypeface());
        mSearchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mAdapter.filter(s.toString());
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        initLoadMore();


    }
    private class GetData extends AsyncTask<Void,Void,Void>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mArrayList.clear();
            initObjects();
            initSetters();
            initTypeface();
        }

        @Override
        protected Void doInBackground(Void... voids) {

            //mDBManager.getSearchHelper();
            mDBManager.generateSearchListFullFromLocalDb();
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            Log.d(TAG, "onPostExecute: "+ LessonManager.CURRENT_DB);
            mAdapter.setFull(mDBManager.getSearchList(10));
            mAdapter.setFullList(mDBManager.getFullSearchList());
            mRecyclerView.setEmptyView(mEmptyView);
            initOnClick();
        }
    }
    private class LoadData extends AsyncTask<Integer,Void,Void>{

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            isLoading = true;
        }

        @Override
        protected Void doInBackground(Integer... integers) {
            //mArrayList.addAll(mDBManager.getSearchListLoad(integers[0],integers[1]));
            mAdapter.addLoadedItems(mDBManager.getSearchListLoad(integers[0],integers[1]),integers[0],integers[1]);
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mAdapter.notifyDataSetChanged();
            isLoading = false;
        }
    }
    public void initLoadMore(){
        final LinearLayoutManager layoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                mTotalItemCount = layoutManager.getItemCount();
                mLastVisibleItem = layoutManager.findLastVisibleItemPosition();
                if (!isLoading && (mLastVisibleItem + mVisibleThreshold) >= mTotalItemCount && mSearchEditText.getText().length() <=0) {
                        new LoadData().execute(mAdapter.getItemCount(), mAddItemCount);

                }
            }
        });
    }
}
