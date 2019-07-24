package com.example.ainurbayanova.kolesa.mvp.presenters;

import com.example.ainurbayanova.kolesa.mvp.view.interfaces.FirebaseLoadInterface;
import com.example.ainurbayanova.kolesa.mvp.modules.Challenge;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class GetDataPresenter implements FirebaseLoadInterface.model {

    @Override
    public void onResponse(final OnFinishedListener onFinishedListener, final String fKey) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("challenges").limitToLast(4).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<Challenge> challenges = new ArrayList<>();
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    boolean userIs = false;
                    for (DataSnapshot into : data.getChildren()) {
                        if (into.getKey().equals("users")) {
                            for (DataSnapshot users : into.getChildren()) {
                                if(users.child("fKey").getValue().toString()
                                        .equals(fKey)){
                                    userIs = true;
                                }
                            }
                        }
                    }
                    if (userIs) {
                        Challenge challenge = data.getValue(Challenge.class);
                        challenges.add(challenge);
                    }
                }
                onFinishedListener.onFinished(challenges);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
