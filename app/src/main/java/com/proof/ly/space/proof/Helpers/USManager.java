package com.proof.ly.space.proof.Helpers;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by aman on 29.03.18.
 */

public class USManager {
    private static final String PREF_NAME = "user_session";
    private static final String ID = "user_id";
    private static final String IN = "user_in";
    private static SharedPreferences preferences;
    private SharedPreferences.Editor mEditor;

    public USManager(Context c) {
        preferences = c.getSharedPreferences(PREF_NAME, 0);
    }

    public static USManager getInstance(Context context) {
        return new USManager(context);
    }

    public void logIn(int id) {
        mEditor = preferences.edit();
        mEditor.putInt(ID, id);
        mEditor.putBoolean(IN, true);
        mEditor.apply();
    }

    public void logOut() {
        mEditor = preferences.edit();
        mEditor.clear();
        mEditor.apply();
    }

    public static boolean hasLogIn() {
        return preferences != null && preferences.getBoolean(IN, false);
    }

    public static boolean hasLogIn(Context context) {
        if (preferences == null) {
            return context.getSharedPreferences(PREF_NAME, 0).getBoolean(IN, false);
        } else
            return preferences.getBoolean(IN, false);
    }

    public static int getUID() {
        return preferences.getInt(ID, 0);
    }
}
