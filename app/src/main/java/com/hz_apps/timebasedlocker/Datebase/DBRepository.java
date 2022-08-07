package com.hz_apps.timebasedlocker.Datebase;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class DBRepository {
    private final DBDao dbDao;

    private DBRepository(Application application){
        RoomDB database = RoomDB.getInstance(application);
        dbDao = database.dbDao();
    }

    public void insertVideos(SavedVideo[] videosList){
        for (SavedVideo video : videosList){
            dbDao.insertVideo(video);
        }
    }

    public LiveData<List<SavedVideo>> getAllSavedVideos(){
        return getAllSavedVideos();
    }
}
