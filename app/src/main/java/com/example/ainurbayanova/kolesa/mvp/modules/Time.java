package com.example.ainurbayanova.kolesa.mvp.modules;

import java.io.Serializable;

public class Time implements Serializable {
    int hour,minute;

    public Time(){

    }

    public Time(int hour, int minute) {
        this.hour = hour;
        this.minute = minute;
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }
}
