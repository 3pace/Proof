package com.proof.ly.space.proof.Helpers.Retrofit;

import com.google.gson.annotations.SerializedName;

public class QuestionResponse {
    @SerializedName("id")
    private int mId;
    @SerializedName("question")
    private String mQuestion;

    public int getId() {
        return mId;
    }

    public void setId(int id) {
        mId = id;
    }

    public String getQuestion() {
        return mQuestion;
    }

    public void setQuestion(String question) {
        mQuestion = question;
    }
}
