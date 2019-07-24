package com.example.ainurbayanova.kolesa.mvp.modules;

import java.io.Serializable;

public class Goal implements Serializable{
    String title,userKey,motivation,fKey;
    int point;
    Date date;
    Time time;
    int color;
    boolean active;

    public Goal(){

    }

    public Goal(String title, String motivation, int point, Date date, Time time, boolean active) {
        this.title = title;
        this.motivation = motivation;
        this.point = point;
        this.date = date;
        this.time = time;
        this.active = active;
    }

    public Goal(String title, String userKey, String motivation, Date date, Time time, int color, int point, String fKey, boolean active) {
        this.title = title;
        this.userKey = userKey;
        this.motivation = motivation;
        this.date = date;
        this.time = time;
        this.color = color;
        this.point = point;
        this.fKey = fKey;
        this.active = active;
    }

    public Goal(String title) {
        this.title = title;
    }

    public boolean isActive() {
        return active;
    }

    public String getfKey() {
        return fKey;
    }

    public int getPoint() {
        return point;
    }

    public String getUserKey() {
        return userKey;
    }

    public String getMotivation() {
        return motivation;
    }

    public Date getDate() {
        return date;
    }

    public Time getTime() {
        return time;
    }

    public int getColor() {
        return color;
    }

    public String getTitle() {
        return title;
    }
}
