package com.example.ainurbayanova.kolesa.mvp.modules;

import java.io.Serializable;

public class Challenges_done implements Serializable {
    public int point;
    public String userKey;
    public Challenge challenge;
    public String fKey;
    public String days;
    public Date date;
    public Time time;
    public Challenges_done(){

    }
    public Challenges_done(int point,String userKey,Challenge challenge,String days,Date date,Time time){
        this.point = point;
        this.userKey = userKey;
        this.challenge = challenge;
        this.days = days;
        this.date = date;
        this.time = time;
    }

    public Challenges_done(int point,String userKey,Challenge challenge,String days){
        this.point = point;
        this.userKey = userKey;
        this.challenge = challenge;
        this.days = days;
        this.date = date;
        this.time = time;
    }


    public Date getDate() {
        return date;
    }

    public Time getTime() {
        return time;
    }

    public String getDays() {
        return days;
    }

    public void setfKey(String fKey) {
        this.fKey = fKey;
    }

    public int getPoint() {
        return point;
    }

    public String getUserKey() {
        return userKey;
    }

    public Challenge getChallenge() {
        return challenge;
    }

    public String getfKey() {
        return fKey;
    }
}
