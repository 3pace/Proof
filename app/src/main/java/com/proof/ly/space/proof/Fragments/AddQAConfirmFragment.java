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
import com.proof.ly.space.proof.Helpers.DBManager;
import com.proof.ly.space.proof.Helpers.QManager;
import com.proof.ly.space.proof.Helpers.USManager;
import com.proof.ly.space.proof.Interfaces.FragmentInterface;
import com.proof.ly.space.proof.MainActivity;
import com.proof.ly.space.proof.R;


/**
 * Created by aman on 27.03.18.
 */

public class AddQAConfirmFragment extends Fragment implements FragmentInterface{
    private TextView txt_title;
    private Button btn_confirm;
    private QManager qManager;
    private DBManager dbManager;
    public static Fragment getInstance(){
        return new AddQAConfirmFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        qManager = ((MainActivity)getActivity()).getqManager();
        dbManager = ((MainActivity)getActivity()).getDbManager();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_q_a_confirm,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initViews(view);
        initObjects();
        initSetters();
        initTypeface();
        initOnClick();
    }

    @Override
    public void initViews(View itemView) {
        txt_title = itemView.findViewById(R.id.txt_confirm_title);
        btn_confirm = itemView.findViewById(R.id.btn_confirm);
    }

    @Override
    public void initTypeface() {
        Typeface typeface = ((MainActivity) getActivity()).getTypeface();
        txt_title.setTypeface(typeface);
        btn_confirm.setTypeface(typeface);
    }

    @Override
    public void initOnClick() {
        btn_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addQuestion();
                getActivity().onBackPressed();
            }
        });
    }

    @Override
    public void initObjects() {

    }

    @Override
    public void initSetters() {

    }
    public void addQuestion(){
        String q = qManager.getJson();
        dbManager.addQuestion(q, USManager.getUID());
    }
}
