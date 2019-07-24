package com.example.ainurbayanova.kolesa;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.bumptech.glide.Glide;
import com.example.ainurbayanova.kolesa.mvp.view.activities.aboutUsActivity;
import com.example.ainurbayanova.kolesa.mvp.view.interfaces.InitializeInterface;
import com.example.ainurbayanova.kolesa.mvp.view.interfaces.UserFirebaseInterface;
import com.example.ainurbayanova.kolesa.mvp.modules.Challenge;
import com.example.ainurbayanova.kolesa.mvp.modules.Challenges_done;
import com.example.ainurbayanova.kolesa.mvp.modules.Date;
import com.example.ainurbayanova.kolesa.mvp.modules.Time;
import com.example.ainurbayanova.kolesa.mvp.modules.User;
import com.example.ainurbayanova.kolesa.mvp.modules.UserForChallenge;

import android.support.v7.widget.Toolbar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ainurbayanova.kolesa.mvp.view.activities.add_challengeActivity;
import com.example.ainurbayanova.kolesa.mvp.view.activities.add_goalsActivity;
import com.example.ainurbayanova.kolesa.authentications.Login;

import com.example.ainurbayanova.kolesa.mvp.view.fragments.Challenges;
import com.example.ainurbayanova.kolesa.mvp.view.fragments.GoalsFragment;
import com.example.ainurbayanova.kolesa.mvp.view.fragments.ToDoListFragment;
import com.example.ainurbayanova.kolesa.services.CheckChallengeService;
import com.example.ainurbayanova.kolesa.mvp.presenters.MainUserPresenter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainActivity extends AppCompatActivity implements InitializeInterface,UserFirebaseInterface.View {


    Toolbar toolbar;
    FloatingActionButton fab;

    FirebaseUser user;
    User main_user = null;
    DatabaseReference databaseReference;
    CircleImageView circleImageView;
    String usernameOfBundle;
    int nowing = 0;
    String image = "";
    String key = "";
    String username = "";
    String email = "";
    String password = "";
    FloatingActionButton fabs;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            switch (item.getItemId()) {
                case R.id.nav_users:
                    getSupportFragmentManager().beginTransaction().replace(R.id.content, new Challenges()).commit();
                    getSupportActionBar().setTitle("Challenges");
                    return true;
                case R.id.nav_widgets:
                    getSupportFragmentManager().beginTransaction().replace(R.id.content, new GoalsFragment()).commit();
                    getSupportActionBar().setTitle("Goals");
                    return true;
                case R.id.ToDoList:
                    getSupportFragmentManager().beginTransaction().replace(R.id.content, new ToDoListFragment()).commit();
                    getSupportActionBar().setTitle("To do list");
                    return true;
                case R.id.AboutUs:
                    startActivity(new Intent(MainActivity.this,aboutUsActivity.class));
                    return true;
            }
            return false;
        }
    };
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FirebaseApp.initializeApp(this);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        setContentView(R.layout.activity_main);

        initPresenter();

        userSetDisplay();

        initializeToolbar();

        if(FirebaseAuth.getInstance().getCurrentUser() == null){
            finish();
        }
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        getSupportFragmentManager().beginTransaction().replace(R.id.content, new Challenges()).commit();
        getSupportActionBar().setTitle("Challenges");

    }

    public void initPresenter(){
        MainUserPresenter mainUserPresenter = new MainUserPresenter(this);
        mainUserPresenter.requestFromFirbase(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
    }

    @Override
    protected void onStart() {
        super.onStart();
        startService(new Intent(this, CheckChallengeService.class));
    }

    public void userSetDisplay(){
        user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null){
            String username = user.getDisplayName();
            if(username == null){
                Bundle bundle = getIntent().getExtras();
                if(bundle != null){
                    usernameOfBundle = bundle.getString("user");
                    userProfile(bundle.getString("user"));
                }
            }
            if(user == null){
                finish();
            }
        }
    }

    public void userProfile(String usernames){
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setPhotoUri(Uri.parse("https://i.pinimg.com/736x/0b/6e/90/0b6e90105524663fee93fb9bb7f0c860.jpg"))
                .setDisplayName(usernames)
                .build();
        user.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(MainActivity.this,"User Updated",Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(MainActivity.this,"User Failure",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initializeToolbar() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setElevation(0);
    }

    private void initClickListener() {
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,add_challengeActivity.class));
            }
        });

        fabs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,add_goalsActivity.class));
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.exit:
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case DialogInterface.BUTTON_POSITIVE:
                                FirebaseAuth.getInstance().signOut();
                                startActivity(new Intent(MainActivity.this, Login.class));
                                finish();
                                dialog.dismiss();
                                break;
                            case DialogInterface.BUTTON_NEGATIVE:
                                dialog.dismiss();
                                break;
                        }
                    }
                };
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setMessage("Are your sure").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();
                break;
            case R.id.add:
                FragmentManager fragmentManager = getSupportFragmentManager();
                Fragment currentFragment = fragmentManager.findFragmentById(R.id.content);
                if(currentFragment !=  null && (currentFragment instanceof Challenges)){
                    startActivity(new Intent(MainActivity.this,add_challengeActivity.class));
                }
                else if(currentFragment !=  null && (currentFragment instanceof GoalsFragment)){
                    startActivity(new Intent(MainActivity.this,add_goalsActivity.class));
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void removeItems(ArrayList<Challenge> challenges,int position,int total1,int total2,String timeOfEnd) {
        final int challengePoint = challenges.get(position).getPoints();

        for (UserForChallenge user:challenges.get(position).getUsers()){
            if(!user.isDone()){

                String fKey = databaseReference.child("challenges_done").child(user.getfKey()).push().getKey();

                String all = (total2 - total1) + timeOfEnd;

                Date date = new Date(getYear(),getMonth(),getDay());

                Time time = new Time(getHour(),getMinute());

                Challenges_done challenges_done = new Challenges_done(-challenges.get(position).getPoints(),
                        user.getfKey(),
                        challenges.get(position),all,date,time);

                challenges_done.setfKey(fKey);

                databaseReference.child("challenges_done").child(user.getfKey()).child(fKey).setValue(challenges_done);

                databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for (DataSnapshot data:dataSnapshot.getChildren()){
                            int subsUserPoint = Integer.parseInt(data.child("points").getValue().toString()) - challengePoint;
                            databaseReference.child("users").child(data.getKey()).child("points").setValue(subsUserPoint);
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        }
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

    public int getHour() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat dateformat = new SimpleDateFormat("H");
        int day = Integer.parseInt(dateformat.format(c.getTime()));
        return day;
    }

    public int getMinute(){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat dateformat = new SimpleDateFormat("m");
        int day = Integer.parseInt(dateformat.format(c.getTime()));
        return day;
    }

    public int getYear(){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat dateformat = new SimpleDateFormat("Y");
        int day = Integer.parseInt(dateformat.format(c.getTime()));
        return day;
    }

    @Override
    public void removeChallenge(Challenge challenges) {
        databaseReference.child("challenges").child(challenges.getKey()).removeValue();
    }

    @Override
    public void initializeAdmin(final TextView adminUsername, final CircleImageView adminImage, final String challengeKey) {
        databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = null;
                for (DataSnapshot data:dataSnapshot.getChildren()){
                    if(data.child("key").getValue().toString()
                            .equals(challengeKey)){
                        user = new User(data.child("username").getValue().toString(),
                                data.child("image").getValue().toString());
                    }
                }
                if(user != null){
                    adminUsername.setText(user.getUsername());
                    Glide.with(MainActivity.this)
                            .load(Uri.parse(user.getImage()))
                            .into(adminImage);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void showProgress() {

    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void setData(User user) {
        main_user = user;
    }
}
