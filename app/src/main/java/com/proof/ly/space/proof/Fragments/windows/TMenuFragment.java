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
import android.widget.TextView;
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
    private TextView txt_tbar;
    private Typeface tf;
    private RecyclerMenuAdapter adapter;
    private RecyclerView rview;
    private ArrayList<String> arrayList = new ArrayList<>();
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


        arrayList.add(getResources().getString(R.string.testing));
        arrayList.add(getResources().getString(R.string.question_db));
        arrayList.add(getResources().getString(R.string.search_title));
        arrayList.add(getResources().getString(R.string.settings));

        //adapter.notifyDataSetChanged();
    }
    public void updateAdapter(){

        adapter.notifyDataSetChanged();
        rview.setAdapter(adapter);
    }

    @Override
    public void initViews(View itemView) {
        rview = itemView.findViewById(R.id.rview);
    }

    @Override
    public void initTypeface() {
        tf = ((MainActivity) getActivity()).getTypeface();
        adapter.setTypeface(tf);
    }

    @Override
    public void initOnClick() {
        adapter.setOnItemClick(new OnItemClick() {

            @Override
            public void onClick(int pos) {
                switch (pos) {
                    case 0:
                        //startActivity(new Intent(MainActivity.this, TestingActivity.class));
                        ((MainActivity) getActivity()).replaceFragment(new MTestingFragment(), "testing");
                        break;
                    case 1:
                        //startActivity(new Intent(MainActivity.this, QuestionManagerActivity.class));
                        ((MainActivity) getActivity()).replaceFragment(new MQuestionManagerFragment());
                        break;
                    case 2:
                        //startActivity(new Intent(MainActivity.this, QuestionManagerActivity.class));
                        ((MainActivity) getActivity()).replaceFragment(new MSearchFragment());
                        break;
                    case 3:
                        ((MainActivity) getActivity()).replaceFragment(new MSettingsFragment());
                        break;

                }
            }
        });
        adapter.setOnHeaderClick(new OnItemClickView() {
            @Override
            public void onClick(int pos, View view) {
                switch (view.getId()) {
                    case R.id.btn_lesson:
                        if (!isLoading) {
                            Button btn = (Button) view;
                            ((MainActivity) getActivity()).startAsyncLoad();
                            btn.setText(((MainActivity) getActivity()).getLessonManager().changeLesson());
                        }
                        break;
                }

            }
        });
    }

    @Override
    public void initObjects() {
        adapter = new RecyclerMenuAdapter(arrayList);
        adapter.addLessonHolder(true);
    }

    @Override
    public void initSetters() {

        rview.setLayoutManager(new LinearLayoutManager(getContext()));
        rview.setAdapter(adapter);
        rview.setHasFixedSize(true);
        OverScrollDecoratorHelper.setUpOverScroll(rview, OverScrollDecoratorHelper.ORIENTATION_VERTICAL);
        adapter.setLessonManager(((MainActivity) getActivity()).getLessonManager());
    }
}
