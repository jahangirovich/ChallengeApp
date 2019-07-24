package com.example.ainurbayanova.kolesa.mvp.modules;

import java.io.Serializable;

public class User implements Serializable{
    public String username,password,email,key,image;
    public int points;
    boolean done;
    public User(){

    }

    public User(String username, String password, String email, String key,String image,int points) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.key = key;
        this.image = image;
        this.points = points;
    }

    public User(String username, String image){
        this.username = username;
        this.image = image;
    }

    public User(String key){
        this.key = key;
    }

    public boolean isDone() {
        return done;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getEmail() {
        return email;
    }

    public String getKey() {
        return key;
    }

    public String getImage() {
        return image;
    }

    public int getPoints() {
        return points;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public void setPoints(int points) {
        this.points = points;
    }
}
