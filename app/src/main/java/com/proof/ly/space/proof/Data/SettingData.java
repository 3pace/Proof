package com.proof.ly.space.proof.Data;

/**
 * Created by aman on 4/24/18.
 */

public class SettingData {
    private String mTitle;
    private boolean state;


    public SettingData(String title, boolean state) {
        this.mTitle = title;
        this.state = state;
    }


    public boolean getState() {
        return state;
    }

    public void setTitle(String title) {
        this.mTitle = title;
    }

    public void setState(boolean state) {
        this.state = state;
    }

    public String getTitle() {
        return mTitle;
    }
}
