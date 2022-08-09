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
    private int id;
    private String videoPath;
    private String videoTitle;
    private boolean isAllowedToEditDateAndTime;
    private boolean isAllowedToSeeTitle;
    private boolean isAllowedToSeeVideoThumbnail;
    @NonNull private DateAndTime unlockDateAndTime;
    @NonNull private DateAndTime lockDateAndTime;
    /*
     * Folder is int because it store the position.
     * If folder=1 mean files belong to folder that have id=1;
     * folder=0 mean home directory
     */
    private int folder=0;

    public SavedVideo(String videoPath, String videoTitle, boolean isAllowedToEditDateAndTime, boolean isAllowedToSeeTitle, boolean isAllowedToSeeVideoThumbnail, @NonNull DateAndTime unlockDateAndTime, @NonNull DateAndTime lockDateAndTime, int folder) {
        this.videoPath = videoPath;
        this.videoTitle = videoTitle;
        this.isAllowedToEditDateAndTime = isAllowedToEditDateAndTime;
        this.isAllowedToSeeTitle = isAllowedToSeeTitle;
        this.isAllowedToSeeVideoThumbnail = isAllowedToSeeVideoThumbnail;
        this.unlockDateAndTime = unlockDateAndTime;
        this.lockDateAndTime = lockDateAndTime;
        this.folder = folder;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getVideoPath() {
        return videoPath;
    }

    public void setVideoPath(String videoPath) {
        this.videoPath = videoPath;
    }

    public String getVideoTitle() {
        return videoTitle;
    }

    public void setVideoTitle(String videoTitle) {
        this.videoTitle = videoTitle;
    }

    public boolean isAllowedToEditDateAndTime() {
        return isAllowedToEditDateAndTime;
    }

    public void setAllowedToEditDateAndTime(boolean allowedToEditDateAndTime) {
        isAllowedToEditDateAndTime = allowedToEditDateAndTime;
    }

    public boolean isAllowedToSeeTitle() {
        return isAllowedToSeeTitle;
    }

    public void setAllowedToSeeTitle(boolean allowedToSeeTitle) {
        isAllowedToSeeTitle = allowedToSeeTitle;
    }

    public boolean isAllowedToSeeVideoThumbnail() {
        return isAllowedToSeeVideoThumbnail;
    }

    public void setAllowedToSeeVideoThumbnail(boolean allowedToSeeVideoThumbnail) {
        isAllowedToSeeVideoThumbnail = allowedToSeeVideoThumbnail;
    }

    @NonNull
    public DateAndTime getUnlockDateAndTime() {
        return unlockDateAndTime;
    }

    public void setUnlockDateAndTime(@NonNull DateAndTime unlockDateAndTime) {
        this.unlockDateAndTime = unlockDateAndTime;
    }

    @NonNull
    public DateAndTime getLockDateAndTime() {
        return lockDateAndTime;
    }

    public void setLockDateAndTime(@NonNull DateAndTime lockDateAndTime) {
        this.lockDateAndTime = lockDateAndTime;
    }

    public int getFolder() {
        return folder;
    }

    public void setFolder(int folder) {
        this.folder = folder;
    }
}
