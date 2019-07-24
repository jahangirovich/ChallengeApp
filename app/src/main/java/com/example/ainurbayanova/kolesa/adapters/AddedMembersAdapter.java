package com.example.ainurbayanova.kolesa.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.ainurbayanova.kolesa.R;
import com.example.ainurbayanova.kolesa.mvp.view.interfaces.DetailOfChallenges;
import com.example.ainurbayanova.kolesa.mvp.modules.User;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class AddedMembersAdapter extends RecyclerView.Adapter<AddedMembersAdapter.ViewHolder> {
    ArrayList<User> users = new ArrayList<>();
    Context mContext;
    DetailOfChallenges detailOfChallenges;

    public AddedMembersAdapter(Context mContext, ArrayList<User> users) {
        this.mContext = mContext;
        this.users = users;
    }

    public void setClickListener(DetailOfChallenges detailOfChallenges){
        this.detailOfChallenges = detailOfChallenges;
    }

    @Override
    public AddedMembersAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView;
        itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.design_linear_member_add, viewGroup, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull AddedMembersAdapter.ViewHolder viewHolder, int i) {
        if(users.get(i) != null){
            viewHolder.username.setText(users.get(i).getUsername());
            Glide.with(mContext)
                    .load(Uri.parse(users.get(i).getImage()))
                    .into(viewHolder.circleImageView);
            if(users.get(i).isDone()){
                viewHolder.username.setTextColor(mContext.getResources().getColor(R.color.green));
            }
        }
    }

    @Override
    public int getItemCount() {
        for (int x = 0;x < users.size();x++){
            if(users.get(x) == null){
                users.remove(x);
            }
        }
        return users.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView username;
        CircleImageView circleImageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.username);
            circleImageView = itemView.findViewById(R.id.image);
        }
    }
}