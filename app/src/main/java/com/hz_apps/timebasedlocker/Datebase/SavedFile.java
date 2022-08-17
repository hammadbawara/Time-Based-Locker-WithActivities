package com.hz_apps.timebasedlocker.Datebase;

import androidx.annotation.NonNull;

import com.hz_apps.timebasedlocker.ui.LockFiles.DateAndTime;

import java.io.Serializable;

public class SavedFile implements Serializable {
    private int id;
    private String path;
    private String originalPath;
    private String title;
    private DateAndTime unlockDateTime;
    private DateAndTime lockDateTime;
    private int fileType;
    private boolean isFile;
    private boolean isAllowedToExtendDateTime;
    private boolean isAllowedToSeePhoto;
    private boolean isAllowedToSeeTitle;

    public SavedFile(String originalPath, String title, DateAndTime unlockDateTime, DateAndTime lockDateTime, int fileType, boolean isFile, boolean isAllowedToExtendDateTime, boolean isAllowedToSeePhoto, boolean isAllowedToSeeTitle) {
        this.originalPath = originalPath;
        this.title = title;
        this.unlockDateTime = unlockDateTime;
        this.lockDateTime = lockDateTime;
        this.fileType = fileType;
        this.isFile = isFile;
        this.isAllowedToExtendDateTime = isAllowedToExtendDateTime;
        this.isAllowedToSeePhoto = isAllowedToSeePhoto;
        this.isAllowedToSeeTitle = isAllowedToSeeTitle;
    }

    public SavedFile(){

    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @NonNull
    public String getOriginalPath() {
        return originalPath;
    }

    public void setOriginalPath(@NonNull String originalPath) {
        this.originalPath = originalPath;
    }

    @NonNull
    public String getTitle() {
        return title;
    }

    public void setTitle(@NonNull String title) {
        this.title = title;
    }

    @NonNull
    public DateAndTime getUnlockDateTime() {
        return unlockDateTime;
    }

    public void setUnlockDateTime(@NonNull DateAndTime unlockDateTime) {
        this.unlockDateTime = unlockDateTime;
    }

    @NonNull
    public DateAndTime getLockDateTime() {
        return lockDateTime;
    }

    public void setLockDateTime(@NonNull DateAndTime lockDateTime) {
        this.lockDateTime = lockDateTime;
    }

    public boolean isFile() {
        return isFile;
    }

    public void setFile(boolean file) {
        isFile = file;
    }

    public boolean isAllowedToExtendDateTime() {
        return isAllowedToExtendDateTime;
    }

    public void setAllowedToExtendDateTime(boolean allowedToExtendDateTime) {
        isAllowedToExtendDateTime = allowedToExtendDateTime;
    }

    public boolean isAllowedToSeePhoto() {
        return isAllowedToSeePhoto;
    }

    public void setAllowedToSeePhoto(boolean allowedToSeePhoto) {
        isAllowedToSeePhoto = allowedToSeePhoto;
    }

    public boolean isAllowedToSeeTitle() {
        return isAllowedToSeeTitle;
    }

    public void setAllowedToSeeTitle(boolean allowedToSeeTitle) {
        isAllowedToSeeTitle = allowedToSeeTitle;
    }

    public int getFileType() {
        return fileType;
    }

    public void setFileType(int fileType) {
        this.fileType = fileType;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }
}
