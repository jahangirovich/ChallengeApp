package com.example.ainurbayanova.kolesa.mvp.view.activities;

import android.content.DialogInterface;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.ainurbayanova.kolesa.R;
import com.example.ainurbayanova.kolesa.mvp.view.interfaces.UserFirebaseInterface;
import com.example.ainurbayanova.kolesa.mvp.modules.Date;
import com.example.ainurbayanova.kolesa.mvp.modules.Goal;
import com.example.ainurbayanova.kolesa.mvp.modules.Time;
import com.example.ainurbayanova.kolesa.mvp.modules.User;
import com.example.ainurbayanova.kolesa.mvp.presenters.MainUserPresenter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Random;

public class add_goalsActivity extends AppCompatActivity implements UserFirebaseInterface.View{
    EditText nameOfChallenge;
    EditText motivation;
    EditText date;
    EditText time;
    Date date_for_add;
    Time time_for_add;
    TextView now_date;
    TextView now_time;
    TextInputLayout pointOfLayout;
    TextInputLayout startOfLayout;
    TextInputLayout finishOfLayout;
    TextInputLayout nameOfLayout;
    User main_user;
    TextInputLayout motivationOfLayout;
    EditText points;
    Toolbar toolbar;
    DatabaseReference databaseReference;
    MainUserPresenter mainUserPresenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add__goals);
        initToolbar();
        initWidgets();
        setNowDateAndTime();
        initializeClickListener();
    }
    public void initToolbar() {
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Add Goal");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void initWidgets(){
        points = findViewById(R.id.points);
        now_date = findViewById(R.id.now_date);
        now_time = findViewById(R.id.now_time);
        nameOfChallenge = findViewById(R.id.nameOfChallenge);
        motivation = findViewById(R.id.motivationOfChallenge);
        time = findViewById(R.id.time);
        nameOfLayout = findViewById(R.id.nameOfLayout);
        pointOfLayout = findViewById(R.id.pointOfLayout);
        startOfLayout = findViewById(R.id.startOfLayout);
        finishOfLayout = findViewById(R.id.finishOfLayout);
        motivationOfLayout = findViewById(R.id.motivationOfLayout);
        date = findViewById(R.id.date);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        mainUserPresenter = new MainUserPresenter(this);
        mainUserPresenter.requestFromFirbase(FirebaseAuth.getInstance().getCurrentUser().getDisplayName());
    }

    public void setNowDateAndTime(){
        String[] time = new String[]{"JAN","FEB","MAR","APRIL","MAY","JUNE","JULY","AUG","SEP","OCT","NOV","DEC"};
        now_time.setText("Start time:  " + getHour() + ":" + getMinute());
        now_date.setText("Start date:  " + getDay() + " " + time[getMonth() - 1] + " " + getYear());
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

    public int getYear(){
        Calendar c = Calendar.getInstance();
        SimpleDateFormat dateformat = new SimpleDateFormat("Y");
        int month = Integer.parseInt(dateformat.format(c.getTime()));
        return month;
    }

    public void initializeClickListener(){
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
        points.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initializeNumberDialog();
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
                date.setHintTextColor(getResources().getColor(R.color.red));
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

    public int getRandomColor(){
        Random rand = new Random();

        int n = rand.nextInt(6);

        return n;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.add:
                if(checkDateAndTimeCredentials(date_for_add,time_for_add)){
                    startOfLayout.setError("Please fill correctly");
                    finishOfLayout.setError("Please fill correctly");
                    Toast.makeText(add_goalsActivity.this,"Please fill date and time credentials", Snackbar.LENGTH_LONG).show();
                }
                else if(TextUtils.isEmpty(nameOfChallenge.getText().toString())){
                    nameOfLayout.setError("Empty");
                    Toast.makeText(add_goalsActivity.this,"Please fill name",Snackbar.LENGTH_LONG).show();
                }
                else if(TextUtils.isEmpty(motivation.getText().toString())){
                    nameOfLayout.setError("Empty");
                    Toast.makeText(add_goalsActivity.this,"Please fill motivation",Snackbar.LENGTH_LONG).show();
                }
                else if(TextUtils.isEmpty(points.getText().toString())){
                    nameOfLayout.setError("Empty");
                    Toast.makeText(add_goalsActivity.this,"Please fill point",Snackbar.LENGTH_LONG).show();
                    points.setError("Empty");
                }
                else{
                    String fKey = databaseReference.child("Goals").push().getKey();
                    Goal goal = new Goal(
                            nameOfChallenge.getText().toString(),
                            main_user.getKey(),
                            motivation.getText().toString(),
                            date_for_add,
                            time_for_add,
                            getRandomColor(),
                            Integer.parseInt(points.getText().toString()),
                            fKey,
                            true);
                    databaseReference.child("Goals").child(fKey).setValue(goal);
                    finish();
                }
                Toast.makeText(add_goalsActivity.this,nameOfChallenge.getText().toString() + " added",Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
    private boolean checkDateAndTimeCredentials(Date user_date, Time user_time) {
        int now_day = getDay();
        int now_month = getMonth();
        int now_hour = getHour();
        int now_minute = getMinute();

        if(user_date == null || user_time == null){
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
