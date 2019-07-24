package com.example.ainurbayanova.kolesa.mvp.presenters;

import com.example.ainurbayanova.kolesa.mvp.view.interfaces.UserFirebaseInterface;
import com.example.ainurbayanova.kolesa.mvp.modules.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class GetUserPresenter implements UserFirebaseInterface.model {

    @Override
    public void onResponse(final OnFinishedListener onFinishedListener, final String username) {
        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
        databaseReference.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot user:dataSnapshot.getChildren()){
                    if(user.child("username").getValue().toString().equals(username)){
                        User user1 = user.getValue(User.class);
                        onFinishedListener.onFinished(user1);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }
}
