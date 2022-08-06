package com.hz_apps.timebasedlocker.Datebase;

import androidx.room.Entity;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverters;

import com.hz_apps.timebasedlocker.ui.LockFiles.DateAndTime;

@Entity(tableName = "saved_videos_list")
@TypeConverters(Converters.class)
public class SavedVideo {

    @PrimaryKey(autoGenerate = true)
    private int id;
    private String videoPath;
    private String videoName;
    private String videoSize;
    private String videoDuration;
    private boolean isAllowedToEditDateAndTime;
    private boolean isAllowedToSeeMetaData;
    private boolean isAllowedToSeeVideoThumbnail;
    private DateAndTime unlockDateAndTime;
    private DateAndTime lockDateAndTime;

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

    public String getVideoSize() {
        return videoSize;
    }

    public void setVideoSize(String videoSize) {
        this.videoSize = videoSize;
    }

    public String getVideoDuration() {
        return videoDuration;
    }

    public void setVideoDuration(String videoDuration) {
        this.videoDuration = videoDuration;
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
}
