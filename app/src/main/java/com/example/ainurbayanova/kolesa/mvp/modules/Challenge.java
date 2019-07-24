package com.example.ainurbayanova.kolesa.mvp.modules;

import java.io.Serializable;
import java.util.ArrayList;

public class Challenge implements Serializable {
    public String name,motivation,key;
    Date date;
    Time time;
    Boolean alarm;
    User admin;
    int challengers,points;
    ArrayList<UserForChallenge> users = new ArrayList<>();

    public Challenge(){

    }

    public Challenge(User admin,int challengers,String name,
                     String motivation, Date date,
                     Time time,String key,ArrayList<UserForChallenge> users,
                     Boolean alarm,int points) {
        this.admin = admin;
        this.challengers = challengers;
        this.name = name;
        this.motivation = motivation;
        this.time = time;
        this.date = date;
        this.key = key;
        this.users = users;
        this.alarm = alarm;
        this.points = points;
    }

    public Challenge(String name,String motivation,Date date,Time time){
        this.name = name;
        this.motivation = motivation;
        this.time = time;
        this.date = date;
    }

    public Challenge(String name,String motivation,Date date,Time time,ArrayList<UserForChallenge> users){
        this.name = name;
        this.motivation = motivation;
        this.time = time;
        this.date = date;
        this.users = users;
    }

    public int getPoints() {
        return points;
    }

    public Boolean getAlarm() {
        return alarm;
    }

    public String getKey() {
        return key;
    }

    public Date getDate() {
        return date;
    }

    public String getName() {
        return name;
    }

    public String getMotivation() {
        return motivation;
    }

    public Time getTime() {
        return time;
    }

    public User getAdmin() {
        return admin;
    }

    public int getChallengers() {
        return challengers;
    }

    public ArrayList<UserForChallenge> getUsers() {
        return users;
    }
}
