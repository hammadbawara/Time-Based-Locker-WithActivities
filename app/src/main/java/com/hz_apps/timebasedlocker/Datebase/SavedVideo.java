package com.hz_apps.timebasedlocker.Datebase;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.hz_apps.timebasedlocker.ui.LockFiles.DateAndTime;

@Entity(tableName = "saved_videos_list")
@TypeConverters(Converters.class)
public class SavedVideo {

    @PrimaryKey(autoGenerate = true)
    @NonNull private int id;
    @NonNull private String videoPath;
    @NonNull private String videoName;
    @NonNull private boolean isAllowedToEditDateAndTime;
    @NonNull private boolean isAllowedToSeeMetaData;
    @NonNull private boolean isAllowedToSeeVideoThumbnail;
    @NonNull private DateAndTime unlockDateAndTime;
    @NonNull private DateAndTime lockDateAndTime;
    @NonNull private int folder_address;

    public SavedVideo(@NonNull String videoPath, @NonNull String videoName, boolean isAllowedToEditDateAndTime, boolean isAllowedToSeeMetaData, boolean isAllowedToSeeVideoThumbnail, @NonNull DateAndTime unlockDateAndTime, @NonNull DateAndTime lockDateAndTime, int folder_address) {
        this.videoPath = videoPath;
        this.videoName = videoName;
        this.isAllowedToEditDateAndTime = isAllowedToEditDateAndTime;
        this.isAllowedToSeeMetaData = isAllowedToSeeMetaData;
        this.isAllowedToSeeVideoThumbnail = isAllowedToSeeVideoThumbnail;
        this.unlockDateAndTime = unlockDateAndTime;
        this.lockDateAndTime = lockDateAndTime;
        this.folder_address = folder_address;
    }

    public int getId() {
        return id;
    }

    public void setId(int id){
        this.id = id;
    }

    public String getVideoPath() {
        return videoPath;
    }

    public void setVideoPath(String videoPath) {
        this.videoPath = videoPath;
    }

    public String getVideoName() {
        return videoName;
    }

    public void setVideoName(String videoName) {
        this.videoName = videoName;
    }


    public boolean isAllowedToEditDateAndTime() {
        return isAllowedToEditDateAndTime;
    }

    public void setAllowedToEditDateAndTime(boolean allowedToEditDateAndTime) {
        isAllowedToEditDateAndTime = allowedToEditDateAndTime;
    }

    public boolean isAllowedToSeeMetaData() {
        return isAllowedToSeeMetaData;
    }

    public void setAllowedToSeeMetaData(boolean allowedToSeeMetaData) {
        isAllowedToSeeMetaData = allowedToSeeMetaData;
    }

    public boolean isAllowedToSeeVideoThumbnail() {
        return isAllowedToSeeVideoThumbnail;
    }

    public void setAllowedToSeeVideoThumbnail(boolean allowedToSeeVideoThumbnail) {
        isAllowedToSeeVideoThumbnail = allowedToSeeVideoThumbnail;
    }

    public DateAndTime getUnlockDateAndTime() {
        return unlockDateAndTime;
    }

    public void setUnlockDateAndTime(DateAndTime unlockDateAndTime) {
        this.unlockDateAndTime = unlockDateAndTime;
    }

    public DateAndTime getLockDateAndTime() {
        return lockDateAndTime;
    }

    public void setLockDateAndTime(DateAndTime lockDateAndTime) {
        this.lockDateAndTime = lockDateAndTime;
    }

    public int getFolder_address() {
        return folder_address;
    }

    public void setFolder_address(int folder_address) {
        this.folder_address = folder_address;
    }
}
