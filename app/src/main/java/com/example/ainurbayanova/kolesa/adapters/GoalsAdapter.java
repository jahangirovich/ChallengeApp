package com.example.ainurbayanova.kolesa.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.ainurbayanova.kolesa.R;
import com.example.ainurbayanova.kolesa.mvp.view.interfaces.DetailOfChallenges;
import com.example.ainurbayanova.kolesa.mvp.modules.Goal;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class GoalsAdapter extends RecyclerView.Adapter<GoalsAdapter.ViewHolder> {
    ArrayList<Goal> goals = new ArrayList<>();
    Context mContext;
    DetailOfChallenges addInterface;
    boolean checkIsDone = false;

    public GoalsAdapter(Context mContext,ArrayList<Goal> goals){
        this.mContext = mContext;
        this.goals = goals;
    }

    public void setOnAddClickListener(DetailOfChallenges addInterface){
        this.addInterface = addInterface;
    }

    @Override
    public GoalsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView;
        itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.design_of_goals, viewGroup, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull GoalsAdapter.ViewHolder viewHolder, int i) {
        viewHolder.title.setText(goals.get(i).getTitle());
        int[] hi = new int[]{mContext.getResources().getColor(R.color.green),
                mContext.getResources().getColor(R.color.bronze),
                mContext.getResources().getColor(R.color.fiolet),
                mContext.getResources().getColor(R.color.orange),
                mContext.getResources().getColor(R.color.yellow),
                mContext.getResources().getColor(R.color.blue)};
        String[] month = new String[]{"JAN","FEB","MARCH","APRIL","MAY","JUNE","JULY","AUG","SEP","OCT","DEC"};
        viewHolder.leftLine.setBackgroundColor(hi[goals.get(i).getColor()]);
        viewHolder.motivation.setText(goals.get(i).getMotivation());
        viewHolder.time.setText(goals.get(i).getTime().getHour() + ":" + goals.get(i).getTime().getMinute() + " PM");
        viewHolder.date.setText(goals.get(i).getDate().getDay() + " " + month[goals.get(i).getDate().getMonth()-1] + " " + goals.get(i).getDate().getYear());
        setBackground(viewHolder,i);
    }

    public void setBackground(@NonNull GoalsAdapter.ViewHolder viewHolder,int position){

        int now_month = getMonth();
        int now_day = getDay();
        int now_hour = getHour();
        int now_minute = getMinute();

        int user_month = goals.get(position).getDate().getMonth();
        int user_day = goals.get(position).getDate().getDay();
        int user_hour = goals.get(position).getTime().getHour();
        int user_minute = goals.get(position).getTime().getMinute();

        int total1 = (user_hour * 60) + user_minute;
        int total2 = (now_hour * 60) + now_minute;

        if (user_month == now_month && now_day == user_day) {
            if(total1 >= total2){
                viewHolder.linearLayout.setBackgroundColor(mContext.getResources().getColor(R.color.white));
                viewHolder.leftLine.setBackgroundColor(mContext.getResources().getColor(R.color.green));
                viewHolder.date.setTextColor(mContext.getResources().getColor(R.color.green));
                viewHolder.time.setTextColor(mContext.getResources().getColor(R.color.green));
                viewHolder.motivation.setTextColor(mContext.getResources().getColor(R.color.green));
                viewHolder.title.setTextColor(mContext.getResources().getColor(R.color.green));
            }
            else{
                viewHolder.linearLayout.setBackgroundColor(mContext.getResources().getColor(R.color.white));
                viewHolder.leftLine.setBackgroundColor(mContext.getResources().getColor(R.color.red));
                viewHolder.date.setTextColor(mContext.getResources().getColor(R.color.red));
                viewHolder.time.setTextColor(mContext.getResources().getColor(R.color.red));
                viewHolder.motivation.setTextColor(mContext.getResources().getColor(R.color.red));
                viewHolder.title.setTextColor(mContext.getResources().getColor(R.color.red));
            }
        }
        else if (user_month < now_month) {

            viewHolder.linearLayout.setBackgroundColor(mContext.getResources().getColor(R.color.white));
            viewHolder.leftLine.setBackgroundColor(mContext.getResources().getColor(R.color.red));
            viewHolder.date.setTextColor(mContext.getResources().getColor(R.color.red));
            viewHolder.time.setTextColor(mContext.getResources().getColor(R.color.red));
            viewHolder.motivation.setTextColor(mContext.getResources().getColor(R.color.red));
            viewHolder.title.setTextColor(mContext.getResources().getColor(R.color.red));
        }
        else if(user_month == now_month &&  now_day > user_day){

            viewHolder.linearLayout.setBackgroundColor(mContext.getResources().getColor(R.color.white));
            viewHolder.leftLine.setBackgroundColor(mContext.getResources().getColor(R.color.red));
            viewHolder.date.setTextColor(mContext.getResources().getColor(R.color.red));
            viewHolder.time.setTextColor(mContext.getResources().getColor(R.color.red));
            viewHolder.motivation.setTextColor(mContext.getResources().getColor(R.color.red));
            viewHolder.title.setTextColor(mContext.getResources().getColor(R.color.red));
        }

        if(!goals.get(position).isActive()){
            viewHolder.linearLayout.setBackgroundColor(mContext.getResources().getColor(R.color.white));
            viewHolder.leftLine.setBackgroundColor(mContext.getResources().getColor(R.color.gray));
            viewHolder.date.setTextColor(mContext.getResources().getColor(R.color.gray));
            viewHolder.time.setTextColor(mContext.getResources().getColor(R.color.gray));
            viewHolder.motivation.setTextColor(mContext.getResources().getColor(R.color.gray));
            viewHolder.title.setTextColor(mContext.getResources().getColor(R.color.gray));
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

    @Override
    public int getItemCount() {
        return goals.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView title;
        TextView motivation;
        TextView date;
        TextView time;
        LinearLayout linearLayout;
        RelativeLayout leftLine;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            linearLayout = itemView.findViewById(R.id.linearForClick);
            leftLine = itemView.findViewById(R.id.leftLine);
            motivation = itemView.findViewById(R.id.motivation);
            date = itemView.findViewById(R.id.date);
            time = itemView.findViewById(R.id.time);
            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addInterface.ClickI(getPosition());
                }
            });
        }
    }
}