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

    private TextView txt_info;
    private Button btn_start;

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
        // Inflate the layout for this fragment
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
        btn_start = itemView.findViewById(R.id.btn_start);
        txt_info = itemView.findViewById(R.id.txt_info);
    }

    @Override
    public void initTypeface() {
        Typeface typeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/ubuntum.ttf");
        btn_start.setTypeface(typeface);
        txt_info.setTypeface(typeface);
    }


    @Override
    public void initOnClick() {
        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MTestingFragment.startTesting();
            }
        });

    }

    @Override
    public void initObjects() {

    }

    @Override
    public void initSetters() {

        if (MTestingFragment.loading){
            txt_info.setText("");
        }else {
            int qCount = getQuestionsCount();

            if (qCount == 0) {
                nullAnswers();
                if (((MainActivity) getActivity()).getmDBManager().isAllQuestionsViewed()) {
                    btn_start.setText(getResources().getString(R.string.new_cycle).toUpperCase());
                    btn_start.setVisibility(View.VISIBLE);
                    btn_start.setClickable(true);
                    btn_start.setOnClickListener(new View.OnClickListener() {
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

            txt_info.setText(text.toString());
        }
    }
    private void nullAnswers(){
        txt_info.setText(getResources().getString(R.string.no_questions));
        btn_start.setVisibility(View.INVISIBLE);
        btn_start.setClickable(false);
        txt_info.setTextColor(getResources().getColor(R.color.colorAccent));


    }

    private int getQuestionsCount(){
        return ((MainActivity) getActivity()).getmQuestionManager().getQ().size();
    }


}
