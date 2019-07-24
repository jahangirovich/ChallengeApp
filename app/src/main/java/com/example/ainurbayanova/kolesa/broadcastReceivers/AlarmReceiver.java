package com.example.ainurbayanova.kolesa.broadcastReceivers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.example.ainurbayanova.kolesa.services.ExampleService;

public class AlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        context.startService(new Intent(context,ExampleService.class));
    }


    //    notificationManagerCompat = NotificationManagerCompat.from(context);
//        Intent notificationIntent = new Intent(context,MainActivity.class);
//        PendingIntent pendingIntent = PendingIntent.getActivity(context,
//                0,notificationIntent,0);
//
//        Notification notification = new NotificationCompat.Builder(context,channel1_id)
//                .setContentTitle("Alarm")
//                .setContentText("Wake up")
//                .setSmallIcon(R.drawable.ic_flash_on_black_24dp)
//                .setContentIntent(pendingIntent)
//                .setVibrate(new long[] { 1000, 1000, 1000, 1000, 1000 })
//                .setDefaults(Notification.DEFAULT_SOUND)
//                .setAutoCancel(true)
//                .build();
//        notificationManagerCompat.notify(1,notification);
}
