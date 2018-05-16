package com.proof.ly.space.proof.Fragments.windows;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.proof.ly.space.proof.Adapters.RecyclerMenuAdapter;
import com.proof.ly.space.proof.Interfaces.FragmentInterface;
import com.proof.ly.space.proof.Interfaces.OnItemClick;
import com.proof.ly.space.proof.Interfaces.OnItemClickView;
import com.proof.ly.space.proof.MainActivity;
import com.proof.ly.space.proof.R;

import java.util.ArrayList;

import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;


/**
 * Created by aman on 4/14/18.
 */

public class TMenuFragment extends Fragment implements FragmentInterface {

    private RecyclerMenuAdapter mRecyclerMenuAdapter;
    private RecyclerView mRecyclerView;
    private ArrayList<String> mArrayList = new ArrayList<>();
    public static boolean isLoading = false;


    public TMenuFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addMenuItems();
        initObjects();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.menu_fragment, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        initTypeface();
        initSetters();
        initOnClick();


    }

    public void addMenuItems() {


        mArrayList.add(getResources().getString(R.string.testing));
        mArrayList.add(getResources().getString(R.string.question_db));
        mArrayList.add(getResources().getString(R.string.search_title));
        mArrayList.add(getResources().getString(R.string.settings));

        //mRecyclerMenuAdapter.notifyDataSetChanged();
    }
    public void updateAdapter(){

        mRecyclerMenuAdapter.notifyDataSetChanged();
        mRecyclerView.setAdapter(mRecyclerMenuAdapter);
    }

    @Override
    public void initViews(View itemView) {
        mRecyclerView = itemView.findViewById(R.id.rview);
    }

    @Override
    public void initTypeface() {
        Typeface typeface = ((MainActivity) getActivity()).getTypeface();
        mRecyclerMenuAdapter.setTypeface(typeface);
    }

    @Override
    public void initOnClick() {
        mRecyclerMenuAdapter.setOnItemClick(new OnItemClick() {

            @Override
            public void onClick(int pos) {
                switch (pos) {
                    case 0:
                        //startActivity(new Intent(MainActivity.this, TestingActivity.class));
                        ((MainActivity) getActivity()).replaceFragment(new MTestingFragment(), getResources().getString(R.string.tag_testing));

                        break;
                    case 1:
                        //startActivity(new Intent(MainActivity.this, QuestionManagerActivity.class));
                        ((MainActivity) getActivity()).replaceFragment(new MQuestionManagerFragment(), getResources().getString(R.string.tag_qmanager));
                        break;
                    case 2:
                        //startActivity(new Intent(MainActivity.this, QuestionManagerActivity.class));
                        ((MainActivity) getActivity()).replaceFragment(new MSearchFragment(), getResources().getString(R.string.tag_search));
                        break;
                    case 3:
                        ((MainActivity) getActivity()).replaceFragment(new MSettingsFragment(), getResources().getString(R.string.tag_settings));
                        break;

                }
            }
        });
        mRecyclerMenuAdapter.setOnHeaderClick(new OnItemClickView() {
            @Override
            public void onClick(int pos, View view) {
                switch (view.getId()) {
                    case R.id.btn_lesson:
                        if (!isLoading) {
                            Button btn = (Button) view;
                            ((MainActivity) getActivity()).startAsyncLoad();
                            btn.setText(((MainActivity) getActivity()).getmLessonManager().changeLesson());
                        }
                        break;
                }

            }
        });
    }

    @Override
    public void initObjects() {
        mRecyclerMenuAdapter = new RecyclerMenuAdapter(mArrayList);
        mRecyclerMenuAdapter.addLessonHolder(true);
    }

    @Override
    public void initSetters() {

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mRecyclerMenuAdapter);
        mRecyclerView.setHasFixedSize(true);
        OverScrollDecoratorHelper.setUpOverScroll(mRecyclerView, OverScrollDecoratorHelper.ORIENTATION_VERTICAL);
        mRecyclerMenuAdapter.setLessonManager(((MainActivity) getActivity()).getmLessonManager());
    }
}
