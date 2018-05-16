package com.proof.ly.space.proof.Fragments;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.graphics.PorterDuff;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.proof.ly.space.proof.Helpers.MConstans;
import com.proof.ly.space.proof.Interfaces.FragmentInterface;
import com.proof.ly.space.proof.MainActivity;
import com.proof.ly.space.proof.R;

import java.util.HashMap;

/**
 * A simple {@link Fragment} subclass.
 */
public class ResultFragment extends Fragment implements FragmentInterface {

    private TextView mTextViewResult, mTextViewFinish, mTextViewResultPercent;
    private ImageView mImageViewResultIcon;
    private Button mButtonRestart;
    private boolean mInit = false;
    private boolean mOnceIconAnimation = false;

    public ResultFragment() {

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
        mInit = true;
        mTextViewResult = itemView.findViewById(R.id.txt_result);
        mTextViewFinish = itemView.findViewById(R.id.txt_finish_title);
        mButtonRestart = itemView.findViewById(R.id.btn_restart);
        mTextViewResultPercent = itemView.findViewById(R.id.txt_result_percent);
        mImageViewResultIcon = itemView.findViewById(R.id.img_view_result);
    }

    @Override
    public void initTypeface() {
        Typeface typeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/ubuntum.ttf");
        mTextViewResult.setTypeface(typeface);
        mTextViewFinish.setTypeface(typeface);
        mButtonRestart.setTypeface(typeface);
        mTextViewResultPercent.setTypeface(typeface);
    }

    @Override
    public void initOnClick() {

        mButtonRestart.setOnClickListener(new View.OnClickListener() {
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




        HashMap<String, Integer> resultMap = ((MainActivity) getActivity()).getmQuestionManager().getTestingResult();
        int correctQuestions = resultMap.get(MConstans.TESTING_RESULT_CORRECT_QUESTIONS_COUNT);
        int notCorrectQuestions = resultMap.get(MConstans.TESTING_RESULT_NOT_CORRECT_QUESTIONS_COUNT);
        int totalPoints = resultMap.get(MConstans.TESTING_RESULT_POINTS);
        int mistakes = resultMap.get(MConstans.TESTING_RESULT_MISTAKES);
        int percent = resultMap.get(MConstans.TESTING_RESULT_PERCENT);
        int notChecked = resultMap.get(MConstans.TESTING_RESULT_NOT_CHECKED_COUNT);
        int questionsCount = resultMap.get(MConstans.TESTING_RESULT_QUESTIONS_COUNT);

        StringBuilder resultText = new StringBuilder("");
        resultText
                .append(getResources().getString(R.string.result))
                .append(" ")
                .append(correctQuestions)
                .append(" ")
                .append(getResources().getString(R.string.from).toLowerCase())
                .append(" ")
                .append(questionsCount)
                .append(".\n")
                .append(getResources().getString(R.string.vy_nabrali))
                .append(" ")
                .append(totalPoints)
                .append(" ")
                .append(getWord(totalPoints,"баллов","балл","балла"))
                .append(", ")
                .append(getResources().getString(R.string.sovershili))
                .append(" ")
                .append(mistakes)
                .append(" ")
                .append(getWord(mistakes,"ошибок","ошибку","ошибки"));
        if (notChecked != 0)
            resultText
                    .append(" ")
                    .append(getResources().getString(R.string.ne_otevili))
                    .append(" ")
                    .append(notChecked)
                    .append(" ")
                    .append(getWord(notChecked,"вопросов","вопрос","вопроса"))
                    .append(".");
        else
            resultText.append(".");
        mTextViewResult.setText(resultText);
        startCountAnimation("%", 0, percent, 3000);

        Drawable mResultIcon = getResultIcon(percent);
        mResultIcon.setColorFilter(((MainActivity) getActivity()).getClickedColor(), PorterDuff.Mode.SRC_ATOP);
        mImageViewResultIcon.setImageDrawable(mResultIcon);

        System.out.println("\ncorrect questions: " + resultMap.get(MConstans.TESTING_RESULT_CORRECT_QUESTIONS_COUNT));
        System.out.println("not correct questions: " + resultMap.get(MConstans.TESTING_RESULT_NOT_CORRECT_QUESTIONS_COUNT));
        System.out.println("points: " + resultMap.get(MConstans.TESTING_RESULT_POINTS));
        System.out.println("mistakes: " + resultMap.get(MConstans.TESTING_RESULT_MISTAKES));
        System.out.println("percent: " + resultMap.get(MConstans.TESTING_RESULT_PERCENT) + "%");
        System.out.println("not checked: " + resultMap.get(MConstans.TESTING_RESULT_NOT_CHECKED_COUNT));

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser && mInit) {
            initSetters();
        }
    }

    public String getWord(int count, String word0,String word1,String word2) {
        int rem = count % 100;
        if(rem < 11 || rem > 14){
            rem = count % 10;
            if(rem == 1) return word1;
            if(rem >= 2 && rem <= 4) return word2;
        } return word0;
    }

    private void startCountAnimation(final String suffix, int start, int end, long duration) {
        mImageViewResultIcon.setVisibility(View.INVISIBLE);
        ValueAnimator animator = ValueAnimator.ofInt(start, end);
        animator.setDuration(duration);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator animation) {
                mTextViewResultPercent.setText(animation.getAnimatedValue().toString().concat(suffix));
            }
        });
        animator.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                mImageViewResultIcon.setVisibility(View.VISIBLE);
                blinkView();
            }
        });
        animator.start();
    }

    private void blinkView(){

        Animation anim = new AlphaAnimation(0.0f, 1.0f);
        anim.setDuration(40); //You can manage the blinking time with this parameter
        anim.setStartOffset(20);
        anim.setRepeatMode(Animation.REVERSE);
        anim.setRepeatCount(3);
        if (!mOnceIconAnimation) {
            mImageViewResultIcon.startAnimation(anim);
            mOnceIconAnimation = true;
        }

    }

    private Drawable getResultIcon(int totalPercent){
        if (totalPercent > -100 && totalPercent < 30)
            return getResources().getDrawable(R.drawable.baseline_sentiment_very_dissatisfied_black_36);
        else if (totalPercent >= 30 && totalPercent < 60)
            return getResources().getDrawable(R.drawable.baseline_sentiment_satisfied_black_36);
        else
            return getResources().getDrawable(R.drawable.baseline_sentiment_very_satisfied_black_36);
    }

}
