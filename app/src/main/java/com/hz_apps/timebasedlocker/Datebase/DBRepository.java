package com.hz_apps.timebasedlocker.Datebase;

import android.app.Application;

public class DBRepository {
    private final DBDao dbDao;

    private DBRepository(Application application){
        RoomDB database = RoomDB.getInstance(application);
        dbDao = database.dbDao();
    }
}
