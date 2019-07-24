package com.example.ainurbayanova.kolesa.mvp.view.activities;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.ainurbayanova.kolesa.adapters.AddedMembersAdapter;
import com.example.ainurbayanova.kolesa.R;
import com.example.ainurbayanova.kolesa.services.ChallengeService;
import com.example.ainurbayanova.kolesa.mvp.modules.Challenge;
import com.example.ainurbayanova.kolesa.mvp.modules.Date;
import com.example.ainurbayanova.kolesa.mvp.modules.Time;
import com.example.ainurbayanova.kolesa.mvp.modules.User;
import com.example.ainurbayanova.kolesa.mvp.modules.UserForChallenge;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class add_challengeActivity extends AppCompatActivity {

    Toolbar toolbar;
    LinearLayout timeLayout;
    LinearLayout dateLayout;
    LinearLayout membersLayout;
    EditText nameOfChallenge;
    EditText motivation;
    EditText date;
    EditText time;
    RecyclerView recyclerView;
    AddedMembersAdapter addedMembersAdapter;
    TextView checkRecycler;
    User user;
    int challengers = 0;
    DatabaseReference databaseReference;
    ArrayList<UserForChallenge> users;
    EditText points;
    FirebaseUser auth;
    Date date_for_add;
    Time time_for_add;
    TextInputLayout nameOfLayout;
    TextInputLayout motivationOfLayout;
    TextView now_date;
    TextView now_time;
    TextInputLayout pointOfLayout;
    TextInputLayout startOfLayout;
    TextInputLayout finishOfLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_);
        initToolbar();
        initWidgets();
        initClickListener();
        initializeRecyclerView();
        setNowDateAndTime();
    }

    public void setNowDateAndTime(){
        String[] time = new String[]{"JAN","FEB","MAR","APRIL","MAY","JUNE","JULY","AUG","SEP","OCT","NOV","DEC"};
        now_time.setText("Start time:  " + getHour() + ":" + getMinute());
        now_date.setText("Start date:  " + getDay() + " " + time[getMonth() - 1] + " " + getYear());
    }

    public int getYear(){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat dateformat = new SimpleDateFormat("Y");
        int month = Integer.parseInt(dateformat.format(c.getTime()));
        return month;
    }

    private void initWidgets() {
        timeLayout = findViewById(R.id.timeLayout);
        dateLayout = findViewById(R.id.dateLayout);
        nameOfChallenge = findViewById(R.id.nameOfChallenge);
        motivation = findViewById(R.id.motivationOfChallenge);
        time = findViewById(R.id.time);
        date = findViewById(R.id.date);
        membersLayout = findViewById(R.id.membersLayout);
        nameOfLayout = findViewById(R.id.nameOfLayout);
        pointOfLayout = findViewById(R.id.pointOfLayout);
        startOfLayout = findViewById(R.id.startOfLayout);
        finishOfLayout = findViewById(R.id.finishOfLayout);
        motivationOfLayout = findViewById(R.id.motivationOfLayout);
        checkRecycler = findViewById(R.id.checkRecycler);
        auth = FirebaseAuth.getInstance().getCurrentUser();
        if(auth != null){
            user = new User(auth.getDisplayName(),auth.getPhotoUrl().toString());
        }
        databaseReference = FirebaseDatabase.getInstance().getReference();
        points = findViewById(R.id.points);
        now_date = findViewById(R.id.now_date);
        now_time = findViewById(R.id.now_time);
    }

    private void initClickListener() {
        time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initializeTimeDialog();
            }
        });
        date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initializeDateDialog();
            }
        });
        membersLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(add_challengeActivity.this, add_membersActivity.class);
                startActivityForResult(intent, 1);
            }
        });
        points.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initializeNumberDialog();
            }
        });

    }

    public void initializeRecyclerView(){
            recyclerView = findViewById(R.id.recyclerView);
            LinearLayoutManager layoutManager
                    = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
            recyclerView.setLayoutManager(layoutManager);

        }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data == null) {
            return;
        }
        users = (ArrayList<UserForChallenge>) data.getSerializableExtra("users");

        final ArrayList<User> all_users = new ArrayList<>();
        databaseReference.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for (DataSnapshot data:dataSnapshot.getChildren()){
                    for (UserForChallenge key:users){
                        if(data.getKey().equals(key.getfKey())){
                            User user = data.getValue(User.class);
                            all_users.add(user);
                        }
                    }
                }
                addedMembersAdapter = new AddedMembersAdapter(add_challengeActivity.this,all_users);
                recyclerView.setAdapter(addedMembersAdapter);
                if(users.size() == 0){
                    checkRecycler.setVisibility(View.VISIBLE);
                }
                else{
                    checkRecycler.setVisibility(View.GONE);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        challengers = users.size();
    }

    public void initToolbar() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Add challenge");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void initializeTimeDialog() {
        LayoutInflater factory = LayoutInflater.from(this);

        final View timeDialog = factory.inflate(R.layout.dialog_time, null);
        final AlertDialog.Builder alertTimeDialog = new AlertDialog.Builder(this);
        final TimePicker timePicker = timeDialog.findViewById(R.id.timePicker);

        alertTimeDialog.create();
        alertTimeDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int hour = timePicker.getCurrentHour();
                int minute = timePicker.getCurrentMinute();
                time_for_add = new Time(hour,minute);
                time.setText(hour + ":" + minute);
                time.setHintTextColor(getResources().getColor(R.color.green));
                dialog.dismiss();
            }
        });
        alertTimeDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertTimeDialog.setView(timeDialog);
        alertTimeDialog.show();
    }

    public void initializeNumberDialog() {
        LayoutInflater factory = LayoutInflater.from(this);

        final View timeDialog = factory.inflate(R.layout.number_dialog, null);
        final AlertDialog.Builder alertTimeDialog = new AlertDialog.Builder(this);
        final NumberPicker numberPicker = timeDialog.findViewById(R.id.numberPicker);
        numberPicker.setMaxValue(50);
        numberPicker.setMinValue(0);
        alertTimeDialog.create();
        alertTimeDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int number = numberPicker.getValue();

                points.setText(number + "");
                points.setHintTextColor(getResources().getColor(R.color.green));
                dialog.dismiss();
            }
        });
        alertTimeDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertTimeDialog.setView(timeDialog);
        alertTimeDialog.show();
    }

    public void initializeDateDialog() {
        LayoutInflater factory = LayoutInflater.from(this);

        final View timeDialog = factory.inflate(R.layout.dialog_date, null);
        final AlertDialog.Builder alertDateDialog = new AlertDialog.Builder(this);
        final DatePicker datePicker = timeDialog.findViewById(R.id.datePicker);

        alertDateDialog.create();
        alertDateDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int month = datePicker.getMonth() + 1;
                int day = datePicker.getDayOfMonth();
                int year = datePicker.getYear();

                date_for_add = new Date(year,month,day);
                date.setText(month + "/" + day);
                date.setHintTextColor(getResources().getColor(R.color.green));
                dialog.dismiss();
            }
        });
        alertDateDialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertDateDialog.setView(timeDialog);
        alertDateDialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.add:
                final ProgressDialog dialog = new ProgressDialog(add_challengeActivity.this);
                dialog.setMessage("Posting...");
                dialog.show();

                final String name = nameOfChallenge.getText().toString();
                final String motiv = motivation.getText().toString();
                String point = points.getText().toString();
                final String key = databaseReference.child("challenges").push().getKey();

                if(TextUtils.isEmpty(name)){
                    Toast.makeText(this,"Please fill name",Snackbar.LENGTH_LONG).show();
                    dialog.dismiss();
                    nameOfLayout.setError("Empty");
                }
                else if(checkDateAndTimeCredentials(date_for_add,time_for_add)){
                    Toast.makeText(this,"Please fill correct date",Snackbar.LENGTH_LONG).show();
                    dialog.dismiss();
                    startOfLayout.setError("Please fill correctly");
                    finishOfLayout.setError("Please fill correctly");
                }
                else if(TextUtils.isEmpty(motiv)){
                    Toast.makeText(this,"Please fill motivation",Snackbar.LENGTH_LONG).show();
                    dialog.dismiss();
                    motivationOfLayout.setError("Empty");
                }
                else if(TextUtils.isEmpty(point)){
                    Toast.makeText(this,"Please fill point",Snackbar.LENGTH_LONG).show();
                    pointOfLayout.setError("Empty");
                    dialog.dismiss();
                }
                else if(challengers == 0){
                    Toast.makeText(this,"Please fill challengers",Snackbar.LENGTH_LONG).show();
                    dialog.dismiss();
                }
                else{
                    databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            String userKey = "";
                            for (DataSnapshot data:dataSnapshot.getChildren()){
                                if(data.child("username").getValue().toString().equals(user.getUsername())){
                                    userKey = data.child("key").getValue().toString();
                                }
                            }
                            users.add(new UserForChallenge(userKey,true));

                            user = new User(userKey);

                            Challenge challenge = new Challenge(user,challengers + 1,
                                    name,motiv,date_for_add,time_for_add,key,
                                    users,true,Integer.parseInt(points.getText()+ ""));

                            databaseReference.child("challenges").child(key).setValue(challenge);

                            dialog.dismiss();
                            startService(new Intent(add_challengeActivity.this,ChallengeService.class));
                            finish();
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public int getDay(){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat dateformat = new SimpleDateFormat("d");
        int day = Integer.parseInt(dateformat.format(c.getTime()));
        return day;
    }

    public int getHour(){
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

    public int getMonth(){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat dateformat = new SimpleDateFormat("M");
        int month = Integer.parseInt(dateformat.format(c.getTime()));
        return month;
    }

    private boolean checkDateAndTimeCredentials(Date user_date, Time user_time) {
        int now_day = getDay();
        int now_month = getMonth();
        int now_hour = getHour();
        int now_minute = getMinute();

        if(user_date == null){
            return true;
        }

        int user_month = user_date.getMonth();
        int user_day = user_date.getDay();
        int user_hour = user_time.getHour();
        int user_minute = user_time.getMinute();

        return (user_month <= now_month && now_day > user_day) ||
                (user_month == now_month && now_day == user_day && user_hour < now_hour) ||
                (user_month == now_month && now_day == user_day && user_hour == now_hour && now_minute > user_minute);

    }

}
