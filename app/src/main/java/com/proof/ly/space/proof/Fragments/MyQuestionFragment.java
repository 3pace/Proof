package com.proof.ly.space.proof.Fragments;

import android.content.Context;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.melnykov.fab.FloatingActionButton;
import com.proof.ly.space.proof.Adapters.RecyclerMenuAdapter;
import com.proof.ly.space.proof.CustomViews.MRecyclerView;
import com.proof.ly.space.proof.Fragments.windows.MQuestionManagerFragment;
import com.proof.ly.space.proof.Helpers.DBManager;
import com.proof.ly.space.proof.Helpers.USManager;
import com.proof.ly.space.proof.Interfaces.FragmentInterface;
import com.proof.ly.space.proof.Interfaces.OnItemClick;
import com.proof.ly.space.proof.MainActivity;
import com.proof.ly.space.proof.R;

import java.util.ArrayList;

/**
 * Created by aman on 29.03.18.
 */

public class MyQuestionFragment extends Fragment implements FragmentInterface {
    private FloatingActionButton mFloatingActionButton;
    private MRecyclerView mRecyclerView;
    private RecyclerMenuAdapter mMenuAdapter;
    private ArrayList<String> mArrayList = new ArrayList<>();
    private View mEmptyView;
    private DBManager mDBManager;
    private MainActivity mActivity;

    public static Fragment getInstance(){
        return new MyQuestionFragment();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (MainActivity) getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_my_questions,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        new GetData().execute();

    }

    @Override
    public void initViews(View itemView) {
        mFloatingActionButton = itemView.findViewById(R.id.fab);
        mRecyclerView = itemView.findViewById(R.id.rview);
        mEmptyView = itemView.findViewById(R.id.img_empty);
        ((ImageView) mEmptyView.findViewById(R.id.img_empty)).setColorFilter(mActivity.getDisabledColor(), PorterDuff.Mode.SRC_ATOP);
    }

    @Override
    public void initTypeface() {
        Typeface typeface = mActivity.getTypeface();
        mMenuAdapter.setTypeface(typeface);
    }

    @Override
    public void initOnClick() {
        mFloatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mFloatingActionButton.setClickable(false);
                mFloatingActionButton.hide();
                MQuestionManagerFragment.nextPageFast();
            }
        });

        mMenuAdapter.setOnItemClick(new OnItemClick() {
            @Override
            public void onClick(int pos) {

            }
        });
    }

    @Override
    public void initObjects() {
        mMenuAdapter = new RecyclerMenuAdapter(mArrayList);
        mDBManager =mActivity.getDatabaseManager();
    }

    @Override
    public void initSetters() {


        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mMenuAdapter);
        mFloatingActionButton.attachToRecyclerView(mRecyclerView);
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
            mArrayList.addAll(mDBManager.getUserQuestions(USManager.getUID()));
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mMenuAdapter.notifyDataSetChanged();
            mRecyclerView.setEmptyView(mEmptyView);
            initOnClick();

        }
    }
}
