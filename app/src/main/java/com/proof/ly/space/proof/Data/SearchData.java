package com.proof.ly.space.proof.Data;

/**
 * Created by aman on 4/17/18.
 */

public class SearchData {
    private String question,answer;

    public SearchData(){}
    public SearchData(String question, String answer) {
        this.question = question;
        this.answer = answer;
    }

    public String getAnswer() {
        return answer;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }
}
