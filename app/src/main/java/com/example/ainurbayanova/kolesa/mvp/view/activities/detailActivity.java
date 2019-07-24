package com.example.ainurbayanova.kolesa.mvp.view.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.design.widget.BottomSheetDialog;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.ainurbayanova.kolesa.adapters.AddedMembersAdapter;
import com.example.ainurbayanova.kolesa.adapters.FeedBackAdapter;
import com.example.ainurbayanova.kolesa.databases.Alarm;
import com.example.ainurbayanova.kolesa.mvp.view.fragments.Little_Fragments.dialog;
import com.example.ainurbayanova.kolesa.R;
import com.example.ainurbayanova.kolesa.mvp.view.interfaces.UserFeedBackInterface;
import com.example.ainurbayanova.kolesa.mvp.modules.AlarmModule;
import com.example.ainurbayanova.kolesa.mvp.modules.Challenge;
import com.example.ainurbayanova.kolesa.mvp.modules.Challenges_done;
import com.example.ainurbayanova.kolesa.mvp.modules.Date;
import com.example.ainurbayanova.kolesa.mvp.modules.FeedBack;
import com.example.ainurbayanova.kolesa.mvp.modules.Time;
import com.example.ainurbayanova.kolesa.mvp.modules.User;
import com.example.ainurbayanova.kolesa.mvp.modules.UserForChallenge;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.ainurbayanova.kolesa.databases.Alarm.COL_CHALLENGE;
import static com.example.ainurbayanova.kolesa.databases.Alarm.COL_DAY;
import static com.example.ainurbayanova.kolesa.databases.Alarm.COL_HOUR;
import static com.example.ainurbayanova.kolesa.databases.Alarm.COL_MINUTE;
import static com.example.ainurbayanova.kolesa.databases.Alarm.COL_MONTH;
import static com.example.ainurbayanova.kolesa.databases.Alarm.COL_USERNAME;


public class detailActivity extends AppCompatActivity implements dialog.IClicked,UserFeedBackInterface {
    Toolbar toolbar;
    RecyclerView recyclerView;
    AddedMembersAdapter addedMembersAdapter;
    ArrayList<UserForChallenge> users = new ArrayList<>();
    Challenge challenge;
    TextView time;
    TextView date;
    TextView title;
    TextView motivation;
    Switch aSwitch;
    Button saveChanges;
    User admin;
    TextView point;
    DatabaseReference databaseReference;
    private Uri imageUri;
    RecyclerView recyclerViewForFeedback;
    FeedBackAdapter feedBackAdapter;
    ArrayList<FeedBack> feedBacks = new ArrayList<>();
    ArrayList<FeedBack> copy_feedbacks = new ArrayList<>();
    ImageView addFeedback;
    FloatingActionButton fabMore;
    Alarm alarmDatabase;
    String myKey;
    int myPoint;
    LinearLayout thereIsNo;
    boolean isDone = false;
    BottomSheetDialog mBottomSheetDialog;
    ImageView imageView;
    View sheetView;
    ArrayList<AlarmModule> alarmModules = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        initializeToolbar();
        initWidgets();
        getDatas();
        getAll();
        setValues();
        initAdmin();
        initializeFeedbacks();
        initSaveChanges();
    }

    public void getDatas() {
        alarmModules.clear();
        Cursor data = alarmDatabase.getData();
        alarmModules = new ArrayList<>();
        while (data.moveToNext()) {
            alarmModules.add(new AlarmModule(data.getInt(data.getColumnIndex(COL_MONTH)),
                    data.getInt(data.getColumnIndex(COL_DAY)),
                    data.getInt(data.getColumnIndex(COL_HOUR)),
                    data.getInt(data.getColumnIndex(COL_MINUTE)),
                    data.getString(data.getColumnIndex(COL_USERNAME)),
                    data.getString(data.getColumnIndex(COL_CHALLENGE))));
        }
    }

    public int getDay() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat dateformat = new SimpleDateFormat("d");
        int day = Integer.parseInt(dateformat.format(c.getTime()));
        return day;
    }

    public int getMonth() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat dateformat = new SimpleDateFormat("M");
        int month = Integer.parseInt(dateformat.format(c.getTime()));
        return month;
    }

    private void initAdmin() {
        admin = new User(FirebaseAuth.getInstance().getCurrentUser().getDisplayName(),
                FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl().toString());
    }

    public boolean isAdmin() {
        if (challenge.getAdmin().getKey().equals(myKey)) {
            return true;
        } else {
            return false;
        }
    }

    private void initializeToolbar() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("detailActivity");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.detail_menu, menu);
        MenuItem item = menu.findItem(R.id.delete);
        MenuItem item_done = menu.findItem(R.id.Done);
        if (!challenge.getAdmin().getKey().equals(myKey)) {
            item.setVisible(false);
        }
        if (isDone) {
            item_done.setVisible(false);
        }
        return super.onCreateOptionsMenu(menu);
    }

    public void initRecycler(ArrayList<User> users) {
        recyclerView = findViewById(R.id.recyclerView);
        addedMembersAdapter = new AddedMembersAdapter(this, users);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,
                LinearLayoutManager.HORIZONTAL,
                false);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(addedMembersAdapter);
    }

    public void getAll() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            challenge = (Challenge) bundle.getSerializable("challenge");
            myKey = bundle.getString("key");
            isDone = bundle.getBoolean("done");
            myPoint = bundle.getInt("myPoint");
        }
    }

    public int getHour() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat dateformat = new SimpleDateFormat("H");
        int day = Integer.parseInt(dateformat.format(c.getTime()));
        return day;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.delete) {
            dialogShow();
        } else if (item.getItemId() == R.id.Done) {
            dialogShowForDone();
        }
        else if(item.getItemId() == R.id.Share){
            dialogShare();
        }
        else if(item.getItemId() == R.id.settings){
            mBottomSheetDialog = new BottomSheetDialog(this);
            sheetView = getLayoutInflater().inflate(R.layout.bottom_sheet, null);
            mBottomSheetDialog.setContentView(sheetView);
            mBottomSheetDialog.show();
        }
        return super.onOptionsItemSelected(item);
    }

    public void dialogShare(){
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        imageUri = Uri.parse("https://i.pinimg.com/736x/0b/6e/90/0b6e90105524663fee93fb9bb7f0c860.jpg");
        String shareBodyText = "Check it out. We have very interesting challenge " + challenge.getName() +  ".\n" +
                " You can join to us.";

        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT,"Subject here");
        sharingIntent.putExtra(android.content.Intent.EXTRA_STREAM, imageUri);

        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBodyText);

        startActivity(Intent.createChooser(sharingIntent, "Shearing Option"));
    }

    public void dialogShow() {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        databaseReference.child("challenges").child(challenge.getKey()).removeValue();
                        Toast.makeText(detailActivity.this, challenge.getName() + " removed", Toast.LENGTH_SHORT).show();
                        finish();
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        Toast.makeText(detailActivity.this, "Canceled", Toast.LENGTH_SHORT).show();
                        break;
                }
            }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Are you sure?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }

    public void dialogShowForDone() {
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case DialogInterface.BUTTON_POSITIVE:
                        uploadToChallengesDone();
                        Toast.makeText(detailActivity.this, challenge.getName() + " Done!", Toast.LENGTH_SHORT).show();
                        finish();
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:

                        break;
                }
            }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Have you done?").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }

//    public void initForAdmin(){
//
//        databaseReference.child("users").addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//                for(DataSnapshot data:dataSnapshot.getChildren()){
//                    for (UserForChallenge key:challenge.getUsers()){
//                        if(data.child("username").getValue().toString().equals(key.getUsername())){
//
//                        }
//                    }
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//        uploadToChallengesDone(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
//        databaseReference.child("challenges").child(challenge.getKey()).removeValue();
//    }

    public void uploadToChallengesDone() {
        String fKey = databaseReference.child("challenges_done").push().getKey();
        Date date = new Date(getYear(),getMonth(),getDay());

        Time time = new Time(getHour(),getMinute());
        String now = getMonth() + "/" + getDay() + "/" + getHour() + "/" + getMinute();

        Challenge challenge_for_Done = new Challenge(challenge.getName(), challenge.getMotivation(), challenge.getDate(), challenge.getTime(), challenge.getUsers());
        Challenges_done challenges_done = new Challenges_done(challenge.getPoints(),
                myKey,
                challenge_for_Done, now,date,time);

        challenges_done.setfKey(fKey);
        databaseReference.child("challenges_done").child(myKey).child(fKey).setValue(challenges_done);
        myPoint += challenge.getPoints();
        databaseReference.child("users").child(myKey).child("points").setValue(myPoint);
        databaseReference.child("challenges").child(challenge.getKey()).child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String key = "";
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    if (data.child("fKey").getValue().toString()
                            .equals(myKey)) {
                        key = data.getKey();
                    }
                }
                databaseReference.child("challenges").child(challenge.getKey()).child("users").child(key).child("done").setValue(true);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void initWidgets() {
        time = findViewById(R.id.time);
        date = findViewById(R.id.date);
        title = findViewById(R.id.title);
        motivation = findViewById(R.id.motivationOfChallenge);
        aSwitch = findViewById(R.id.switcher);
        saveChanges = findViewById(R.id.saveChanges);
        point = findViewById(R.id.point);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        recyclerViewForFeedback = findViewById(R.id.recyclerViewForFeedback);
        addFeedback = findViewById(R.id.addFeedBack);
        fabMore = findViewById(R.id.fabMore);
        thereIsNo = findViewById(R.id.thereIsNo);
        alarmDatabase = new Alarm(this);
        imageView = findViewById(R.id.imageView);
    }

    public void setValues() {
        if (setDaysLeft() == -1) {
            time.setText(challenge.getTime().getHour() + ":" + challenge.getTime().getMinute());
            date.setText(challenge.getDate().getDay() + "/" + challenge.getDate().getMonth());
        } else if (setDaysLeft() == 0) {
            time.setText(setTimesLeft() + " HOURS LEFT");
            date.setText("It is today");
            if (setTimesLeft() == 0) {
                time.setText(setMinutesLeft() + " MINUTES LEFT");
                date.setText("0 hours left");
            }
        } else {
            time.setText(challenge.getTime().getHour() + ":" + challenge.getTime().getMinute());
            date.setText(setDaysLeft() + " days left");
        }
        point.setText("+" + challenge.getPoints());
        motivation.setText(challenge.getMotivation());
        aSwitch.setChecked(challenge.getAlarm());
        title.setText(challenge.getName());
        users = challenge.getUsers();
        databaseReference.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<User> new_users = new ArrayList<>();
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    for (UserForChallenge key : users) {
                        if (data.child("key").getValue().toString().equals(key.getfKey())) {
                            User hi = data.getValue(User.class);
                            assert hi != null;
                            hi.setDone(key.isDone());
                            new_users.add(hi);
                        }
                    }
                }
                initRecycler(new_users);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public int setDaysLeft() {
        int now_date = getMonth();
        int now_day = getDay();

        int userMonth = challenge.getDate().getMonth();
        int userDay = challenge.getDate().getDay();

        if (userMonth > now_date) {
            return -1;
        } else if (userMonth == now_date && userDay >= now_day) {
            return userDay - now_day;
        }
        return -2;
    }

    public int setTimesLeft() {
        int countMinute = 0;
        if (getTotalUserMinute() > getTotalNowMinute()) {
            countMinute = (getTotalUserMinute() - getTotalNowMinute()) / 60;
        }
        return countMinute;
    }

    public int setMinutesLeft() {
        return (getTotalUserMinute() - getTotalNowMinute()) % 60;
    }

    public int getTotalNowMinute() {
        int now_hour = getHour();
        int now_minute = getMinute();
        int total_now = (now_hour * 60) + now_minute;
        return total_now;
    }

    public int getTotalUserMinute() {
        int user_hour = challenge.getTime().getHour();
        int user_minute = challenge.getTime().getMinute();
        int total_user = (user_hour * 60) + user_minute;
        return total_user;
    }

    public int getMinute() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat dateformat = new SimpleDateFormat("m");
        int day = Integer.parseInt(dateformat.format(c.getTime()));
        return day;
    }

    public int getYear() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat dateformat = new SimpleDateFormat("Y");
        int day = Integer.parseInt(dateformat.format(c.getTime()));
        return day;
    }

    public void initRecyclerForFeedBack(ArrayList<FeedBack> feedBacks) {
        feedBackAdapter = new FeedBackAdapter(this, feedBacks);
        recyclerViewForFeedback.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewForFeedback.setAdapter(feedBackAdapter);
    }

    public void initializeFeedbacks() {
        addFeedback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog dialogFragment = new dialog();
                dialogFragment.show(getSupportFragmentManager(), "dialog1");
            }
        });
        databaseReference.child("challenges").child(challenge.getKey()).child("feedbacks").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                feedBacks.clear();
                for (DataSnapshot data : dataSnapshot.getChildren()) {
                    FeedBack feedBack = data.getValue(FeedBack.class);
                    feedBacks.add(feedBack);
                }
                if(feedBacks.size() != 0){
                    thereIsNo.setVisibility(View.GONE);
                }
                if (feedBacks.size() > 2) {
                    copy_feedbacks.add(feedBacks.get(0));
                    copy_feedbacks.add(feedBacks.get(1));
                    initRecyclerForFeedBack(copy_feedbacks);
                } else {
                    initRecyclerForFeedBack(feedBacks);
                    fabMore.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void add(String text) {
        String key = databaseReference.child("challenges").child(challenge.getKey()).child("feedbacks").push().getKey();
        FeedBack feedBack = new FeedBack(myKey, text, key);
        databaseReference.child("challenges").child(challenge.getKey()).child("feedbacks").child(key).setValue(feedBack);
    }

    public void initSaveChanges() {
        saveChanges.setVisibility(View.GONE);
        if(isAdmin()){
            aSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    databaseReference.child("challenges").child(challenge.getKey()).child("alarm").setValue(isChecked);
                    if(isChecked){
                        Toast.makeText(detailActivity.this,"Notification switched on!",Toast.LENGTH_SHORT).show();
                    }
                    else{
                        Toast.makeText(detailActivity.this,"Notification switched off!",Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        else{
            aSwitch.setEnabled(false);
        }
    }

    public String getUsername() {
        return FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
    }

    @Override
    public void initUser(final TextView username, final CircleImageView profileImage, final FeedBack feedBack) {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                User user = null;
                for (DataSnapshot data:dataSnapshot.getChildren()){
                    if(data.getKey().equals(feedBack.getUserId())){
                        user = data.getValue(User.class);
                    }
                }
                if(user != null){
                    Glide.with(detailActivity.this).load(Uri.parse(user.getImage()))
                            .into(profileImage);
                    username.setText(user.getUsername());
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

}
