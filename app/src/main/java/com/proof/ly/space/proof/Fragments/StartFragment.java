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

import com.proof.ly.space.proof.Fragments.windows.MTestingFragment;
import com.proof.ly.space.proof.Helpers.SettingsManager;
import com.proof.ly.space.proof.Interfaces.FragmentInterface;
import com.proof.ly.space.proof.MainActivity;
import com.proof.ly.space.proof.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class StartFragment extends Fragment implements FragmentInterface {

    private TextView mTextViewInfo;
    private Button mButtonStart;

    public StartFragment() {
    }

    public static Fragment getInstance(int position) {
        Bundle bundle = new Bundle();
        bundle.putInt("pos", position);
        StartFragment fragment = new StartFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_start, container, false);
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
        mButtonStart = itemView.findViewById(R.id.btn_start);
        mTextViewInfo = itemView.findViewById(R.id.txt_info);
    }

    @Override
    public void initTypeface() {
        Typeface typeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/ubuntum.ttf");
        mButtonStart.setTypeface(typeface);
        mTextViewInfo.setTypeface(typeface);
    }


    @Override
    public void initOnClick() {
        mButtonStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MTestingFragment.startTesting();
                ((MainActivity) getActivity()).startTimer();
            }
        });

    }

    @Override
    public void initObjects() {

    }

    @Override
    public void initSetters() {

        if (MTestingFragment.loading) {
            mTextViewInfo.setText("");
        } else {
            int qCount = getQuestionsCount();

            if (qCount == 0) {
                nullAnswers();
                if (((MainActivity) getActivity()).getmDBManager().isAllQuestionsViewed()) {
                    mButtonStart.setText(getResources().getString(R.string.new_cycle).toUpperCase());
                    mButtonStart.setVisibility(View.VISIBLE);
                    mButtonStart.setClickable(true);
                    mButtonStart.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            ((MainActivity) getActivity()).getmDBManager().newCycle();
                            getActivity().onBackPressed();
                        }
                    });

                }
                return;
            }
            StringBuilder text = new StringBuilder(getResources().getString(R.string.question_size));
            text
                    .append(" ")
                    .append(String.valueOf(qCount));
            if (SettingsManager.cycleMode)
                text
                        .append("\n")
                        .append(getResources().getString(R.string.viewed_questions))
                        .append(" ")
                        .append(((MainActivity) getActivity()).getmDBManager().getViewedPercent())
                        .append("%")
                        .append("\n")
                        .append(getResources().getString(R.string.cycle))
                        .append(" ")
                        .append(((MainActivity) getActivity()).getmDBManager().getCycleNum())
                        ;

            mTextViewInfo.setText(text.toString());
        }
    }

    private void nullAnswers() {
        mTextViewInfo.setText(getResources().getString(R.string.no_questions));
        mButtonStart.setVisibility(View.INVISIBLE);
        mButtonStart.setClickable(false);
        mTextViewInfo.setTextColor(getResources().getColor(R.color.colorAccent));


    }

    private int getQuestionsCount() {
        return ((MainActivity) getActivity()).getmQuestionManager().getQ().size();
    }


}
