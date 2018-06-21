package com.proof.ly.space.proof.Helpers;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by aman on 4/22/18.
 */

public class SettingsManager {

    private SharedPreferences mPreferences;
    private SharedPreferences.Editor mEditor;
    private final String PREF_NAME = "settings";
    private final String COLORED = "colored";
    private final String CYCLE_MODE = "cycle_mode";
    private final String AUTOFLIP = "autoflip";
    private final String NIGHTMODE = "nightmode";
    private final String QUESCOUNT = "quescount";
    private final String LANGUAGE = "language";
    private final String DB_VERSION = "localDbVersion";
    public static int localDbVersion;
    public static boolean colored, cycleMode, autoflip, nighmode, quesCount, currentLanguage;


    public SettingsManager(Context context) {
        mPreferences = context.getSharedPreferences(PREF_NAME, 0);
    }

    public void saveColoredState(boolean state) {
        mEditor = mPreferences.edit();
        mEditor.putBoolean(COLORED, state);
        mEditor.apply();

    }

    public boolean getColoredState() {
        return mPreferences.getBoolean(COLORED, false);
    }

    public void saveModeState(boolean mode) {
        mEditor = mPreferences.edit();
        mEditor.putBoolean(CYCLE_MODE, mode);
        mEditor.apply();
    }

    public boolean getCycleModeState() {
        return mPreferences.getBoolean(CYCLE_MODE, false);
    }

    public void saveAutoflipState(boolean state) {
        mEditor = mPreferences.edit();
        mEditor.putBoolean(AUTOFLIP, state);
        mEditor.apply();
    }

    public boolean getAutoflipState() {
        return mPreferences.getBoolean(AUTOFLIP, true);
    }

    public void saveNightmodeState(boolean nighmode) {
        mEditor = mPreferences.edit();
        mEditor.putBoolean(NIGHTMODE, nighmode);
        mEditor.apply();
    }

    public boolean getNightmodeState() {
        return mPreferences.getBoolean(NIGHTMODE, false);
    }

    public void saveQuesCount(boolean quesCount) {
        mEditor = mPreferences.edit();
        mEditor.putBoolean(QUESCOUNT, quesCount);
        mEditor.apply();
    }

    public boolean getQuesCount() {
        return mPreferences.getBoolean(QUESCOUNT, true);
    }

    public void saveDBVersion(int version) {
        mEditor = mPreferences.edit();
        mEditor.putInt(DB_VERSION, version);
        mEditor.apply();
    }

    public int getDBVersion() {
        return mPreferences.getInt(DB_VERSION, 0);
    }

    public void setLanguageIsRussian(boolean langIsRussian) {
        mEditor = mPreferences.edit();
        mEditor.putBoolean(LANGUAGE, langIsRussian);
        mEditor.apply();
    }

    public boolean langIsRussian(){
        return mPreferences.getBoolean(LANGUAGE, true);
    }
}
