package com.example.ainurbayanova.kolesa.mvp.modules;

public class FeedBack {
    public String userId,text,key;

    public FeedBack(){

    }

    public FeedBack(String userId, String text,String key) {
        this.userId = userId;
        this.text = text;
        this.key = key;
    }

    public String getKey() {
        return key;
    }

    public String getUserId() {
        return userId;
    }

    public String getText() {
        return text;
    }
}
