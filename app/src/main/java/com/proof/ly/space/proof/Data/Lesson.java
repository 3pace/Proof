package com.proof.ly.space.proof.Data;

/**
 * Created by aman on 4/24/18.
 */

public class Lesson {
    private String mName, mDb;

    public Lesson(String name, String db) {
        this.mName = name;
        this.mDb = db;
    }

    public String getName() {
        return mName;
    }

    public String getDb() {
        return mDb;
    }
}
