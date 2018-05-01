package com.proof.ly.space.proof.Helpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


//import com.readystatesoftware.sqliteasset.SQLiteAssetHelper;

/**
 * Created by aman on 4/17/18.
 */

public class DBSearchHelper extends SQLiteOpenHelper/*SQLiteAssetHelper*/ {

    public DBSearchHelper(Context context) {
        super(context, "none", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
/*
    private static final String DATABASE_NAME = "testing.db";
    private static final int DATABASE_VERSION = 1;

    public DBSearchHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }*/
}
