package com.proof.ly.space.proof.Data;

import android.util.Log;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

/**
 * Created by aman on 16.03.18.
 */

public class Question {
    private int qId;
    private String q;
    private ArrayList<Answers> answers;
    private int cCount = 0;
    private boolean checked = false;
    private boolean correctChecked = false;
    private int cAnswers = 0;
    private int notcAnswers = 0;
    private int correctCount = 0;
    private int leftCorrectCount = 0;
    private boolean created = false;
    private int notCorrect = 0;

    public Question(int qId,String q,ArrayList<Answers> answers,int cCount){
        this.qId = qId;
        this.q = q;
        this.answers = answers;
        this.cCount = cCount;


    }




    public int getqId() {
        return qId;
    }

    public String getQ() {
        return q;
    }

    public int getcCount() {
        return cCount;
    }
    public void setChecked(boolean checked){
        this.checked = checked;
    }

    public void setcAnswers(int cAnswers) {
        this.cAnswers = cAnswers;
    }

    public void setNotcAnswers(int notcAnswers) {
        this.notcAnswers = notcAnswers;
    }

    public int getNotcAnswers() {
        return notcAnswers;
    }

    public int getcAnswers() {
        return cAnswers;
    }

    public boolean isChecked() {
        return checked;
    }

    public boolean isCorrectChecked() {
        return correctChecked;
    }

    public void setCorrectChecked(boolean correctChecked) {
        this.correctChecked = correctChecked;
    }

    public ArrayList<Answers> getAnswers() {
        return answers;
    }

    public void setLeftCorrectCount(int leftCorrectCount) {
        this.leftCorrectCount = leftCorrectCount;
    }

    public int getLeftCorrectCount() {
        return leftCorrectCount;
    }

    public void setCreated(boolean created) {
        this.created = created;
    }

    public boolean isCreated() {
        return created;
    }

    public int getCorrectCount() {
        return correctCount;
    }

    public int getNotCorrect() {
        return notCorrect;
    }

    public void setNotCorrect(int notCorrect) {
        this.notCorrect = notCorrect;
    }
}
