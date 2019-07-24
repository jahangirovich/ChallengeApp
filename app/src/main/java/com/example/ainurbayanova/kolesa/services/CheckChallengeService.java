package com.example.ainurbayanova.kolesa.services;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.example.ainurbayanova.kolesa.MainActivity;
import com.example.ainurbayanova.kolesa.R;
import com.example.ainurbayanova.kolesa.mvp.modules.Challenge;
import com.example.ainurbayanova.kolesa.mvp.modules.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

import static com.example.ainurbayanova.kolesa.mvp.view.activities.App.channel1_id;

public class CheckChallengeService extends Service {
    DatabaseReference databaseReference;
    User user;
    boolean userIs = false;
    boolean isAdmin = false;
    String username;
    ArrayList<Challenge> challenges = new ArrayList<>();

    NotificationManagerCompat notificationManagerCompat;

    @Override
    public void onCreate() {
        super.onCreate();
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        uploadUserByKey();
        return START_STICKY;
    }

    private void uploadChallenges(final String new_userKey) {
        user = new User(FirebaseAuth.getInstance().getCurrentUser().getDisplayName(), "");
        notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
        username = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();

        databaseReference.child("challenges").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                challenges.clear();
                for (DataSnapshot data:dataSnapshot.getChildren()){
                    boolean notify = Boolean.parseBoolean(data.child("alarm").getValue().toString());
                    userIs = false;
                    for (DataSnapshot into:data.getChildren()){
                        if(into.getKey().equals("users")){
                            for(DataSnapshot users:into.getChildren()){
                                String key = users.child("fKey").getValue().toString();
                                boolean isDone = Boolean.parseBoolean(users.child("done").getValue().toString());
                                if(key.equals(new_userKey) &&
                                        !isDone){
                                    userIs = true;
                                }
                            }
                        }
                    }
                    if(userIs && notify){
                        Challenge challenge = data.getValue(Challenge.class);
                        challenges.add(challenge);
                    }
                }

                Collections.reverse(challenges);

                checkActiveChallenges();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    public void uploadUserByKey(){
        databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String new_userKey = "";
                for (DataSnapshot data:dataSnapshot.getChildren()){
                    if(data.child("username").getValue().toString()
                            .equals(FirebaseAuth.getInstance().getCurrentUser().getDisplayName())){
                        new_userKey = data.child("key").getValue().toString();
                    }
                }
                uploadChallenges(new_userKey);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }


    public void checkActiveChallenges() {
        ArrayList<Challenge> new_collection = new ArrayList<>();
        for (Challenge challenge : challenges) {
            int now_month = getMonth();
            int now_day = getDay();
            int now_hour = getHour();
            int now_minute = getMinute();

            int user_month = challenge.getDate().getMonth();
            int user_day = challenge.getDate().getDay();
            int user_hour = challenge.getTime().getHour();
            int user_minute = challenge.getTime().getMinute();

            if ((now_month == user_month && user_day == now_day && now_hour < user_hour) ||
                    (now_month == user_month && user_day == now_day && now_hour == user_hour && user_minute >= now_minute)) {
                new_collection.add(challenge);
            }

        }

        if (new_collection.size() == 0) {
            return;
        } else {
            Intent notificationIntent = new Intent(getApplicationContext(), MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(),
                    0, notificationIntent, 0);

            NotificationCompat.InboxStyle inboxStyle =
                    new NotificationCompat.InboxStyle();
            for (int i = 0; i < new_collection.size(); i++) {
                inboxStyle.addLine(new_collection.get(i).getName());
            }
            inboxStyle.setBigContentTitle("List of challenges that have little time:");
            Notification notification = new NotificationCompat.Builder(getApplicationContext(), channel1_id)
                    .setContentTitle("Challenges")
                    .setStyle(inboxStyle)
                    .setSmallIcon(R.drawable.ic_flash_on_black_24dp)
                    .setContentIntent(pendingIntent)
                    .setDefaults(Notification.DEFAULT_SOUND)
                    .setSound(Settings.System.DEFAULT_NOTIFICATION_URI)
                    .setAutoCancel(true)
                    .build();
            notificationManagerCompat.notify(1, notification);
        }
    }

    public int getDay() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat dateformat = new SimpleDateFormat("d");
        int day = Integer.parseInt(dateformat.format(c.getTime()));
        return day;
    }

    public int getHour() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat dateformat = new SimpleDateFormat("H");
        int day = Integer.parseInt(dateformat.format(c.getTime()));
        return day;
    }

    public int getMinute() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat dateformat = new SimpleDateFormat("m");
        int day = Integer.parseInt(dateformat.format(c.getTime()));
        return day;
    }

    public int getMonth() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat dateformat = new SimpleDateFormat("M");
        int month = Integer.parseInt(dateformat.format(c.getTime()));
        return month;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
