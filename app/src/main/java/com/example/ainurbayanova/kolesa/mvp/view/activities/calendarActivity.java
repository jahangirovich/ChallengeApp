package com.example.ainurbayanova.kolesa.mvp.view.activities;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CalendarView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;


import com.example.ainurbayanova.kolesa.adapters.DoneChallengesAdapter;
import com.example.ainurbayanova.kolesa.R;
import com.example.ainurbayanova.kolesa.mvp.view.interfaces.UserFirebaseInterface;
import com.example.ainurbayanova.kolesa.mvp.view.interfaces.UserStoriesInterface;
import com.example.ainurbayanova.kolesa.mvp.modules.Challenges_done;
import com.example.ainurbayanova.kolesa.mvp.modules.User;
import com.example.ainurbayanova.kolesa.mvp.presenters.MainStoriesPresenter;
import com.example.ainurbayanova.kolesa.mvp.presenters.MainUserPresenter;
import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class calendarActivity extends AppCompatActivity implements UserStoriesInterface.View,UserFirebaseInterface.View{
    CalendarView calendarView;
    MainStoriesPresenter mainStoriesPresenter;
    MainUserPresenter mainUserPresenter;
    Toolbar toolbar;
    User main_user;
    ArrayList<Challenges_done> challenges_dones = new ArrayList<>();
    DoneChallengesAdapter doneChallengesAdapter;
    RecyclerView recyclerView;
    ArrayList<Challenges_done> date_challenges = new ArrayList<>();
    LinearLayout showResult;
    ProgressBar circularProgressBar;
    TextView amount;
    TextView percent;
    RelativeLayout noLayout;
    ProgressBar progressBar;
    RelativeLayout all;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        initToolbar();
        initWidgets();
        uploadUser();
    }

    private void initToolbar() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    private void uploadUser() {
        mainUserPresenter = new MainUserPresenter(this);
        mainUserPresenter.requestFromFirbase(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
    }

    private void initWidgets() {
        calendarView = findViewById(R.id.materialCalendarView);
        recyclerView = findViewById(R.id.recyclerView);
        amount = findViewById(R.id.amount);
        all = findViewById(R.id.all);
        showResult = findViewById(R.id.showResult);
        noLayout = findViewById(R.id.noLayout);
        percent = findViewById(R.id.percentage);
        progressBar = findViewById(R.id.progressBar);
        circularProgressBar = findViewById(R.id.circularProgressBar);
    }

    public void initCalendar(){
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                if(challenges_dones != null){
                    date_challenges.clear();
                    addToThatDay(dayOfMonth,month);
                    initSizesForRecycler();
                    setProgressResults();
                }
            }
        });
        addToThatDay(getDay(),getMonth()-1);
    }

    private void addToThatDay(int day, int i) {
    }

    public void setProgressResults(){
        circularProgressBar.setProgress(getTotal() * 100/500);
        percent.setText(getTotal() * 100/500 + "%");
        amount.setText(getTotal() + "/" + 500);
    }

    public int getMonth() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat dateformat = new SimpleDateFormat("M");
        int month = Integer.parseInt(dateformat.format(c.getTime()));
        return month;
    }

    public int getDay() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat dateformat = new SimpleDateFormat("d");
        int day = Integer.parseInt(dateformat.format(c.getTime()));
        return day;
    }

    public int getTotal(){
        int total = 0;
        for (Challenges_done challenges_done:date_challenges){
            total += challenges_done.getPoint();
        }
        return total;
    }

    public void initSizesForRecycler(){
        if(date_challenges.size() == 0){
            noLayout.setVisibility(View.VISIBLE);
            showResult.setVisibility(View.GONE);
            Animation animFadeOut = AnimationUtils.loadAnimation(calendarActivity.this,
                    R.anim.fade_in);
            showResult.startAnimation(animFadeOut);
            recyclerView.setVisibility(View.GONE);
        }
        else{
            noLayout.setVisibility(View.GONE);
            showResult.setVisibility(View.VISIBLE);
            Animation animFadeOut = AnimationUtils.loadAnimation(calendarActivity.this,
                    R.anim.fade_out);
            showResult.startAnimation(animFadeOut);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }

    public void initRecyclerChallenge() {
        doneChallengesAdapter = new DoneChallengesAdapter(this, date_challenges);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(doneChallengesAdapter);
    }

    @Override
    public void showProgressBar() {

    }

    @Override
    public void hideProgressBar() {

    }

    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void setData(User user) {
        main_user = user;
        mainStoriesPresenter = new MainStoriesPresenter(this);
        mainStoriesPresenter.requestFromFirebase(user.getKey());

    }

    @Override
    public void setDatasToRecylerView(ArrayList<Challenges_done> challenges) {
        challenges_dones = challenges;
        setProgressResults();
        initCalendar();
    }
}
