package com.example.ainurbayanova.kolesa.mvp.modules;

public class Point {
    int point;
    String image,username;

    public Point(){

    }

    public Point(int point, String image, String username) {
        this.point = point;
        this.image = image;
        this.username = username;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getImage() {
        return image;
    }

    public int getPoint() {
        return point;
    }

    public String getUsername() {
        return username;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
