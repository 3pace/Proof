package com.proof.ly.space.proof.Fragments;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.proof.ly.space.proof.Adapters.RecyclerTestingAdapter;
import com.proof.ly.space.proof.Data.Answer;
import com.proof.ly.space.proof.Data.Question;
import com.proof.ly.space.proof.Fragments.windows.MTestingFragment;
import com.proof.ly.space.proof.Helpers.SettingsManager;
import com.proof.ly.space.proof.Interfaces.FragmentInterface;
import com.proof.ly.space.proof.Interfaces.OnItemClick;
import com.proof.ly.space.proof.MainActivity;
import com.proof.ly.space.proof.R;

import java.util.ArrayList;

import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;

/**
 * Created by aman on 3/12/18.
 */

public class TestingFragment extends Fragment implements FragmentInterface {
    private int mFragmentPosition;
    private RecyclerView mRecyclerView;
    private RecyclerTestingAdapter mTestingAdapter;
    private ArrayList<Question> mArrayListQuestions = new ArrayList<>();
    private ArrayList<Answer> mArrayListAnswers = new ArrayList<>();
    private int mCorrectCount = 0;
    private int mCheckedCount = 0;
    private int mCorrectAnswersCount = 0;
    private int mCorrectCheckedAnswersCount = 0;
    private int mCorrectCheckedQuestionsCount = 0;
    private int mNotCorrectCheckedQuestionsCount = 0;
    private int mNotCorrectCheckedAnswersCount = 0;
    private int mDisabledColor, mClickedColor;
    private MainActivity mActivity;


    public static Fragment getInstance(int position) {
        Bundle bundle = new Bundle();
        bundle.putInt("pos", position);
        TestingFragment tabFragment = new TestingFragment();
        tabFragment.setArguments(bundle);


        return tabFragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mActivity = (MainActivity) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mFragmentPosition = getArguments().getInt("pos");

        mArrayListQuestions = mActivity.getQuestionManager().getQ();
        mCorrectAnswersCount = mArrayListQuestions.get(mFragmentPosition).getCorrectAnswersCount();
        mArrayListQuestions.get(mFragmentPosition).setLeftCorrectClickCount(mCorrectAnswersCount);
        mCorrectCount = mArrayListQuestions.get(mFragmentPosition).getLeftCorrectClickCount();
        mNotCorrectCheckedAnswersCount = mArrayListQuestions.get(mFragmentPosition).getNotCorrectCheckedQuestionsCount();

        mDisabledColor = mActivity.getDisabledColor();
        mClickedColor = mActivity.getClickedColor();
        initObjects();

    }


    @Override
    public View onCreateView(@Nullable LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_testing, container, false);
        initViews(view);

        return view;
    }

    @Override
    public void onViewCreated(@Nullable View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initTypeface();
        initOnClick();
        initSetters();

    }


    @Override
    public void initViews(View itemView) {
        mRecyclerView = itemView.findViewById(R.id.rview);


    }

    @Override
    public void initTypeface() {
        Typeface typeface = mActivity.getTypeface();
        mTestingAdapter.setTypeface(typeface);

    }

    @Override
    public void initOnClick() {

    }

    @Override
    public void initObjects() {
        mTestingAdapter = new RecyclerTestingAdapter(mArrayListQuestions.get(mFragmentPosition).getArrayListAnswers());
        mTestingAdapter.setDisabledColor(mDisabledColor, mClickedColor);


    }

    @Override
    public void initSetters() {
        StringBuilder text = new StringBuilder(String.valueOf((mFragmentPosition + 1)));
        text = mCorrectAnswersCount > 1 ?
                text.append(". ").append(mArrayListQuestions.get(mFragmentPosition).getQuestion()).append(" (").append(mCorrectAnswersCount).append(" отв.)") :
                text.append(". ").append(mArrayListQuestions.get(mFragmentPosition).getQuestion());

        mTestingAdapter.setQuestion(text.toString());


        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mTestingAdapter);
        OverScrollDecoratorHelper.setUpOverScroll(mRecyclerView, OverScrollDecoratorHelper.ORIENTATION_VERTICAL);
        mTestingAdapter.setOnItemClick(new OnItemClick() {
            @Override
            public void onClick(int clickPosition) {
                int answersSize;
                mCorrectCount = mArrayListQuestions.get(mFragmentPosition).getLeftCorrectClickCount();
                log("before: " + mCorrectCount);
                mArrayListAnswers = mArrayListQuestions.get(mFragmentPosition).getArrayListAnswers();
                answersSize = mArrayListAnswers.size();

                for (int i = 0; i < answersSize; i++) {
                    mArrayListAnswers.get(i).setEnabled(mArrayListAnswers.get(i).isEnabled());
                }

                mCorrectCount -= 1; //сколько раз можно нажать
                mArrayListAnswers.get(clickPosition).setEnabled(false);
                mArrayListAnswers.get(clickPosition).setClicked(true);
                if (mArrayListAnswers.get(clickPosition).isCorrect()) {
                    mArrayListAnswers.get(clickPosition).setIsChecked(1);
                    mArrayListAnswers.get(clickPosition).setCorrectChecked(true);
                    mCorrectCheckedAnswersCount++;

                    switch (mCorrectAnswersCount) {
                        case 1:
                            if (mCorrectCheckedAnswersCount >= 1)
                                mArrayListQuestions.get(mFragmentPosition).setCorrectChecked(true);
                            break;
                        case 2:
                            if (mCorrectCheckedAnswersCount >= 1)
                                mArrayListQuestions.get(mFragmentPosition).setCorrectChecked(true);
                            break;
                        case 3:
                            if (mCorrectCheckedAnswersCount >= 2)
                                mArrayListQuestions.get(mFragmentPosition).setCorrectChecked(true);
                            break;
                        default:
                            if (mCorrectCheckedAnswersCount >= 2)
                                mArrayListQuestions.get(mFragmentPosition).setCorrectChecked(true);
                            break;

                    }

                    /*mArrayListQuestions.get(mFragmentPosition).setCorrectCheckedQuestionsCount(mCorrectCheckedQuestionsCount);
                    эта идея не прокатила, ведь можно просто использовать
                    условие isCorrectChecked для подсчета правильно отвеченных вопросов*/

                } else {

                    mNotCorrectCheckedAnswersCount++;//количсетво неправильно отвеченных ответов


                    if (mCorrectAnswersCount >= 2) { //если количесво правильных ответов в вопросе больше 2
                        if (mNotCorrectCheckedAnswersCount == 2) {
                            //и проверяем если мы уже совершили 2 ошибки, то можно останавливать.
                            // Т.к. если в вопросе 3 или 2 прав. ответа, то при 2 ошибках считается что неправильно ответили на вопрос
                            log("" + mNotCorrectCheckedAnswersCount);
                            mArrayListAnswers.get(clickPosition).setCorrectChecked(false);
                            mArrayListQuestions.get(mFragmentPosition).setCorrectChecked(false);

                           /* mArrayListQuestions.get(mFragmentPosition).setNotCorrectCheckedQuestionsCount(mNotCorrectCheckedQuestionsCount);
                            эта идея не прокатила, ведь можно просто использовать
                            условие isCorrectChecked для подсчета правильно отвеченных вопросов*/

                            if (SettingsManager.colored)
                                mCorrectCount = 0; //если вклчн. быстрый показ прв. отв. то при ошибке сразу останвлиаем
                        }
                    } else {
                        //если ответов меньше чем 2, значит в вопросе один ответ, и следовательно, вопрос счиатется неправильно отвеченным
                        mArrayListQuestions.get(mFragmentPosition).setNotCorrectCheckedQuestionsCount(mNotCorrectCheckedAnswersCount);
                    }
                    mArrayListAnswers.get(clickPosition).setIsChecked(1);

                    mArrayListQuestions.get(mFragmentPosition).setNotCorrectCheckedAnswersCount(mNotCorrectCheckedAnswersCount);


                }

                for (int i = 0; i < answersSize; i++) {
                    if (mArrayListAnswers.get(i).getIsChecked() != 1) {
                        mArrayListAnswers.get(i).setIsChecked(2); //делаем не выбранные варианты серого цвета

                    }
                }


                if (mCorrectCount == 0)
                    for (int j = 0; j < answersSize; j++) {
                        if (mArrayListAnswers.get(j).getIsChecked() != 1) {
                            mArrayListAnswers.get(j).setIsChecked(2);
                            if (mArrayListAnswers.get(j).isCorrect()) {
                                mArrayListAnswers.get(j).setIsChecked(1);
                                mArrayListAnswers.get(j).setClicked(false);
                                mArrayListAnswers.get(j).setCorrectChecked(true);
                            }
                        }
                    }


                mArrayListQuestions.get(mFragmentPosition).setLeftCorrectClickCount(mCorrectCount);
                log("after: " + mArrayListQuestions.get(mFragmentPosition).getLeftCorrectClickCount() + "");
                mArrayListQuestions.get(mFragmentPosition).setCorrectCheckedAnswersCount(mCorrectCheckedAnswersCount);

                if (mCorrectCount <= 0) {
                    for (int i = 0; i < answersSize; i++) {
                        mArrayListAnswers.get(i).setEnabled(false);
                    }
                    addViewedQuestion();
                    mArrayListQuestions.get(mFragmentPosition).setChecked(true);
                    MTestingFragment.nextPage(mArrayListQuestions.size());

                }

                mCheckedCount = 0;
                int questionsSize = mArrayListQuestions.size();
                for (int i = 0; i < questionsSize; i++)
                    if (mArrayListQuestions.get(i).isChecked()) {
                        mCheckedCount++;
                    }

                log("checked c: " + mCheckedCount + ", size: " + mArrayListQuestions.size());
                if (mCheckedCount >= questionsSize) {
                    mActivity.getQuestionManager().stopTesting();
                    //MTestingFragment.finishTesting(mArrayListQuestions.size());
                    mActivity.stopTimer();
                    mActivity.replaceFragment(new ResultFragment(), getResources().getString(R.string.tag_result));

                }

                mTestingAdapter.notifyDataSetChanged();

            }


        });

    }

    private void addViewedQuestion() {
        if (SettingsManager.cycleMode) {
            int qId = mArrayListQuestions.get(mFragmentPosition).getQuestionId();
            mActivity.getDatabaseManager().addViewedQuestion(qId); //добавялем id вопроса в список просмотренных
            Log.d("test", "question added id: " + qId);
        } else {
            Log.d("test", "cycle mode disabled");
        }
    }


    public RecyclerTestingAdapter getAdapter() {
        return mTestingAdapter;
    }

    public void log(String l) {
        Log.d("Test", l);
    }


}