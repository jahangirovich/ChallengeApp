package com.example.ainurbayanova.kolesa.mvp.view.fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.example.ainurbayanova.kolesa.adapters.GoalsAdapter;
import com.example.ainurbayanova.kolesa.mvp.view.fragments.Little_Fragments.DialogDetailOfGoal;
import com.example.ainurbayanova.kolesa.R;
import com.example.ainurbayanova.kolesa.mvp.view.interfaces.DetailOfChallenges;
import com.example.ainurbayanova.kolesa.mvp.modules.Goal;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;

public class GoalsFragment extends Fragment {


    public GoalsFragment() {

    }

    View view;
    ProgressBar progressBar;
    GoalsAdapter goalsAdapter;
    ArrayList<Goal> goals = new ArrayList<>();
    RecyclerView recyclerView;
    DatabaseReference databaseReference;
    String key = "";
    RelativeLayout loader;
    LinearLayout thereIsNo;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_widget_fragment, container, false);
        initWidgets();
        findUserKey();
        return view;
    }

    private void initWidgets() {
        progressBar = view.findViewById(R.id.progressBar);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        recyclerView = view.findViewById(R.id.recyclerView);
        loader = view.findViewById(R.id.loader);
        thereIsNo = view.findViewById(R.id.thereIsNo);
    }

    private void initRecycler(){
        ArrayList<Goal> goaling = new ArrayList<>();
        goalsAdapter = new GoalsAdapter(getActivity(),goals);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(goalsAdapter);
        goalsAdapter.setOnAddClickListener(new DetailOfChallenges() {
            @Override
            public void ClickI(int position) {
                DialogDetailOfGoal detailOfGoal = new DialogDetailOfGoal();
                Bundle data = new Bundle();
                data.putSerializable("goal",goals.get(position));
                detailOfGoal.setArguments(data);
                detailOfGoal.show(getActivity().getSupportFragmentManager(),"detailActivity");
            }
        });
    }

    public void findUserKey(){
        databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot data:dataSnapshot.getChildren()){
                    if(data.child("username").getValue().toString()
                            .equals(FirebaseAuth.getInstance().getCurrentUser().getDisplayName())){
                        key = data.child("key").getValue().toString();
                    }
                }
                databaseReference.child("Goals").addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        goals.clear();
                        for (DataSnapshot data:dataSnapshot.getChildren()){
                            if(data.child("userKey").getValue().toString().equals(key)){
                                Goal goal = data.getValue(Goal.class);
                                goals.add(goal);
                            }
                        }
                        initRecycler();
                        loader.setVisibility(View.GONE);
                        if(goals.size() != 0){
                            thereIsNo.setVisibility(View.GONE);
                        }
                        else{
                            thereIsNo.setVisibility(View.VISIBLE);
                        }
                        Collections.reverse(goals);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }



}
