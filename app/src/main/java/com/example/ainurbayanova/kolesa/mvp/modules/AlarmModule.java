package com.example.ainurbayanova.kolesa.mvp.modules;

public class AlarmModule {
    int id;
    int month;
    int day;
    int hour;
    int minute;
    String username,fKeyOfChallenge;

    public AlarmModule(int month, int day, int hour, int minute,String username,String fKeyOfChallenge) {
        this.month = month;
        this.day = day;
        this.hour = hour;
        this.minute = minute;
        this.fKeyOfChallenge = fKeyOfChallenge;
        this.username = username;
    }

    public String getfKeyOfChallenge() {
        return fKeyOfChallenge;
    }

    public String getUsername() {
        return username;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public int getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }
}

