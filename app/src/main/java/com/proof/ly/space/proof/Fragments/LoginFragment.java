package com.proof.ly.space.proof.Fragments;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.proof.ly.space.proof.Fragments.windows.MAuthFragment;
import com.proof.ly.space.proof.Helpers.DBManager;
import com.proof.ly.space.proof.Interfaces.FragmentInterface;
import com.proof.ly.space.proof.MainActivity;
import com.proof.ly.space.proof.R;
import com.proof.ly.space.proof.Utils.ClickEffect;

/**
 * Created by aman on 29.03.18.
 */

public class LoginFragment extends Fragment implements FragmentInterface{

    private TextView txt_next,txt_register;
    private EditText etxt_login,etxt_pswrd;
    private DBManager dbManager;

    public static Fragment getInstance(){
        return new LoginFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbManager = ((MainActivity)getActivity()).getDbManager();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_login,container,false);
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
        etxt_login = itemView.findViewById(R.id.etxt_login);
        etxt_pswrd = itemView.findViewById(R.id.etxt_password);
        txt_next = itemView.findViewById(R.id.txt_next);
        txt_register = itemView.findViewById(R.id.txt_register);
    }

    @Override
    public void initTypeface() {
        Typeface typeface = ((MainActivity) getActivity()).getTypeface();
        etxt_login.setTypeface(typeface);
        etxt_pswrd.setTypeface(typeface);
        txt_next.setTypeface(typeface);
        txt_register.setTypeface(typeface);
    }

    @Override
    public void initOnClick() {
        txt_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = etxt_login.getText().toString().trim().toUpperCase();
                String password = etxt_pswrd.getText().toString().trim();
                if (dbManager.logIn(username,password)){
                    getActivity().onBackPressed();
                }else Log.d("TEST", "onClick: "+false);

            }
        });
        txt_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MAuthFragment.toRegisterPage();
            }
        });
    }

    @Override
    public void initObjects() {

    }

    @Override
    public void initSetters() {
        ClickEffect.setView(txt_next);
        ClickEffect.setView(txt_register);
    }

    private void confirm(){

    }
}
