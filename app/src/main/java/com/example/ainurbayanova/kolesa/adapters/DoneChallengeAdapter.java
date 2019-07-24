package com.example.ainurbayanova.kolesa.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.ainurbayanova.kolesa.R;
import com.example.ainurbayanova.kolesa.mvp.view.interfaces.DetailOfChallenges;
import com.example.ainurbayanova.kolesa.mvp.modules.Challenges_done;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class DoneChallengeAdapter extends RecyclerView.Adapter<DoneChallengeAdapter.ViewHolder> {
    ArrayList<Challenges_done> challenges_dones = new ArrayList<>();
    Context mContext;
    DetailOfChallenges IDetail;

    public DoneChallengeAdapter(Context mContext, ArrayList<Challenges_done> challenges_dones) {
        this.mContext = mContext;
        this.challenges_dones = challenges_dones;
    }

    public void setOnLongClickListener(DetailOfChallenges IDetail) {
        this.IDetail = IDetail;
    }

    @Override
    public DoneChallengeAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView;
        itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.design_of_done_fragment, viewGroup, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull DoneChallengeAdapter.ViewHolder viewHolder, int i) {
        Challenges_done myChallenge = challenges_dones.get(i);

        viewHolder.nameOfChallenge.setText(myChallenge.getChallenge().getName() + "");

        if (myChallenge.point < 0) {
            viewHolder.points.setText("Points: " + myChallenge.getPoint() + "");
            viewHolder.points.setTextColor(mContext.getResources().getColor(R.color.red));
            viewHolder.verified.setImageResource(R.drawable.close);
            viewHolder.time.setText("Times up");
            viewHolder.time.setTextColor(mContext.getResources().getColor(R.color.red));
            viewHolder.date.setText(myChallenge.getDays() + " later.");
            viewHolder.date.setTextColor(mContext.getResources().getColor(R.color.red));
        } else {
            viewHolder.points.setText("Points: +" + myChallenge.getPoint() + "");
            viewHolder.points.setTextColor(mContext.getResources().getColor(R.color.green));
            viewHolder.verified.setImageResource(R.drawable.verified);
            setAllDateAndTime(myChallenge, viewHolder);
            viewHolder.date.setTextColor(mContext.getResources().getColor(R.color.green));
            viewHolder.time.setText("");
        }
    }

    public void setAllDateAndTime(Challenges_done myChallenge, @NonNull DoneChallengeAdapter.ViewHolder viewHolder) {
        String[] splittedTime = myChallenge.days.split("/");

        int deadline_month = myChallenge.challenge.getDate().getMonth();
        int deadline_day = myChallenge.challenge.getDate().getDay();
        int deadline_hour = myChallenge.challenge.getTime().getHour();
        int deadline_minute = myChallenge.challenge.getTime().getMinute();

        int finished_month = Integer.parseInt(splittedTime[0]);
        int finished_day = Integer.parseInt(splittedTime[1]);
        int finished_hour = Integer.parseInt(splittedTime[2]);
        int finished_minute = Integer.parseInt(splittedTime[3]);

        if (deadline_month == finished_month) {
            if (finished_day < deadline_day) {
                int total = deadline_day - finished_day;
                viewHolder.date.setText(total + " days early done");
            } else if (finished_day == deadline_day) {
                if (deadline_hour > finished_hour) {
                    int total = deadline_hour - finished_hour;
                    viewHolder.date.setText(total + " hours early done");
                } else if (deadline_hour == finished_hour) {
                    if (deadline_minute > finished_minute) {
                        int total = deadline_minute - finished_minute;
                        viewHolder.date.setText(total + " minutes early done");
                    }
                }
            }
        } else {
            int total = deadline_month - finished_month;
            viewHolder.date.setText(total + " month early done");
        }
    }

    @Override
    public int getItemCount() {
        return challenges_dones.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView nameOfChallenge;
        CircleImageView verified;
        TextView date;
        LinearLayout longLinearPressing;
        TextView time;
        TextView points;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            nameOfChallenge = itemView.findViewById(R.id.nameOfChallenge);
            verified = itemView.findViewById(R.id.verified);
            date = itemView.findViewById(R.id.date);
            time = itemView.findViewById(R.id.time);
            points = itemView.findViewById(R.id.points);
            longLinearPressing = itemView.findViewById(R.id.longLinearPressing);
            longLinearPressing.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    IDetail.ClickI(getPosition());
                }
            });
        }
    }
}