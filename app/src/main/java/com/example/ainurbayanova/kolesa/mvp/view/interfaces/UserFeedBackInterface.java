package com.example.ainurbayanova.kolesa.mvp.view.interfaces;

import android.widget.TextView;

import com.example.ainurbayanova.kolesa.mvp.modules.FeedBack;

import de.hdodenhof.circleimageview.CircleImageView;

public interface UserFeedBackInterface {
    void initUser(TextView username, CircleImageView profileImage, FeedBack feedBack);
}
