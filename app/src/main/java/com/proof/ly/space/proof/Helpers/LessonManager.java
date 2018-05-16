package com.proof.ly.space.proof.Helpers;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.proof.ly.space.proof.Data.Lesson;

import java.util.ArrayList;

/**
 * Created by aman on 4/24/18.
 */

public class LessonManager {
    private ArrayList<Lesson> mArrayListLessons = new ArrayList<>();
    private int mLessonCursor = 0;
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
        mArrayListLessons.clear();
        SQLiteDatabase database = dbManager.getHelper().getReadableDatabase();
        Cursor c = database.query(DBHelper.MENU_LESSONS,null,null,null,null,null,null);
        if (c.moveToFirst())
            do {
                mArrayListLessons.add(new Lesson(c.getString(1),c.getString(2)));
            } while (c.moveToNext());

        database.close();
        c.close();
        CURRENT_LESSON = mArrayListLessons.size() > 0 ? mArrayListLessons.get(0).getName() : DEF_NOT;
        CURRENT_DB = mArrayListLessons.size() > 0 ? mArrayListLessons.get(0).getDb() : DEF_TABLE;
    }
    public String changeLesson(){
        if (mArrayListLessons.size() > 0) {
            String lesson;

            mLessonCursor++;
            if (mLessonCursor >= mArrayListLessons.size())
                mLessonCursor = 0;

            lesson = mArrayListLessons.get(mLessonCursor).getName();
            CURRENT_LESSON = mArrayListLessons.get(mLessonCursor).getName();
            CURRENT_DB = mArrayListLessons.get(mLessonCursor).getDb();
            lessonChanged = isLessonChangedForTesting = true;
            return lesson;
        }
        return DEF_NOT;

    }
    public int getCount(){
        return mArrayListLessons.size();
    }
    public String setCurrentLesson(String currentLesson,String currendDb){
        CURRENT_LESSON = currentLesson;
        CURRENT_DB = currendDb;
        String lesson = CURRENT_LESSON;
        return lesson.trim();
    }
}
