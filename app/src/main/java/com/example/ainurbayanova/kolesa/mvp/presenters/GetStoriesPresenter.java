package com.example.ainurbayanova.kolesa.mvp.presenters;

import com.example.ainurbayanova.kolesa.mvp.view.interfaces.UserStoriesInterface;
import com.example.ainurbayanova.kolesa.mvp.modules.Challenges_done;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class GetStoriesPresenter implements UserStoriesInterface.Model{

    @Override
    public void onResponse(final OnFinishedListener onFinishedListener, String fKey) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("challenges_done").child(fKey).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<Challenges_done> challenges_dones = new ArrayList<>();
                for (DataSnapshot challenges_done:dataSnapshot.getChildren()){
                    Challenges_done challenge = challenges_done.getValue(Challenges_done.class);
                    challenges_dones.add(challenge);
                }
                onFinishedListener.onFinished(challenges_dones);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
