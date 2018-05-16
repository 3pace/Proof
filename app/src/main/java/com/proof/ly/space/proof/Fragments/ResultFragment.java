package com.proof.ly.space.proof.Fragments;


import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.proof.ly.space.proof.Interfaces.FragmentInterface;
import com.proof.ly.space.proof.MainActivity;
import com.proof.ly.space.proof.R;

import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class ResultFragment extends Fragment implements FragmentInterface {

    private TextView txt_result,txt_finish_title;
    private Button btn_restart;
    private boolean init = false;

    public ResultFragment() {
        // Required empty public constructor
    }
    public static Fragment getInstance(int position) {
        Bundle bundle = new Bundle();
        bundle.putInt("pos", position);
        ResultFragment fragment = new ResultFragment();
        fragment.setArguments(bundle);

        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_result, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        initObjects();
        initTypeface();
        initOnClick();
        initSetters();
    }

    @Override
    public void initViews(View itemView) {
        init = true;
        txt_result = itemView.findViewById(R.id.txt_result);
        txt_finish_title = itemView.findViewById(R.id.txt_finish_title);
        btn_restart = itemView.findViewById(R.id.btn_restart);
    }

    @Override
    public void initTypeface() {
        Typeface typeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/ubuntum.ttf");
        txt_result.setTypeface(typeface);
        txt_finish_title.setTypeface(typeface);
        btn_restart.setTypeface(typeface);
    }

    @Override
    public void initOnClick() {

        btn_restart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();


            }
        });
    }

    @Override
    public void initObjects() {

    }

    @Override
    public void initSetters() {
        txt_finish_title.setAlpha(0);
        txt_result.setAlpha(0);
        txt_finish_title.animate().alpha(1).setDuration(600).start();
        txt_result.animate().alpha(1).setDuration(600).start();

        HashMap<String, Integer> rmap = ((MainActivity) getActivity()).getmQuestionManager().getNotClickedAnswers();
        int points = ((MainActivity) getActivity()).getmQuestionManager().getResult();
        int nchecked = rmap.get("notclicked");
        int ncorrects = rmap.get("notcorrect");

        StringBuilder text = new StringBuilder(getResources().getString(R.string.vy_nabrali));
        text
                .append(" ")
                .append(points)
                .append(" ")
                .append(getWord(points,"баллов","балл","балла"))
                .append(", ")
                .append(getResources().getString(R.string.sovershili))
                .append(" ")
                .append(ncorrects)
                .append(" ")
                .append(getWord(ncorrects,"ошибок","ошибку","ошибки"));
        if (nchecked != 0)
            text
                    .append(" ")
                    .append(getResources().getString(R.string.ne_otevili))
                    .append(" ")
                    .append(nchecked)
                    .append(" ")
                    .append(getWord(nchecked,"вопросов","вопрос","вопроса"));

        txt_result.setText(text.toString());

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && init) {
            initSetters();
        }
    }

    public static String getWord(int count, String word0,String word1,String word2) {
        int rem = count % 100;
        if(rem < 11 || rem > 14){
            rem = count % 10;
            if(rem == 1) return word1;
            if(rem >= 2 && rem <= 4) return word2;
        } return word0;
    }

}
