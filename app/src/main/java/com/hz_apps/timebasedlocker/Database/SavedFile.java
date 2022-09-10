package com.hz_apps.timebasedlocker.Database;

import androidx.annotation.NonNull;

import com.hz_apps.timebasedlocker.ui.LockFiles.DateAndTime;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

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

    /**
     *
     * @param currentDateTime : Current Date And Time
     * @return Highest possible time value.
     *         If remaining time is '10 days 5 hours 2 minute'
     *         then it will return 10 days.
     *         Similarly if time is '5 hours 2 minutes' then it will
     *         return 5 hours.
     */
    public String getTimeLeftToUnlock(DateAndTime currentDateTime){

        if (currentDateTime == null){
            return "update time";
        }

        LocalDate cDate = currentDateTime.getDate();
        LocalTime cTime = currentDateTime.getTime();
        LocalDate unlDate = unlockDateTime.getDate();
        LocalTime unTime = unlockDateTime.getTime();


        if ((unlDate.getYear() - cDate.getYear()) > 0) {
            int YEARS = unlDate.getYear() - cDate.getYear();
            if (YEARS == 1) return ("1 year");
            else return (YEARS + " years");
        }
        if ((unlDate.getMonthValue() - cDate.getMonthValue()) > 0) {
            int MONTHS = unlDate.getMonthValue() - cDate.getMonthValue();
            if (MONTHS == 1) return ("1 month");
            else return (MONTHS + " months");
        }
        if ((unlDate.getDayOfMonth() - cDate.getDayOfMonth()) > 0) {
            int DAYS = unlDate.getDayOfMonth() - cDate.getDayOfMonth();
            if (DAYS == 1) return ("1 day");
            else return (DAYS + " days");
        }
        if ((unTime.getHour() - cTime.getHour()) > 0) {
            int HOURS = unTime.getHour() - cTime.getHour();
            if (HOURS == 1) return ("1 hour");
            else return (HOURS + " hours");
        }
        if ((unTime.getMinute() - cTime.getMinute()) > 0) {
            int MINUTES = unTime.getMinute() - cTime.getMinute();
            if (MINUTES == 1) return ("1 minute");
            else return (MINUTES + " minutes");
        }
        if ((unTime.getSecond() - cTime.getSecond()) > 0) {
            int SECONDS = unTime.getSecond()-cTime.getSecond();
            if (SECONDS == 1) return "1 second";
            else return (SECONDS + " seconds");
        }
        return "";
    }
}
