package com.proof.ly.space.proof.Fragments;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.proof.ly.space.proof.Fragments.windows.TMenuFragment;
import com.proof.ly.space.proof.Interfaces.FragmentInterface;
import com.proof.ly.space.proof.MainActivity;
import com.proof.ly.space.proof.R;
import com.proof.ly.space.proof.Utils.ClickEffect;

/**
 * Created by aman on 29.03.18.
 */

public class RegisterFragment extends Fragment implements FragmentInterface {

    private TextView txt;
    private Button btn;

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
        txt = itemView.findViewById(R.id.txt_info);
        btn = itemView.findViewById(R.id.btn_back);

    }

    @Override
    public void initTypeface() {
        Typeface typeface = ((MainActivity) getActivity()).getTypeface();
        txt.setTypeface(typeface);
        btn.setTypeface(typeface);
    }

    @Override
    public void initOnClick() {
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity)getActivity()).replaceFragmentWithoutBackstack(new TMenuFragment(),MainActivity.MENU_FRAGMENT_TAG);
            }
        });

    }

    @Override
    public void initObjects() {

    }

    @Override
    public void initSetters() {

    }


}
