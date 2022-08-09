package com.hz_apps.timebasedlocker.Datebase;


import androidx.room.TypeConverter;

import com.hz_apps.timebasedlocker.ui.LockFiles.DateAndTime;

public class Converters {
    @TypeConverter
    public String fromDateAndTime(DateAndTime dateAndTime) {
        return dateAndTime.toString();
    }

    @TypeConverter
    public DateAndTime toDateAndTime(String dateAndTimeString) {
        return DateAndTime.parse(dateAndTimeString);
    }

}
