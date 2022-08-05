package com.hz_apps.timebasedlocker.ui.LockFiles;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

public class DateAndTime implements Serializable {

    private LocalDate date;
    private LocalTime time = LocalTime.MIN;

    public DateAndTime(){

    }

    public DateAndTime(LocalDate date) {
        this.date = date;
    }

    public DateAndTime(LocalTime time) {
        this.time = time;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }
}
