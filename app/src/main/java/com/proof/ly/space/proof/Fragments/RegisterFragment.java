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
import com.proof.ly.space.proof.Interfaces.FragmentInterface;
import com.proof.ly.space.proof.MainActivity;
import com.proof.ly.space.proof.R;
import com.proof.ly.space.proof.Utils.ClickEffect;

/**
 * Created by aman on 29.03.18.
 */

public class RegisterFragment extends Fragment implements FragmentInterface {

    private EditText etxt_login,etxt_pswrd,etxt_pswrd_c;
    private TextView txt_next;

    public static Fragment getInstance(){
        return new RegisterFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_register,container,false);
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
        etxt_pswrd_c = itemView.findViewById(R.id.etxt_password_confirm);
        txt_next = itemView.findViewById(R.id.txt_next);

    }

    @Override
    public void initTypeface() {
        Typeface typeface = ((MainActivity) getActivity()).getTypeface();
        etxt_login.setTypeface(typeface);
        etxt_pswrd.setTypeface(typeface);
        etxt_pswrd_c.setTypeface(typeface);
        txt_next.setTypeface(typeface);
    }

    @Override
    public void initOnClick() {
        txt_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = etxt_login.getText().toString().trim().toUpperCase();
                String password = etxt_pswrd.getText().toString().trim();
                String password_c = etxt_pswrd_c.getText().toString().trim();
                if (username.length() > 3 && password.equals(password_c)){
                    confirm(username,password);
                    Log.d("TEST", "onClick: "+true);
                }
            }
        });
    }

    @Override
    public void initObjects() {

    }

    @Override
    public void initSetters() {
        ClickEffect.setView(txt_next);
    }

    public void confirm(String username,String password){
        ((MainActivity)getActivity()).getDbManager().addUser(username,password);
        getActivity().onBackPressed();
    }
}
