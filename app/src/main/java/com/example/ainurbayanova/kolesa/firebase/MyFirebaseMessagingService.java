package com.example.ainurbayanova.kolesa.firebase;

import android.app.NotificationManager;
import android.content.Context;
import android.media.RingtoneManager;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.example.ainurbayanova.kolesa.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.util.Map;

import static com.example.ainurbayanova.kolesa.mvp.view.activities.App.channel1_id;

public class MyFirebaseMessagingService extends FirebaseMessagingService {
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);
        if(remoteMessage.getData() != null){
            sendNotification(remoteMessage);
        }
    }
    public void sendNotification(RemoteMessage remoteMessage){
        Map<String,String> data = remoteMessage.getData();
        String title = data.get("title");
        String content = data.get("content");

        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){

        }
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, channel1_id)
                .setContentTitle(remoteMessage.getNotification().getTitle())
                .setContentText(remoteMessage.getNotification().getBody())
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setStyle(new NotificationCompat.BigTextStyle())
                .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        notificationManager.notify(0, notificationBuilder.build());



    }
}
