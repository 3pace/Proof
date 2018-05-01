package com.proof.ly.space.proof.Data;

/**
 * Created by aman on 4/24/18.
 */

public class SettingData {
    private String title;
    private boolean state;


    public SettingData() {
    }

    public SettingData(String title, boolean state) {
        this.title = title;
        this.state = state;
    }


    public boolean getState() {
        return state;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public String getTitle() {
        return title;
    }
}
