package com.proof.ly.space.proof.Data;

import java.util.ArrayList;

/**
 * Created by aman on 16.03.18.
 */

public class Question {
    private int mQuestionId;
    private String mQuestion;
    private ArrayList<Answers> mArrayListAnswers;
    private int mCorrectAnswersCount;
    private boolean mIsChecked = false;
    private boolean mIsCorrectChecked = false;
    private int mCorrectCheckedAnswersCount = 0;
    private int mNotCorrectCheckedAnswersCount = 0;
    private int mLeftCorrectClickCount = 0;
    private boolean created = false;
    private int mNotCorrectCheckedQuestionsCount = 0;

    public Question(int mQuestionId, String mQuestion, ArrayList<Answers> mArrayListAnswers, int mCorrectAnswersCount){
        this.mQuestionId = mQuestionId;
        this.mQuestion = mQuestion;
        this.mArrayListAnswers = mArrayListAnswers;
        this.mCorrectAnswersCount = mCorrectAnswersCount;


    }




    public int getQuestionId() {
        return mQuestionId;
    }

    public String getQuestion() {
        return mQuestion;
    }

    public int getCorrectAnswersCount() {
        return mCorrectAnswersCount;
    }
    public void setChecked(boolean checked){
        this.mIsChecked = checked;
    }

    public void setCorrectCheckedAnswersCount(int correctCheckedAnswersCount) {
        this.mCorrectCheckedAnswersCount = correctCheckedAnswersCount;
    }

    public void setNotCorrectCheckedAnswersCount(int notCorrectCheckedAnswersCount) {
        this.mNotCorrectCheckedAnswersCount = notCorrectCheckedAnswersCount;
    }

    public int getNotCorrectCheckedAnswersCount() {
        return mNotCorrectCheckedAnswersCount;
    }

    public int getCorrectCheckedAnswersCount() {
        return mCorrectCheckedAnswersCount;
    }

    public boolean isChecked() {
        return mIsChecked;
    }

    public boolean isCorrectChecked() {
        return mIsCorrectChecked;
    }

    public void setCorrectChecked(boolean correctChecked) {
        this.mIsCorrectChecked = correctChecked;
    }

    public ArrayList<Answers> getArrayListAnswers() {
        return mArrayListAnswers;
    }

    public void setLeftCorrectClickCount(int leftCorrectClickCount) {
        this.mLeftCorrectClickCount = leftCorrectClickCount;
    }

    public int getLeftCorrectClickCount() {
        return mLeftCorrectClickCount;
    }

    public void setCreated(boolean created) {
        this.created = created;
    }

    public boolean isCreated() {
        return created;
    }



    public int getNotCorrectCheckedQuestionsCount() {
        return mNotCorrectCheckedQuestionsCount;
    }

    public void setNotCorrectCheckedQuestionsCount(int notCorrectCheckedQuestionsCount) {
        this.mNotCorrectCheckedQuestionsCount = notCorrectCheckedQuestionsCount;
    }
}
