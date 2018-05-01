package com.proof.ly.space.proof.Helpers;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.proof.ly.space.proof.Data.Lesson;

import java.util.ArrayList;

/**
 * Created by aman on 4/24/18.
 */

public class LessonManager {
    private ArrayList<Lesson> lessons = new ArrayList<>();
    private int lessonCursor = 0;
    public static final String DEF_NOT = "Недоступно";
    public static final String DEF_TABLE = "def";
    public static String CURRENT_DB;
    public static String CURRENT_LESSON;
    public static boolean lessonChanged = false;
    public static boolean isLessonChangedForTesting = false;

    public LessonManager(DBManager dbManager) {
        update(dbManager);


    }
    public void update(DBManager dbManager){
        SQLiteDatabase database = dbManager.getHelper().getReadableDatabase();
        Cursor c = database.query(DBHelper.MENU_LESSONS,null,null,null,null,null,null);
        if (c.moveToFirst())
            do {
                lessons.add(new Lesson(c.getString(1),c.getString(2)));
            } while (c.moveToNext());

        database.close();
        c.close();
        CURRENT_LESSON = lessons.size() > 0 ? lessons.get(0).getName() : DEF_NOT;
        CURRENT_DB = lessons.size() > 0 ? lessons.get(0).getDb() : DEF_TABLE;
    }
    public String changeLesson(){
        if (lessons.size() > 0) {
            String lesson;

            lessonCursor++;
            if (lessonCursor >= lessons.size())
                lessonCursor = 0;

            lesson = lessons.get(lessonCursor).getName();
            CURRENT_LESSON = lessons.get(lessonCursor).getName();
            CURRENT_DB = lessons.get(lessonCursor).getDb();
            lessonChanged = isLessonChangedForTesting = true;
            return lesson;
        }
        return DEF_NOT;

    }
    public String setCurrentLesson(String currentLesson,String currendDb){
        CURRENT_LESSON = currentLesson;
        CURRENT_DB = currendDb;
        String lesson;
        lesson = CURRENT_LESSON;
        return lesson;
    }
}
