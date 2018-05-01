package com.proof.ly.space.proof.Data;

/**
 * Created by aman on 4/24/18.
 */

public class Lesson {
    private String name,db;

    public Lesson(String name, String db) {
        this.name = name;
        this.db = db;
    }

    public String getName() {
        return name;
    }

    public String getDb() {
        return db;
    }
}
