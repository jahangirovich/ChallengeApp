package com.example.ainurbayanova.kolesa.services;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

import com.example.ainurbayanova.kolesa.MainActivity;
import com.example.ainurbayanova.kolesa.R;
import com.example.ainurbayanova.kolesa.mvp.modules.Challenge;
import com.example.ainurbayanova.kolesa.mvp.modules.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static com.example.ainurbayanova.kolesa.mvp.view.activities.App.channel1_id;

public class ChallengeService extends Service {
    DatabaseReference databaseReference;
    User user;
    boolean userIs = false;
    boolean isAdmin = false;
    String username;

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

    private void uploadChallenges(final String userKey) {
        user = new User(FirebaseAuth.getInstance().getCurrentUser().getDisplayName(),
                FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl().toString());
        notificationManagerCompat = NotificationManagerCompat.from(getApplicationContext());
        username = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        databaseReference.child("challenges").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                userIs = false;

                Challenge challenge = dataSnapshot.getValue(Challenge.class);

                for (DataSnapshot into : dataSnapshot.getChildren()) {
                    if (into.getKey().equals("users")) {
                        for (DataSnapshot users : into.getChildren()) {
                            String user = users.child("fKey").getValue().toString();
                            if (user.equals(userKey)) {
                                userIs = true;
                            }
                        }
                    }
                }

                if (userIs) {
                    findAdmin(userKey,challenge.getName());
                }
            }


            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

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

    public void findAdmin(final String admin,final String name) {
        databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String user = "";
                for (DataSnapshot data:dataSnapshot.getChildren()){
                    if(data.child("key").getValue().toString()
                            .equals(admin)){
                        user = data.child("username").getValue().toString();
                    }
                }
                NotificationManager mNotificationManager =
                        (NotificationManager) getApplicationContext().getSystemService(Context.NOTIFICATION_SERVICE);
                Intent notificationsIntent = new Intent(getApplicationContext(), MainActivity.class);
                notificationsIntent.addFlags(Intent.FLAG_FROM_BACKGROUND);
                PendingIntent pendingIntent = PendingIntent.getActivity(getApplicationContext(), 0, notificationsIntent,
                        PendingIntent.FLAG_ONE_SHOT);

                Notification notification = new NotificationCompat.Builder(getApplicationContext(), channel1_id)
                        .setContentTitle(takeAdmin(user) + " created new Challenge")
                        .setContentText(name + "")
                        .setSmallIcon(R.drawable.ic_flash_on_black_24dp)
                        .setContentIntent(pendingIntent)
                        .setDefaults(Notification.DEFAULT_SOUND)
                        .setAutoCancel(true)
                        .build();
                mNotificationManager.notify(1, notification);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public String takeAdmin(String user){
        if(user.equals(FirebaseAuth.getInstance().getCurrentUser().getDisplayName())){
            return "You";
        }
        else{
            return user;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
