package com.proof.ly.space.proof.Data;

/**
 * Created by aman on 4/21/18.
 */

public class JsonQuestion {
    private int mId;
    private String mJsonQuestion;

    public JsonQuestion(int id, String jsonQuestion) {
        this.mId = id;
        this.mJsonQuestion = jsonQuestion;
    }

    public int getId() {
        return mId;
    }

    public String getJsonQuestion() {
        return mJsonQuestion;
    }
}
