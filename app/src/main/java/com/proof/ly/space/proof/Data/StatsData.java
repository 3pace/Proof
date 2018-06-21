package com.proof.ly.space.proof.Data;

public class StatsData {
    private int mPoint, mCorrect, mNotCorrect, mPercent, mCount;
    private String mDate;

    public StatsData(int point, int correct, int notCorrect, int percent, int count, String date) {
        mPoint = point;
        mCorrect = correct;
        mNotCorrect = notCorrect;
        mPercent = percent;
        mCount = count;
        mDate = date;
    }

    public StatsData() {
    }

    public int getPoint() {
        return mPoint;
    }

    public void setPoint(int point) {
        mPoint = point;
    }

    public StatsData setCorrect(int correct) {
        mCorrect = correct;
        return this;
    }

    public void setNotCorrect(int notCorrect) {
        mNotCorrect = notCorrect;
    }

    public void setPercent(int percent) {
        mPercent = percent;
    }

    public void setCount(int count) {
        mCount = count;
    }

    public void setDate(String date) {
        mDate = date;
    }

    public int getCorrect() {
        return mCorrect;

    }

    public int getNotCorrect() {
        return mNotCorrect;
    }

    public int getPercent() {
        return mPercent;
    }

    public int getCount() {
        return mCount;
    }

    public String getDate() {
        return mDate;
    }
}
