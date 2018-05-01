package com.proof.ly.space.proof.Fragments;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.proof.ly.space.proof.Adapters.RecyclerTestingAdapter;
import com.proof.ly.space.proof.Data.Answers;
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
    private int position;
    private RecyclerView rview;
    private RecyclerTestingAdapter radapter;
    private ArrayList<Question> questions = new ArrayList<>();
    private ArrayList<Answers> answers = new ArrayList<>();
    private int correctCount = 0;
    private int checkedCount = 0;
    private int cCount = 0;
    private int cAnswers = 0;
    private int notcAnswers = 0;
    private int notCorrectCheckedCount = 0;
    private int disabledColor,clickedColor;


    public static Fragment getInstance(int position) {
        Bundle bundle = new Bundle();
        bundle.putInt("pos", position);
        TestingFragment tabFragment = new TestingFragment();
        tabFragment.setArguments(bundle);

        return tabFragment;
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        position = getArguments().getInt("pos");

        questions = ((MainActivity) getActivity()).getqManager().getQ();
        cCount = questions.get(position).getcCount();
        questions.get(position).setLeftCorrectCount(cCount);
        correctCount = questions.get(position).getLeftCorrectCount();
        notCorrectCheckedCount = questions.get(position).getNotCorrect();

        disabledColor = ((MainActivity)getActivity()).getDisabledColor();
        clickedColor = ((MainActivity)getActivity()).getClickedColor();
        initObjects();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_testing, container, false);
        initViews(view);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initTypeface();
        initOnClick();
        initSetters();

    }


    @Override
    public void initViews(View itemView) {
        rview = itemView.findViewById(R.id.rview);


    }

    @Override
    public void initTypeface() {
        Typeface typeface = ((MainActivity) getActivity()).getTypeface();
        radapter.setTypeface(typeface);

    }

    @Override
    public void initOnClick() {

    }

    @Override
    public void initObjects() {
        radapter = new RecyclerTestingAdapter(questions.get(position).getAnswers());
        radapter.setDisabledColor(disabledColor, clickedColor);


    }

    @Override
    public void initSetters() {
        StringBuilder text = new StringBuilder(String.valueOf((position + 1)));
        text = cCount > 1 ?
                text.append(". ").append(questions.get(position).getQ()).append(" (").append(cCount).append(" отв.)"):
                text.append(". ").append(questions.get(position).getQ());

        radapter.setQuestion(text.toString().toUpperCase());

        rview.setLayoutManager(new LinearLayoutManager(getContext()));
        rview.setAdapter(radapter);
        OverScrollDecoratorHelper.setUpOverScroll(rview, OverScrollDecoratorHelper.ORIENTATION_VERTICAL);
        radapter.setOnItemClick(new OnItemClick() {
            @Override
            public void onClick(int pos) {
                int answersSize;
                correctCount = questions.get(position).getLeftCorrectCount();
                log("before: " + correctCount);
                answers = questions.get(position).getAnswers();
                answersSize = answers.size();

                for (int i = 0; i < answersSize; i++) {
                    answers.get(i).setEnabled(answers.get(i).isEnabled());
                }

                correctCount -= 1; //сколько ра можно нажать
                answers.get(pos).setEnabled(false);
                answers.get(pos).setClicked(true);
                if (answers.get(pos).isCorrect()) {
                    answers.get(pos).setChecked(1);
                    answers.get(pos).setCorrectChecked(true);
                    cAnswers++;
                } else {

                    notCorrectCheckedCount++;//количсетво неправильно отвеченных ответов
                    questions.get(position).setNotCorrect(notCorrectCheckedCount);
                    if (cCount >= 2) //если количесво правильных ответов в вопросе больше 2
                        if (notCorrectCheckedCount == 2) {
                            //и проверяем если мы уже совершили 2 ошибки, то можно останавливать.
                            // Т.к. если в вопросе 3 или 2 прав. ответа, то при 2 ошибках считается что неправильно ответили на вопрос
                            log("" + notCorrectCheckedCount);
                            answers.get(pos).setCorrectChecked(false);
                            notcAnswers++;
                            if (SettingsManager.colored)
                                correctCount = 0; //если вклчн. быстрый показ прв. отв. то при ошибке сразу останвлиаем
                        }

                    answers.get(pos).setChecked(1);


                }
                for (int i = 0; i < answersSize; i++) {
                    if (answers.get(i).getChecked() != 1) {
                        answers.get(i).setChecked(2); //делаем не выбранные варианты серого цвета

                    }
                }


                if (correctCount == 0)
                    for (int j = 0; j < answersSize; j++) {
                        if (answers.get(j).getChecked() != 1) {
                            answers.get(j).setChecked(2);
                            if (answers.get(j).isCorrect()) {
                                answers.get(j).setChecked(1);
                                answers.get(j).setClicked(false);
                                answers.get(j).setCorrectChecked(true);
                            }
                        }
                    }


                questions.get(position).setLeftCorrectCount(correctCount);
                log("after: " + questions.get(position).getLeftCorrectCount() + "");
                questions.get(position).setcAnswers(cAnswers);
                questions.get(position).setNotcAnswers(notcAnswers);
                if (correctCount <= 0) {
                    for (int i = 0; i < answersSize; i++) {
                        answers.get(i).setEnabled(false);
                    }
                    addViewedQuestion();
                    questions.get(position).setChecked(true);
                    MTestingFragment.nextPage(questions.size());

                }

                checkedCount = 0;
                int questionsSize = questions.size();
                for (int i = 0; i < questionsSize; i++)
                    if (questions.get(i).isChecked()) {
                        checkedCount++;
                    }

                log("checked c: " + checkedCount + ", size: " + questions.size());
                if (checkedCount >= questionsSize) {
                    ((MainActivity) getActivity()).getqManager().stopTesting();
                    MTestingFragment.finishTesting(questions.size());

                }

                radapter.notifyDataSetChanged();

            }


        });

    }

    private void addViewedQuestion() {
        if (SettingsManager.cycleMode) {
            int qId = questions.get(position).getqId();
            ((MainActivity) getActivity()).getDbManager().addViewedQuestion(qId); //добавялем id вопроса в список просмотренных
            Log.d("test", "question added id: " + qId);
        } else {
            Log.d("test", "cycle mode disabled");
        }
    }


    public RecyclerTestingAdapter getAdapter() {
        return radapter;
    }

    public void log(String l) {
        Log.d("Test", l);
    }


}