package com.hz_apps.timebasedlocker.Datebase;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.hz_apps.timebasedlocker.ui.LockFiles.DateAndTime;

@Entity(tableName = "saved_photos_list")
public class SavedPhoto {
    @PrimaryKey(autoGenerate = true)
    public int id;

    public String photoPath;
    @NonNull public String photoTitle;
    public boolean isAllowedToEditDataAndTime;
    public boolean isAllowedToSeeTitle;
    public boolean isAllowedToSeeBlurPhoto;
    @NonNull public DateAndTime unlockDateAndTime;
    @NonNull public DateAndTime lockDateAndTime;
    /*
     * Folder is int because it store the position.
     * If folder=1 mean files belong to folder that have id=1;
     * folder=0 mean home directory
     */
    public int folder=0;

    public SavedPhoto(@NonNull String photoPath, @NonNull String photoTitle, boolean isAllowedToEditDataAndTime, boolean isAllowedToSeeTitle, boolean isAllowedToSeeBlurPhoto, @NonNull DateAndTime unlockDateAndTime, @NonNull DateAndTime lockDateAndTime, int folder) {
        this.photoPath = photoPath;
        this.photoTitle = photoTitle;
        this.isAllowedToEditDataAndTime = isAllowedToEditDataAndTime;
        this.isAllowedToSeeTitle = isAllowedToSeeTitle;
        this.isAllowedToSeeBlurPhoto = isAllowedToSeeBlurPhoto;
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

    public String getPhotoPath() {
        return photoPath;
    }

    public void setPhotoPath(String photoPath) {
        this.photoPath = photoPath;
    }

    public String getPhotoTitle() {
        return photoTitle;
    }

    public void setPhotoTitle(String photoTitle) {
        this.photoTitle = photoTitle;
    }

    public boolean isAllowedToEditDataAndTime() {
        return isAllowedToEditDataAndTime;
    }

    public void setAllowedToEditDataAndTime(boolean allowedToEditDataAndTime) {
        isAllowedToEditDataAndTime = allowedToEditDataAndTime;
    }

    public boolean isAllowedToSeeTitle() {
        return isAllowedToSeeTitle;
    }

    public void setAllowedToSeeTitle(boolean allowedToSeeTitle) {
        isAllowedToSeeTitle = allowedToSeeTitle;
    }

    public boolean isAllowedToSeeBlurPhoto() {
        return isAllowedToSeeBlurPhoto;
    }

    public void setAllowedToSeeBlurPhoto(boolean allowedToSeeBlurPhoto) {
        isAllowedToSeeBlurPhoto = allowedToSeeBlurPhoto;
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

    public int getFolder() {
        return folder;
    }

    public void setFolder(int folder) {
        this.folder = folder;
    }
}
