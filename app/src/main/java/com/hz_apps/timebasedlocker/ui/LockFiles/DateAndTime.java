package com.hz_apps.timebasedlocker.ui.LockFiles;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

public class DateAndTime implements Serializable {

    private LocalDate date;
    private LocalTime time = LocalTime.MIN;

    public DateAndTime() {

    }

    public DateAndTime(@NonNull LocalDate date, @NonNull LocalTime time) {
        this.date = date;
        this.time = time;
    }

    public static DateAndTime parse(String dateAndTime) {
        String[] dateAndTimeArray = dateAndTime.split(" ");
        LocalDate date = LocalDate.parse(dateAndTimeArray[0]);
        LocalTime time = LocalTime.parse(dateAndTimeArray[1]);
        return new DateAndTime(date, time);
    }

    public static DateAndTime of(int YEAR, int MONTH, int DayOfMonth, int Hour, int Minute) {
        LocalDate localDate = LocalDate.of(YEAR, MONTH, DayOfMonth);
        LocalTime localTime = LocalTime.of(Hour, Minute);
        return new DateAndTime(localDate, localTime);
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

    @NonNull
    @Override
    public String toString() {
        return date.toString() + " " + time.toString();
    }
}
