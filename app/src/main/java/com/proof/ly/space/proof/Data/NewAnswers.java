package com.proof.ly.space.proof.Data;

/**
 * Created by aman on 25.03.18.
 */

public class NewAnswers {
    private String answer;
    private boolean correct = false;

    public NewAnswers(String answer, boolean correct) {
        this.answer = answer;
        this.correct = correct;
    }

    public String getAnswer() {
        return answer;
    }

    public boolean isCorrect() {
        return correct;
    }

    public void setCorrect(boolean correct) {
        this.correct = correct;
    }
}
