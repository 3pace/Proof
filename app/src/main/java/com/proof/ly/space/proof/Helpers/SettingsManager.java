package com.proof.ly.space.proof.Helpers;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by aman on 4/22/18.
 */

public class SettingsManager {

    private SharedPreferences preferences;
    private SharedPreferences.Editor editor;
    private final String PREF_NAME = "settings";
    private final String COLORED = "colored";
    private final String CYCLE_MODE = "cycle_mode";
    private final String AUTOFLIP = "autoflip";
    private final String NIGHTMODE = "nightmode";
    private final String QUESCOUNT = "quescount";
    private final String DB_VERSION = "db_version";
    public static int db_version;
    public static boolean colored,cycleMode,autoflip,nighmode,quesCount;

    public SettingsManager(Context context) {
        preferences = context.getSharedPreferences(PREF_NAME,0);
    }

    public void saveColoredState(boolean state){
        editor = preferences.edit();
        editor.putBoolean(COLORED,state);
        editor.apply();

    }
    public boolean getColoredState(){
        return preferences.getBoolean(COLORED,false);
    }
    public void saveModeState(boolean mode){
        editor = preferences.edit();
        editor.putBoolean(CYCLE_MODE, mode);
        editor.apply();
    }
    public boolean getCycleModeState(){
        return preferences.getBoolean(CYCLE_MODE,false);
    }

    public void saveAutoflipState(boolean state){
        editor = preferences.edit();
        editor.putBoolean(AUTOFLIP, state);
        editor.apply();
    }
    public boolean getAutoflipState() { return preferences.getBoolean(AUTOFLIP,true);   }
    public void saveNightmodeState(boolean nighmode){
        editor = preferences.edit();
        editor.putBoolean(NIGHTMODE, nighmode);
        editor.apply();
    }
    public boolean getNightmodeState() { return preferences.getBoolean(NIGHTMODE,false);   }

    public void saveQuesCount(boolean quesCount){
        editor = preferences.edit();
        editor.putBoolean(QUESCOUNT,quesCount);
        editor.apply();
    }
    public boolean getQuesCount(){
        return preferences.getBoolean(QUESCOUNT,true);
    }
    public void saveDBVersion(int version){
        editor = preferences.edit();
        editor.putInt(DB_VERSION, version);
        editor.apply();
    }
    public int getDBVersion(){
        return preferences.getInt(DB_VERSION,0);
    }
}
