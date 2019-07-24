package com.example.ainurbayanova.kolesa.mvp.view.activities;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.ainurbayanova.kolesa.mvp.view.fragments.ProfileFragments.DoneFragment;
import com.example.ainurbayanova.kolesa.mvp.view.fragments.ProfileFragments.GoalDoneFragment;
import com.example.ainurbayanova.kolesa.mvp.view.fragments.ProfileFragments.StatisticFragment;
import com.example.ainurbayanova.kolesa.R;
import com.example.ainurbayanova.kolesa.mvp.modules.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import de.hdodenhof.circleimageview.CircleImageView;

public class Profile extends AppCompatActivity implements StatisticFragment.ClickInterface{
    Toolbar toolbar;
    StatisticFragment statisticFragment;
    DoneFragment doneFragment;
    GoalDoneFragment goalDoneFragment;
    TextView email;
    TextView username;
    CircleImageView circleImageView;
    int point = 0;
    User main_user;
    TextView points;
    DatabaseReference databaseReference;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_profile);
        initAbove();
        initToolbar();
        initTabPage();
        initGradle();
        setValues();
        setClickListener();
    }

    private void initGradle() {
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            main_user = null;
            main_user = (User) bundle.getSerializable("main_user");
            Log.i("info",main_user.getUsername());

        }
    }

    public void initToolbar(){
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setTitle("Profile");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void initTabPage(){
        TabLayout tabLayout = findViewById(R.id.tabLayout);
        tabLayout.addTab(tabLayout.newTab().setText("Challenges"));
        tabLayout.addTab(tabLayout.newTab().setText("Statistics"));

        final ViewPager viewPager = findViewById(R.id.viewPager);
        TabPage tabbedPage = new TabPage(getSupportFragmentManager(),3);

        viewPager.setAdapter(tabbedPage);
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        doneFragment = (DoneFragment) tabbedPage.getItem(TabPage.CURRENT_TASK_FRAGMENT);

        Bundle bundle = getIntent().getExtras();
        doneFragment.setArguments(bundle);
        goalDoneFragment = (GoalDoneFragment) tabbedPage.getItem(TabPage.DONE_TASK_FRAGMENT);

        statisticFragment = (StatisticFragment) tabbedPage.getItem(TabPage.GOAL_DONE_TASK_FRAGMENT);
        statisticFragment.setArguments(bundle);
    }

    public void initAbove(){
        email = findViewById(R.id.email);
        username = findViewById(R.id.nameOfUser);
        circleImageView = findViewById(R.id.circleImage);
        points = findViewById(R.id.points);
        databaseReference = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.profile_menu,menu);
        MenuItem menuItem = menu.findItem(R.id.edit);
        if(main_user != null){
            if(!main_user.getUsername().equals(FirebaseAuth.getInstance().getCurrentUser().getDisplayName())){
                menuItem.setVisible(false);
            }
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId() == R.id.edit){
            Bundle bundle = getIntent().getExtras();
            Intent intent = new Intent(this,edit_profileActivity.class);
            intent.putExtra("bundle",bundle);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    public void setValues() {
        if(main_user != null){
            username.setText(main_user.getUsername());
            email.setText(main_user.getEmail());
            Glide.with(this)
                    .load(main_user.getImage())
                    .into(circleImageView);
            if(point < 0){
                points.setText(" "+ main_user.getPoints() + " dp");
                points.setTextColor(getResources().getColor(R.color.red));
            }
            else{
                points.setText(" "+ main_user.getPoints() + " dp");
                points.setTextColor(getResources().getColor(R.color.green));
            }
        }
    }

    public void setClickListener(){
        circleImageView.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Profile.this,ImageDetailActivity.class);
                intent.putExtra("image",main_user.getImage());
                Pair[] pairs = new Pair[1];
                pairs[0] = new Pair<View, String>(circleImageView,"imageTransition");

                ActivityOptions activityOptions = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    activityOptions = ActivityOptions.makeSceneTransitionAnimation(Profile.this,pairs);
                }
                startActivity(intent,activityOptions.toBundle());
            }
        });
    }

    @Override
    public void clicked(boolean isClicked) {
        if(isClicked){
            finish();
        }
    }
}
