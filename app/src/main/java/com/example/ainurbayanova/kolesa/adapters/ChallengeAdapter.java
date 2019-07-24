package com.example.ainurbayanova.kolesa.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ainurbayanova.kolesa.R;
import com.example.ainurbayanova.kolesa.mvp.view.interfaces.DetailOfChallenges;
import com.example.ainurbayanova.kolesa.mvp.modules.Challenge;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

public class ChallengeAdapter extends RecyclerView.Adapter<ChallengeAdapter.ViewHolder> {
    ArrayList<Challenge> challenges;
    Context mContext;
    DetailOfChallenges IDetail;
    DatabaseReference databaseReference;


    public ChallengeAdapter(Context mContext, ArrayList<Challenge> challenges) {
        this.mContext = mContext;
        this.challenges = challenges;
        databaseReference = FirebaseDatabase.getInstance().getReference();
//        removeInterface = (InitializeInterface) mContext;
    }

    public void setClickListener(DetailOfChallenges IDetail) {
        this.IDetail = IDetail;
    }

    @Override
    public ChallengeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView;
        itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.design_recycler, viewGroup, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ChallengeAdapter.ViewHolder viewHolder, int i) {
        viewHolder.name.setText(challenges.get(i).getName());
        viewHolder.motive.setText(challenges.get(i).getMotivation());
        String now_hope = challenges.get(i).getAdmin().getKey();
        initForPictureAdmin(viewHolder,i,now_hope);
        viewHolder.challenger.setText("Challengers: " + challenges.get(i).getChallengers());
        setBackgroundColor(viewHolder, i);
    }

    public void initForPictureAdmin(@NonNull final ChallengeAdapter.ViewHolder viewHolder, final int i, final String now_hope){
//        removeInterface.initializeAdmin(viewHolder.admin,viewHolder.profileImage,now_hope);
    }

    public void setBackgroundColor(ChallengeAdapter.ViewHolder viewHolder, int position) {
        int now_month = getMonth();
        int now_day = getDay();
        int now_hour = getHour();
        int now_minute = getMinute();

        int user_month = challenges.get(position).getDate().getMonth();
        int user_day = challenges.get(position).getDate().getDay();
        int user_hour = challenges.get(position).getTime().getHour();
        int user_minute = challenges.get(position).getTime().getMinute();

        int total1 = (user_hour * 60) + user_minute;
        int total2 = (now_hour * 60) + now_minute;

        if (user_month == now_month && now_day == user_day) {
            if(total1 >= total2){
                viewHolder.clickLayout.setBackgroundColor(mContext.getResources().getColor(R.color.little_green));
            }
            else{

                setChallengesDone(total1,total2,position," minute");
                Log.i("info",challenges.get(position).getName());
                removeChallenge(true,position);
                Toast.makeText(mContext,challenges.get(position).getName() + " was removed",Toast.LENGTH_SHORT).show();
            }
        }
        else if (user_month < now_month) {

            setChallengesDone(user_day,now_month,position," month");

            removeChallenge(true,position);
            Toast.makeText(mContext,challenges.get(position).getName() + " was removed",Toast.LENGTH_SHORT).show();
        }
        else if(user_month == now_month &&  now_day > user_day){

            setChallengesDone(user_day,now_day,position," days");

            removeChallenge(true,position);
            Toast.makeText(mContext,challenges.get(position).getName() + " was removed",Toast.LENGTH_SHORT).show();
        }
    }

    public void removeChallenge(boolean isDone,int position){
        if(isDone){
//            removeInterface.removeChallenge(challenges.get(position));
        }
        else{
            return;
        }
    }

    public void setChallengesDone(int total1, int total2, final int position, String timeOfEnd){
//        removeInterface.removeItems(challenges.get(position),position,total1,total2,timeOfEnd);
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

    @Override
    public int getItemCount() {
        return challenges.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView name;
        TextView motive;
        ImageView rotateImage;
        TextView challenger;
        TextView admin;
        CircleImageView profileImage;
        LinearLayout clickLayout;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            initWidgets();
        }

        @Override
        public void onClick(View v) {
            IDetail.ClickI(getPosition());
        }

        public void initWidgets() {
            name = itemView.findViewById(R.id.nameOfChallenge);
            motive = itemView.findViewById(R.id.motivationOfChallenge);
            rotateImage = itemView.findViewById(R.id.rotateImage);
            admin = itemView.findViewById(R.id.admin);
            challenger = itemView.findViewById(R.id.challenger);
            profileImage = itemView.findViewById(R.id.profileImage);
            clickLayout = itemView.findViewById(R.id.clickLayout);
            RotateAnimation rotate = new RotateAnimation(
                    0, 360,
                    Animation.RELATIVE_TO_SELF, 0.5f,
                    Animation.RELATIVE_TO_SELF, 0.5f
            );

            rotate.setDuration(2000);
            rotate.setRepeatCount(Animation.INFINITE);
            rotateImage.startAnimation(rotate);
            clickLayout.setOnClickListener(this);
        }
    }

}
