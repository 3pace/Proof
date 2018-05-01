package com.proof.ly.space.proof.Data;

/**
 * Created by aman on 4/21/18.
 */

public class JsonQuestion {
    private int id;
    private String jsonQuestion;

    public JsonQuestion(int id, String jsonQuestion) {
        this.id = id;
        this.jsonQuestion = jsonQuestion;
    }

    public int getId() {
        return id;
    }

    public String getJsonQuestion() {
        return jsonQuestion;
    }
}
