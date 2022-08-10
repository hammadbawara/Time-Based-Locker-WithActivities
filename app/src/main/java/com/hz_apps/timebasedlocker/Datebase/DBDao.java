package com.hz_apps.timebasedlocker.Datebase;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface DBDao {

    @Insert
    void insertVideo(SavedVideo video);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateVideo(SavedVideo video);

    @Query("SELECT * FROM saved_videos_list WHERE folder = :folder")
    LiveData<List<SavedVideo>> getAllVideos(int folder);

    @Query("DELETE FROM saved_videos_list WHERE id = :id")
    void deleteVideo(int id);

    @Query("SELECT * FROM dbrecord WHERE `id` = :key")
    DBRecord getDBRecord(int key);

    @Insert
    void insertPhoto(SavedPhoto photo);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void updateDBRecord(DBRecord record);

    @Insert
    void insertDBRecord(DBRecord dbRecord);

    @Query("SELECT * FROM saved_photos_list WHERE folder = :folder")
    LiveData<List<SavedPhoto>> getAllSavedPhotos(int folder);
}
