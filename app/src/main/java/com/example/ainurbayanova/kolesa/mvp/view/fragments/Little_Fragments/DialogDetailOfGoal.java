package com.example.ainurbayanova.kolesa.mvp.view.fragments.Little_Fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.ainurbayanova.kolesa.R;
import com.example.ainurbayanova.kolesa.mvp.modules.Date;
import com.example.ainurbayanova.kolesa.mvp.modules.Goal;
import com.example.ainurbayanova.kolesa.mvp.modules.Time;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class DialogDetailOfGoal extends DialogFragment {
    EditText title;
    EditText motivation;
    EditText point;
    EditText time;
    EditText date;
    RelativeLayout relativeLayout;
    Date date_for_add;
    Time time_for_add;
    Goal goal;
    DatabaseReference databaseReference;
    Button saveChanges;
    Switch aSwitch;
    int points = 0;
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        goal = (Goal) getArguments().getSerializable("goal");
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.detail_dialog, null);
        final AlertDialog.Builder adb = new AlertDialog.Builder(getActivity(),R.style.AlertDialogCustom)
                .setView(dialogView);
        final AlertDialog dialog = adb.create();
        title = dialogView.findViewById(R.id.title);
        motivation = dialogView.findViewById(R.id.motivation);
        databaseReference = FirebaseDatabase.getInstance().getReference();

        point = dialogView.findViewById(R.id.point);
        date = dialogView.findViewById(R.id.date);
        time = dialogView.findViewById(R.id.time);
        aSwitch = dialogView.findViewById(R.id.switcher);
        saveChanges = dialogView.findViewById(R.id.saveChanges);
        relativeLayout = dialogView.findViewById(R.id.background);
        points = goal.getPoint();
        int[] colors = new int[]{
                getActivity().getResources().getColor(R.color.green),
                getActivity().getResources().getColor(R.color.bronze),
                getActivity().getResources().getColor(R.color.fiolet),
                getActivity().getResources().getColor(R.color.orange),
                getActivity().getResources().getColor(R.color.yellow),
                getActivity().getResources().getColor(R.color.blue)
        };

        title.setText(goal.getTitle());
        date_for_add = goal.getDate();
        time_for_add = goal.getTime();
        motivation.setText(goal.getMotivation());
        point.setText(goal.getPoint() + " points");
        date.setText(goal.getDate().getMonth() + "/" + goal.getDate().getDay());
        time.setText(goal.getTime().getHour() + ":" + goal.getTime().getMinute());
        aSwitch.setChecked(goal.isActive());
        relativeLayout.setBackgroundColor(colors[goal.getColor()]);
        saveChanges.setBackgroundColor(colors[goal.getColor()]);
        initClickListener(dialog);
        return dialog;
    }

    public void initClickListener(final AlertDialog dialog ){
        point.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initializeNumberDialog();
            }
        });
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
        saveChanges.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                changeGoals(dialog);
            }
        });
    }

    public void initializeTimeDialog() {
        LayoutInflater factory = LayoutInflater.from(getActivity());

        final View timeDialog = factory.inflate(R.layout.dialog_time, null);
        final AlertDialog.Builder alertTimeDialog = new AlertDialog.Builder(getActivity());
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
        LayoutInflater factory = LayoutInflater.from(getActivity());

        final View timeDialog = factory.inflate(R.layout.number_dialog, null);
        final AlertDialog.Builder alertTimeDialog = new AlertDialog.Builder(getActivity());
        final NumberPicker numberPicker = timeDialog.findViewById(R.id.numberPicker);
        numberPicker.setMaxValue(50);
        numberPicker.setMinValue(0);
        alertTimeDialog.create();
        alertTimeDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int number = numberPicker.getValue();

                point.setText(number + " points");
                points = number;
                point.setHintTextColor(getResources().getColor(R.color.green));
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
        LayoutInflater factory = LayoutInflater.from(getActivity());

        final View timeDialog = factory.inflate(R.layout.dialog_date, null);
        final AlertDialog.Builder alertDateDialog = new AlertDialog.Builder(getActivity());
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

    public void changeGoals(final AlertDialog dialog ){
        if(TextUtils.isEmpty(title.getText().toString()) || TextUtils.isEmpty(motivation.getText().toString())){
            Toast.makeText(getActivity(),"Please fill all credentials",Toast.LENGTH_SHORT).show();
        }
        else{
            Goal goaling = new Goal(
                    title.getText().toString(),
                    goal.getUserKey(),
                    motivation.getText().toString(),
                    date_for_add,
                    time_for_add,
                    goal.getColor(),
                    points,
                    goal.getfKey(),
                    aSwitch.isChecked());
            databaseReference.child("Goals").child(goal.getfKey()).setValue(goaling);
            dialog.dismiss();
        }
    }
}
