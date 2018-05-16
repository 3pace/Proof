package com.proof.ly.space.proof.Data;

/**
 * Created by aman on 4/17/18.
 */

public class SearchData {
    private String mQuestion, mAnswer;

    public SearchData() {
    }

    public SearchData(String question, String answer) {
        this.mQuestion = question;
        this.mAnswer = answer;
    }

    public String getAnswer() {
        return mAnswer;
    }

    public String getQuestion() {
        return mQuestion;
    }

    public void setQuestion(String question) {
        this.mQuestion = question;
    }

    public void setAnswer(String answer) {
        this.mAnswer = answer;
    }
}
