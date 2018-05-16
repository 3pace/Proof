package com.proof.ly.space.proof.Data;

/**
 * Created by aman on 3/12/18.
 */

public class Answers {
    private String mAnswer;
    private boolean mIsCorrect;
    private int mIsChecked = 0;
    private boolean mIsCorrectChecked = false;
    private boolean mIsEnabled = true;
    private boolean mIsClicked = false;


    public boolean isEnabled() {
        return mIsEnabled;
    }

    public boolean isClicked() {
        return mIsClicked;
    }

    public void setClicked(boolean clicked) {
        this.mIsClicked = clicked;
    }

    public void setEnabled(boolean enabled) {
        this.mIsEnabled = enabled;
    }

    public Answers(String answer,boolean isCorrect) {
        this.mAnswer = answer;
        this.mIsCorrect = isCorrect;



    }



    public boolean isCorrectChecked() {
        return mIsCorrectChecked;
    }

    public void setCorrectChecked(boolean correctChecked) {
        this.mIsCorrectChecked = correctChecked;
    }

    public int getIsChecked() {
        return mIsChecked;
    }

    public void setIsChecked(int isChecked) {
        this.mIsChecked = isChecked;
    }

    public boolean isCorrect() {
        return mIsCorrect;
    }

    public String getAnswer() {
        return mAnswer;
    }

}
