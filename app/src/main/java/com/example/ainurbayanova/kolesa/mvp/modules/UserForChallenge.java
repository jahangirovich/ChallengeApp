package com.example.ainurbayanova.kolesa.mvp.modules;

import java.io.Serializable;

public class UserForChallenge implements Serializable{
    public String username;
    private boolean admin;
    private boolean done;
    String fKey;

    public UserForChallenge(){

    }

    public UserForChallenge(String fKey){
        this.fKey = fKey;
    }

    public UserForChallenge(String fKey,boolean admin){
        this.fKey = fKey;
        this.admin = admin;
    }

    public UserForChallenge(String username, boolean admin, boolean done,String fKey) {
        this.username = username;
        this.admin = admin;
        this.done = done;
        this.fKey = fKey;
    }

    public String getfKey() {
        return fKey;
    }

    public String getUsername() {
        return username;
    }

    public boolean isAdmin() {
        return admin;
    }

    public boolean isDone() {
        return done;
    }
}
