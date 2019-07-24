package com.example.ainurbayanova.kolesa.mvp.view.activities;

import android.app.Application;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

import com.google.firebase.FirebaseApp;

public class App extends Application {
    public static final String channel1_id = "channel1";
    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseApp.initializeApp(this);
        createNotificatinChannel();
    }
    private void createNotificatinChannel(){
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            NotificationChannel channel1 = new NotificationChannel(
                    channel1_id,
                    "Channel of Challenge App",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel1.setDescription("Channel1");
            NotificationManager manager = getSystemService(NotificationManager.class);

            manager.createNotificationChannel(channel1);
        }
    }
}
