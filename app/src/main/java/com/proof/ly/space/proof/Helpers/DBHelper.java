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
    public static final String DB_NAME = "datatest";
    private static final int DB_VERSION = 1;

    private static final String ID_ROW = "_id integer primary key, ";

    public static final String QUESTION_TABLE = "questions";
    public static final String QUESTION_ROW = "question";
    public static final String QUESTION_FROM = "user";
    public static final String USER_TABLE = "users";
    public static final String USERNAME_ROW = "username";
    public static final String USER_PASSWORD = "password";
    public static final String MENU_LESSONS = "menu_lessons";
    public static final String MENU_LESSON_NAME = "lesson_name";
    public static final String MENU_LESSON_DB = "lesson_db";

    public static final String STATS_TABLE = "stats";
    public static final String STATS_CORRECT = "s_correct";
    public static final String STATS_PERCENT = "s_percent";
    public static final String STATS_POINT = "s_point";
    public static final String STATS_QUESTIONS_COUNT = "s_count";
    public static final String STATS_NOT_CORRECT = "s_not_correct";
    public static final String STATS_LESSON = "s_lesson";
    public static final String STATS_DATE = "s_date";


    DBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String CREATE_LESSONS_TABLE = "CREATE TABLE " + MENU_LESSONS + " (" +
                ID_ROW +
                MENU_LESSON_NAME + " text, " +
                MENU_LESSON_DB + " text);";
        String CREATE_QUESTIONS_TABLE = "CREATE TABLE " + QUESTION_TABLE + " (" +
                ID_ROW +
                QUESTION_ROW + " text, " +
                QUESTION_FROM + " integer);";
        String CREATE_JAVA_TABLE = "CREATE TABLE " + LessonManager.DEF_TABLE + " (" +
                ID_ROW +
                QUESTION_ROW + " text, " +
                QUESTION_FROM + " integer);";

        String CREATE_USERS_TABLE = "CREATE TABLE " + USER_TABLE + " (" +
                ID_ROW +
                USERNAME_ROW + " text, " +
                USER_PASSWORD + " text);";
        String CREATE_STATS_TABLE = "CREATE TABLE " + STATS_TABLE + " (" +
                ID_ROW +
                STATS_LESSON + " text, " +
                STATS_POINT + " integer, " +
                STATS_CORRECT + " integer, " +
                STATS_NOT_CORRECT + " integer, " +
                STATS_PERCENT + " integer, " +
                STATS_QUESTIONS_COUNT + " integer, " +
                STATS_DATE + " text);";

        sqLiteDatabase.execSQL(CREATE_LESSONS_TABLE);
        sqLiteDatabase.execSQL(CREATE_QUESTIONS_TABLE);
        sqLiteDatabase.execSQL(CREATE_JAVA_TABLE);
        sqLiteDatabase.execSQL(CREATE_USERS_TABLE);
        sqLiteDatabase.execSQL(CREATE_STATS_TABLE);
        Log.d(TAG, "onCreate: created");

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

}
