package com.hz_apps.timebasedlocker.Datebase;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "DBRecord")
public class DBRecord {

    public static final int LAST_VIDEO_ID = 1;

    @PrimaryKey
    int id;

    int value;

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
