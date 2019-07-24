package com.example.ainurbayanova.kolesa.mvp.modules;

public class About {
    int image;
    String name,duty;

    public About(int image, String name, String duty) {
        this.image = image;
        this.name = name;
        this.duty = duty;
    }

    public int getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public String getDuty() {
        return duty;
    }
}
