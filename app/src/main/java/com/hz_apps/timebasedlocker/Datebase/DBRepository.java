package com.hz_apps.timebasedlocker.Datebase;

import android.app.Application;

import androidx.lifecycle.LiveData;

import java.util.List;

public class DBRepository {
    private final DBDao dbDao;

    public DBRepository(Application application){
        RoomDB database = RoomDB.getInstance(application);
        dbDao = database.dbDao();
    }

    public void insertVideos(SavedVideo[] videosList){
        for (SavedVideo video : videosList){
            dbDao.insertVideo(video);
        }
    }

    public LiveData<List<SavedPhoto>> getAllSavedPhotos(int folder){
        return dbDao.getAllSavedPhotos(folder);
    }

    public void insertVideo(SavedVideo video){
        dbDao.insertVideo(video);
    }

    public void insertPhoto(SavedPhoto photo){
        dbDao.insertPhoto(photo);
    }

    public void updateDBRecord(DBRecord record){
        dbDao.updateDBRecord(record);
    }

    public DBRecord getDBRecord(int key){
        return dbDao.getDBRecord(key);
    }

    public LiveData<List<SavedVideo>> getAllSavedVideos(int folder){

        return dbDao.getAllVideos(folder);
    }
}
