package com.proof.ly.space.proof.Fragments;

import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
    private FloatingActionButton fab;
    private MRecyclerView rview;
    private RecyclerMenuAdapter adapter;
    private ArrayList<String> arrayList = new ArrayList<>();
    private View mEmptyView;
    private DBManager dbManager;

    public static Fragment getInstance(){
        return new MyQuestionFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //
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
        new getData().execute();

    }

    @Override
    public void initViews(View itemView) {
        fab = itemView.findViewById(R.id.fab);
        rview = itemView.findViewById(R.id.rview);
        mEmptyView = itemView.findViewById(R.id.img_empty);
    }

    @Override
    public void initTypeface() {
        Typeface typeface = ((MainActivity) getActivity()).getTypeface();
        adapter.setTypeface(typeface);
    }

    @Override
    public void initOnClick() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                fab.setClickable(false);
                fab.hide();
                MQuestionManagerFragment.nextPageFast();
            }
        });

        adapter.setOnItemClick(new OnItemClick() {
            @Override
            public void onClick(int pos) {

            }
        });
    }

    @Override
    public void initObjects() {
        adapter = new RecyclerMenuAdapter(arrayList);
        dbManager = ((MainActivity)getActivity()).getDbManager();
    }

    @Override
    public void initSetters() {


        rview.setLayoutManager(new LinearLayoutManager(getContext()));
        rview.setAdapter(adapter);
        fab.attachToRecyclerView(rview);
    }
    private class getData extends AsyncTask<Void,Void,Void>{
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            arrayList.clear();
            initObjects();
            initSetters();
            initTypeface();

        }

        @Override
        protected Void doInBackground(Void... voids) {
            arrayList.addAll(dbManager.getUserQuestions(USManager.getUID()));
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            adapter.notifyDataSetChanged();
            rview.setEmptyView(mEmptyView);
            initOnClick();

        }
    }
}
