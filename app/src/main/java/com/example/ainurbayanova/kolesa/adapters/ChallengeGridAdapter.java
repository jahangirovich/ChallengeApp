package com.example.ainurbayanova.kolesa.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ainurbayanova.kolesa.R;
import com.example.ainurbayanova.kolesa.mvp.view.interfaces.DetailOfChallenges;
import com.example.ainurbayanova.kolesa.mvp.view.interfaces.InitializeInterface;
import com.example.ainurbayanova.kolesa.mvp.modules.Challenge;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class ChallengeGridAdapter extends BaseAdapter{
    private ArrayList<Challenge> challenges;
    private LayoutInflater layoutInflater;
    InitializeInterface removeInterface;
    DetailOfChallenges detailOfChallenges;
    private Context context;

    public ChallengeGridAdapter(Context aContext,  ArrayList<Challenge> challenges) {
        this.context = aContext;
        this.challenges = challenges;
        layoutInflater = LayoutInflater.from(aContext);
        removeInterface = (InitializeInterface) context;
    }


    @Override
    public int getCount() {
        return challenges.size();
    }

    @Override
    public Object getItem(int position) {
        return challenges.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void setClickListener(DetailOfChallenges detailOfChallenges){
        this.detailOfChallenges = detailOfChallenges;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ChallengeGridAdapter.ViewHolder holder;
        if(convertView == null){
            convertView = layoutInflater.inflate(R.layout.design_of_grid,null);
            holder = new ChallengeGridAdapter.ViewHolder();
            holder.name = convertView.findViewById(R.id.title);
            holder.point = convertView.findViewById(R.id.point);
            holder.clickLayout = convertView.findViewById(R.id.clickLayout);
            convertView.setTag(holder);
            setBackgroundColor(position);
        }
        else{
            holder = (ChallengeGridAdapter.ViewHolder) convertView.getTag();
        }
        if(challenges.get(position).getName().length() > 15){
            String cut = challenges.get(position).getName().substring(0,15);
            holder.name.setText(cut + "...");

        }
        else{
            holder.name.setText(challenges.get(position).getName());
        }
        holder.point.setText(challenges.get(position).getPoints() + "");
        holder.clickLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                detailOfChallenges.ClickI(position);
            }
        });
        return convertView;
    }

    public void setBackgroundColor( int position) {
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

            }
            else{

                removeInterface.removeItems(challenges,position,total1,total2," minute");
                removeChallenge(true,position);
                Toast.makeText(context,challenges.get(position).getName() + " was removed",Toast.LENGTH_SHORT).show();
            }
        }
        else if (user_month < now_month) {

            removeInterface.removeItems(challenges,position,total1,total2," month");

            removeChallenge(true,position);
            Toast.makeText(context,challenges.get(position).getName() + " was removed",Toast.LENGTH_SHORT).show();
        }
        else if(user_month == now_month &&  now_day > user_day){
            removeInterface.removeItems(challenges,position,total1,total2," days");
            removeChallenge(true,position);
            Toast.makeText(context,challenges.get(position).getName() + " was removed",Toast.LENGTH_SHORT).show();
        }
    }


    public void removeChallenge(boolean isDone,int position){
        if(isDone){
            removeInterface.removeChallenge(challenges.get(position));
        }
        else{
            return;
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

    static class ViewHolder {
        TextView name;
        TextView point;
        LinearLayout clickLayout;
    }
}
