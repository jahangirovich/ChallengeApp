package com.example.ainurbayanova.kolesa.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.ainurbayanova.kolesa.R;
import com.example.ainurbayanova.kolesa.mvp.view.interfaces.DetailOfChallenges;
import com.example.ainurbayanova.kolesa.mvp.modules.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class StatisticAdapter extends RecyclerView.Adapter<StatisticAdapter.ViewHolder> {
    ArrayList<User> userForChallenges = new ArrayList<>();
    Context mContext;
    DetailOfChallenges addInterface;
    boolean checkIsDone = false;
    String keyForUser = "";
    DatabaseReference databaseReference;

    public StatisticAdapter(Context mContext, ArrayList<User> userForChallenges,String keyForUser) {
        this.mContext = mContext;
        this.userForChallenges = userForChallenges;
        databaseReference = FirebaseDatabase.getInstance().getReference();
        this.keyForUser = keyForUser;
    }

    public void setOnAddClickListener(DetailOfChallenges addInterface) {
        this.addInterface = addInterface;
    }

    @Override
    public StatisticAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView;
        itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.design_statistic, viewGroup, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull StatisticAdapter.ViewHolder viewHolder, int i) {
        viewHolder.id.setText(i + 1 + "");
        viewHolder.points.setText(userForChallenges.get(i).getPoints() + "dp");
        viewHolder.username.setText(userForChallenges.get(i).getUsername());
        Glide.with(mContext)
                .load(userForChallenges.get(i).getImage())
                .into(viewHolder.imageProfile);
        if(i == 0){
            viewHolder.rate.setImageDrawable(mContext.getResources().getDrawable(R.drawable.gold));
            viewHolder.points.setTextColor(mContext.getResources().getColor(R.color.light_bronze));
        }
        else if(i == 1){
            viewHolder.rate.setImageDrawable(mContext.getResources().getDrawable(R.drawable.silver));
            viewHolder.points.setTextColor(mContext.getResources().getColor(R.color.main_gray));
        }
        else if(i == 2){
            viewHolder.rate.setImageDrawable(mContext.getResources().getDrawable(R.drawable.bronze));
            viewHolder.points.setTextColor(mContext.getResources().getColor(R.color.bronze));
        }
        if(userForChallenges.get(i).getPoints() < 0){
            viewHolder.points.setTextColor(mContext.getResources().getColor(R.color.red));
        }
    }

    @Override
    public int getItemCount() {
        return userForChallenges.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView id;
        TextView username;
        LinearLayout background;
        ImageView rate;
        LinearLayout userLayout;
        TextView points;
        CircleImageView imageProfile;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            id = itemView.findViewById(R.id.idNumber);
            username = itemView.findViewById(R.id.username);
            points = itemView.findViewById(R.id.point);
            imageProfile = itemView.findViewById(R.id.profileImage);
            background = itemView.findViewById(R.id.background);
            userLayout = itemView.findViewById(R.id.userLayout);
            rate = itemView.findViewById(R.id.rate);
            userLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addInterface.ClickI(getPosition());
                }
            });
        }
    }
}
