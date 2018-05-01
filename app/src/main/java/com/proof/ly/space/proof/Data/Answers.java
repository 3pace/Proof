package com.proof.ly.space.proof.Data;

/**
 * Created by aman on 3/12/18.
 */

public class Answers {
    private String answer;
    private boolean correct = false;
    private int checked = 0;
    private boolean correctChecked = false;
    private boolean enabled = true;
    private boolean clicked = false;


    public boolean isEnabled() {
        return enabled;
    }

    public boolean isClicked() {
        return clicked;
    }

    public void setClicked(boolean clicked) {
        this.clicked = clicked;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public Answers(String answer,boolean correct) {
        this.answer = answer;
        this.correct = correct;



    }



    public boolean isCorrectChecked() {
        return correctChecked;
    }

    public void setCorrectChecked(boolean correctChecked) {
        this.correctChecked = correctChecked;
    }

    public int getChecked() {
        return checked;
    }

    public void setChecked(int checked) {
        this.checked = checked;
    }

    public boolean isCorrect() {
        return correct;
    }

    public String getAnswer() {
        return answer;
    }

}
