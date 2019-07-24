package com.example.ainurbayanova.kolesa.mvp.view.fragments;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.ainurbayanova.kolesa.mvp.view.activities.calendarActivity;
import com.example.ainurbayanova.kolesa.mvp.view.activities.detailActivity;
import com.example.ainurbayanova.kolesa.adapters.ChallengeGridAdapter;
import com.example.ainurbayanova.kolesa.R;
import com.example.ainurbayanova.kolesa.mvp.view.interfaces.DetailOfChallenges;
import com.example.ainurbayanova.kolesa.mvp.view.interfaces.FirebaseLoadInterface;
import com.example.ainurbayanova.kolesa.mvp.view.interfaces.UserFirebaseInterface;
import com.example.ainurbayanova.kolesa.mvp.modules.Challenge;
import com.example.ainurbayanova.kolesa.mvp.modules.User;
import com.example.ainurbayanova.kolesa.mvp.modules.UserForChallenge;
import com.example.ainurbayanova.kolesa.mvp.presenters.MainDataPresenter;
import com.example.ainurbayanova.kolesa.mvp.presenters.MainUserPresenter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class Challenges extends Fragment implements FirebaseLoadInterface.View,UserFirebaseInterface.View{


    public Challenges() {

    }

    View view;
    ChallengeGridAdapter adapter;
    GridView gridView;
    DatabaseReference databaseReference;
    User user;
    ArrayList<Challenge> challengeArrayList = new ArrayList<>();
    FirebaseUser fUser;
    boolean doneKey;
    User main_user = null;
    LinearLayout loaderLayout;
    TextView loadingText;
    ProgressBar progressBar;
    LinearLayout thereIsNoLayout;
    LinearLayout more;
    ProgressBar circularProgressBar;
    TextView percent;
    ProgressDialog dialog;
    TextView limit;
    LinearLayout showCalendar;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_group, container, false);
        dialog = new ProgressDialog(getActivity());
        initWidgets();
        setUsername();
        initGrid();
        clickListenerForOthers();
        return view;
    }

    public void clickListenerForOthers(){
        showCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), calendarActivity.class));
            }
        });
    }

    public void initGrid(){
        adapter = new ChallengeGridAdapter(getActivity(), challengeArrayList);
        gridView.setAdapter(adapter);
    }

    private void setUsername() {
        MainUserPresenter mainUserPresenter = new MainUserPresenter(this);
        mainUserPresenter.requestFromFirbase(user.getUsername());
    }

    private void initWidgets() {
        databaseReference = FirebaseDatabase.getInstance().getReference();
        gridView = view.findViewById(R.id.gridView);
        loaderLayout = view.findViewById(R.id.loaderLayout);
        loadingText = view.findViewById(R.id.textOfLoading);
        progressBar = view.findViewById(R.id.progressBar);
        showCalendar = view.findViewById(R.id.showCalendar);
        more = view.findViewById(R.id.more);
        circularProgressBar = view.findViewById(R.id.circularProgressBar);
        percent = view.findViewById(R.id.percentage);
        fUser = FirebaseAuth.getInstance().getCurrentUser();
        limit = view.findViewById(R.id.limit);
        thereIsNoLayout = view.findViewById(R.id.thereIsNoLayout);
        if (fUser != null) {
            user = new User(fUser.getDisplayName(),
                    "");
        }
    }

    public void clickListenerForRecycler(final ArrayList<Challenge> challengesList) {
        adapter.setClickListener(new DetailOfChallenges() {
            @Override
            public void ClickI(final int position) {
                dialog.show();
                for (UserForChallenge user : challengesList.get(position).getUsers()) {
                    if(user.getfKey().equals(main_user.getKey())){
                        doneKey = user.isDone();
                        dialog.dismiss();
                    }
                }

                Intent intent = new Intent(getActivity(), detailActivity.class);
                intent.putExtra("challenge", challengesList.get(position));
                intent.putExtra("key", main_user.getKey());
                intent.putExtra("done", doneKey);
                intent.putExtra("myPoint", main_user.getPoints());
                startActivity(intent);
            }
        });
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {
        loaderLayout.setVisibility(View.GONE);
    }

    @Override
    public void setData(User user) {
        MainDataPresenter mainDataPresenter = new MainDataPresenter(this);
        mainDataPresenter.requestFromFirbase(user.getKey());
        main_user = user;
        setAllCredentialsForPercent();
    }

    public void setAllCredentialsForPercent(){
        percent.setText(main_user.getPoints() * 100 / 500 + "%");
        circularProgressBar.setProgress(main_user.getPoints() * 100/500);
        limit.setText(main_user.getPoints() + "/" + 1000);
    }

    @Override
    public void setDatasToRecylerView(ArrayList<Challenge> challenges) {
        challengeArrayList.clear();
        challengeArrayList.addAll(challenges);
        initializeFab(challenges);
        adapter.notifyDataSetChanged();
        clickListenerForRecycler(challenges);
    }

    @SuppressLint("RestrictedApi")
    private void initializeFab(ArrayList<Challenge> challenges) {
        if(challenges.size() == 4){
            more.setVisibility(View.VISIBLE);
        }
        if(challenges.size() == 0){
            thereIsNoLayout.setVisibility(View.VISIBLE);
        }
        else{
            thereIsNoLayout.setVisibility(View.GONE);
        }
    }

}
