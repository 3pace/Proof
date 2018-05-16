package com.proof.ly.space.proof.Data;

/**
 * Created by aman on 25.03.18.
 */

public class NewAnswers {
    private String mAnswer;
    private boolean mIsCorrect;

    public NewAnswers(String answer, boolean mIsCorrect) {
        this.mAnswer = answer;
        this.mIsCorrect = mIsCorrect;
    }

    public String getAnswer() {
        return mAnswer;
    }

    public boolean isCorrect() {
        return mIsCorrect;
    }

    public void setCorrect(boolean correct) {
        this.mIsCorrect = correct;
    }
}
