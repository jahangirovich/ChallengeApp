package com.example.ainurbayanova.kolesa.mvp.presenters;

import com.example.ainurbayanova.kolesa.mvp.view.interfaces.GetUserPoints;
import com.example.ainurbayanova.kolesa.mvp.modules.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class GetPointsPresenter implements GetUserPoints.Model {
    @Override
    public void getPoints(final OnFinishedListener onFinishedListener) {
        DatabaseReference databaseReferenced = FirebaseDatabase.getInstance().getReference();
        databaseReferenced.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<User> pointLists = new ArrayList<>();
                for (DataSnapshot data:dataSnapshot.getChildren()){
                    User user = data.getValue(User.class);
                    pointLists.add(user);
                }
                ArrayList<User> new_list = bubbleSort(pointLists);
                Collections.reverse(new_list);
                onFinishedListener.onFinished(new_list);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public ArrayList<User> bubbleSort(ArrayList<User> points) {
        int i, j, temp = 0;
        String usernames = "";
        String image = "";
        String email = "";
        String key = "";
        for (i = 0; i < points.size() - 1; i++) {
            for (j = 0; j < points.size() - 1 - i; j++) {
                if (points.get(j).getPoints() > points.get(j + 1).getPoints()) {
                    temp = points.get(j).getPoints();
                    usernames = points.get(j).getUsername();
                    image = points.get(j).getImage();
                    email = points.get(j).getEmail();
                    key = points.get(j).getKey();

                    points.get(j).setPoints(points.get(j + 1).getPoints());
                    points.get(j).setImage(points.get(j + 1).getImage());
                    points.get(j).setUsername(points.get(j + 1).getUsername());
                    points.get(j).setEmail(points.get(j + 1).getEmail());
                    points.get(j).setKey(points.get(j + 1).getKey());

                    points.get(j + 1).setPoints(temp);
                    points.get(j + 1).setUsername(usernames);
                    points.get(j + 1).setImage(image);
                    points.get(j + 1).setEmail(email);
                    points.get(j + 1).setKey(key);
                }
            }
        }
        return points;
    }
}
