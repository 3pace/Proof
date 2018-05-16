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

import com.proof.ly.space.proof.Fragments.windows.TMenuFragment;
import com.proof.ly.space.proof.Interfaces.FragmentInterface;
import com.proof.ly.space.proof.MainActivity;
import com.proof.ly.space.proof.R;

/**
 * Created by aman on 29.03.18.
 */

public class RegisterFragment extends Fragment implements FragmentInterface {

    private TextView mTextView;
    private Button mButton;

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
        mTextView = itemView.findViewById(R.id.txt_info);
        mButton = itemView.findViewById(R.id.btn_back);

    }

    @Override
    public void initTypeface() {
        Typeface typeface = ((MainActivity) getActivity()).getTypeface();
        mTextView.setTypeface(typeface);
        mButton.setTypeface(typeface);
    }

    @Override
    public void initOnClick() {
        mButton.setOnClickListener(new View.OnClickListener() {
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
