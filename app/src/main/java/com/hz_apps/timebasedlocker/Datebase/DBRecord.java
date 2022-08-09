package com.hz_apps.timebasedlocker.Datebase;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "DBRecord")
public class DBRecord {

    public static final int LAST_SAVED_VIDEO_KEY = 1;
    public static final int LAST_SAVED_PHOTO_KEY = 2;

    @PrimaryKey
    int id;

    int value;

    public DBRecord(int id, int value) {
        this.id = id;
        this.value = value;
    }

    public DBRecord(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}
