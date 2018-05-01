package com.proof.ly.space.proof.Helpers;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import static android.content.ContentValues.TAG;

/**
 * Created by aman on 27.03.18.
 */

public class DBHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "datatest";
    private static final int DB_VERSION = 1;

    public static final String QUESTION_TABLE = "questions";
    public static final String QUESTION_ROW = "question";
    public static final String QUESTION_FROM = "user";
    public static final String USER_TABLE = "users";
    public static final String USERNAME_ROW = "username";
    public static final String USER_PASSWORD = "password";
    public static final String MENU_LESSONS = "menu_lessons";
    public static final String MENU_LESSON_NAME = "lesson_name";
    public static final String MENU_LESSON_DB = "lesson_db";


    DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_LESSONS_TABLE = "CREATE TABLE "+MENU_LESSONS +" ("+
                "_id integer primary key, "+
                MENU_LESSON_NAME +" text,"+
                MENU_LESSON_DB + " text);";
        String CREATE_QUESTIONS_TABLE = "CREATE TABLE "+QUESTION_TABLE +" ("+
                "_id integer primary key, "+
                QUESTION_ROW +" text,"+
                QUESTION_FROM + " integer);";
        String CREATE_JAVA_TABLE = "CREATE TABLE "+LessonManager.DEF_TABLE +" ("+
                "_id integer primary key, "+
                QUESTION_ROW +" text,"+
                QUESTION_FROM + " integer);";

        String CREATE_USERS_TABLE = "CREATE TABLE "+USER_TABLE + " ("+
                "_id integer primary key, "+
                USERNAME_ROW + " text, "+
                USER_PASSWORD + " text);";
        sqLiteDatabase.execSQL(CREATE_LESSONS_TABLE);
        sqLiteDatabase.execSQL(CREATE_QUESTIONS_TABLE);
        sqLiteDatabase.execSQL(CREATE_JAVA_TABLE);

        sqLiteDatabase.execSQL(CREATE_USERS_TABLE);
        Log.d(TAG, "onCreate: created");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
