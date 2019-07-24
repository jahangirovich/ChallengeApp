package com.example.ainurbayanova.kolesa.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.ainurbayanova.kolesa.R;
import com.example.ainurbayanova.kolesa.mvp.view.interfaces.AddMembers;
import com.example.ainurbayanova.kolesa.mvp.modules.User;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class MemberAdapter extends RecyclerView.Adapter<MemberAdapter.ViewHolder> {
    ArrayList<User> users = new ArrayList<>();
    Context mContext;
    AddMembers addInterface;

    public MemberAdapter(Context mContext,ArrayList<User> users){
        this.mContext = mContext;
        this.users = users;
    }

    public void setOnAddClickListener(AddMembers addInterface){
        this.addInterface = addInterface;
    }

    @Override
    public MemberAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView;
        itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.design_user_member, viewGroup, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MemberAdapter.ViewHolder viewHolder, int i) {
        viewHolder.name.setText(users.get(i).getUsername());
        Glide.with(mContext)
                .load(Uri.parse(users.get(i).getImage()))
                .into(viewHolder.circleImageView);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        CircleImageView circleImageView;
        LinearLayout addMemberLayout;
        LinearLayout cancelLayout;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.username);
            circleImageView = itemView.findViewById(R.id.image);
            addMemberLayout = itemView.findViewById(R.id.addLayout);
            cancelLayout = itemView.findViewById(R.id.cancelLayout);

            addMemberLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addInterface.addClick(getPosition());
                    addMemberLayout.setVisibility(View.GONE);
                    cancelLayout.setVisibility(View.VISIBLE);
                }
            });
            cancelLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    addInterface.cancelClick(getPosition());
                    addMemberLayout.setVisibility(View.VISIBLE);
                    cancelLayout.setVisibility(View.GONE);
                }
            });
        }
    }
}