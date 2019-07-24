package com.example.ainurbayanova.kolesa.mvp.view.interfaces;

import android.widget.TextView;

import com.example.ainurbayanova.kolesa.mvp.modules.Challenge;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public interface InitializeInterface {
    void removeItems(ArrayList<Challenge> challenges,int position,int total1,int total2,String timeOfEnd);
    void removeChallenge(Challenge challenges);
    void initializeAdmin(TextView adminUsername, CircleImageView adminImage, String challengeKey);
}
