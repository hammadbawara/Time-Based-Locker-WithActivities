package com.hz_apps.timebasedlocker.Datebase;

import androidx.annotation.NonNull;

import com.hz_apps.timebasedlocker.ui.LockFiles.DateAndTime;

import java.io.Serializable;

public class SavedFile implements Serializable {
    private int id;
    private String path;
    private String originalPath;
    private String name;
    private DateAndTime unlockDateTime;
    private DateAndTime lockDateTime;
    private int fileType;
    private boolean isAllowedToExtendDateTime;
    private boolean isAllowedToSeePhoto;
    private boolean isAllowedToSeeTitle;
    private boolean isUnlocked;

    public SavedFile(String originalPath, String name, DateAndTime unlockDateTime, DateAndTime lockDateTime, int fileType, boolean isAllowedToExtendDateTime, boolean isAllowedToSeePhoto, boolean isAllowedToSeeTitle) {
        this.originalPath = originalPath;
        this.name = name;
        this.unlockDateTime = unlockDateTime;
        this.lockDateTime = lockDateTime;
        this.fileType = fileType;
        this.isAllowedToExtendDateTime = isAllowedToExtendDateTime;
        this.isAllowedToSeePhoto = isAllowedToSeePhoto;
        this.isAllowedToSeeTitle = isAllowedToSeeTitle;
    }

    public SavedFile() {

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
    public String getName() {
        return name;
    }

    public void setName(@NonNull String name) {
        this.name = name;
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

    public boolean isUnlocked(){
        return isUnlocked;
    }

    public void setIsUnlocked(boolean locked){
        this.isUnlocked = locked;
    }
}
