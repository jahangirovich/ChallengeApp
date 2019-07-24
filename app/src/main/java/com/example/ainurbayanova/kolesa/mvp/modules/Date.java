package com.example.ainurbayanova.kolesa.mvp.modules;

import java.io.Serializable;

public class Date implements Serializable {
    int year,month,day;

    public Date(){

    }

    public Date(int year,int month, int day) {
        this.month = month;
        this.day = day;
        this.year = year;
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public int getDay() {
        return day;
    }
}
