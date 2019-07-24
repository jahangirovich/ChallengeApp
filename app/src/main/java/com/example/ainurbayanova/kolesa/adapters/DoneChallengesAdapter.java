package com.example.ainurbayanova.kolesa.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.ainurbayanova.kolesa.R;
import com.example.ainurbayanova.kolesa.mvp.modules.Challenges_done;

import java.util.ArrayList;

public class DoneChallengesAdapter extends RecyclerView.Adapter<DoneChallengesAdapter.ViewHolder> {
    ArrayList<Challenges_done> feedbacks;
    Context mContext;

    public DoneChallengesAdapter(Context mContext,ArrayList<Challenges_done> feedbacks){
        this.mContext = mContext;
        this.feedbacks = feedbacks;

    }

    @Override
    public DoneChallengesAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView;
        itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.design_of_done_challenges, viewGroup, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final DoneChallengesAdapter.ViewHolder  viewHolder, final int i) {
        viewHolder.nameOf.setText(feedbacks.get(i).getChallenge().getName());

        int point = feedbacks.get(i).getChallenge().getPoints();
        if(feedbacks.get(i).getTime().getHour() < 13){
            viewHolder.time.setText("at " + feedbacks.get(i).getTime().getHour() + ":" + feedbacks.get(i).getTime().getMinute() + " am");
        }
        else{
            viewHolder.time.setText("at " + feedbacks.get(i).getTime().getHour() + ":" + feedbacks.get(i).getTime().getMinute() + " pm");
        }
        if(point > 0){
            viewHolder.imageView.setBackground(mContext.getDrawable(R.drawable.red_circle_for_layout));
            viewHolder.point.setText("-"+feedbacks.get(i).getChallenge().getPoints() + "");
        }
        else{
            viewHolder.imageView.setBackground(mContext.getDrawable(R.drawable.green_circle_for_layout));
            viewHolder.point.setText("+"+feedbacks.get(i).getPoint() + "");
        }
    }

    @Override
    public int getItemCount() {
        return feedbacks.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView point;
        RelativeLayout imageView;
        TextView time;
        TextView nameOf;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            point = itemView.findViewById(R.id.points);
            imageView = itemView.findViewById(R.id.image);
            nameOf = itemView.findViewById(R.id.nameOfChallenge);
            time = itemView.findViewById(R.id.time);
        }
    }
}