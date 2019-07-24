package com.example.ainurbayanova.kolesa.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ainurbayanova.kolesa.R;
import com.example.ainurbayanova.kolesa.mvp.view.interfaces.UserFeedBackInterface;
import com.example.ainurbayanova.kolesa.mvp.modules.FeedBack;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class FeedBackAdapter extends RecyclerView.Adapter<FeedBackAdapter.ViewHolder> {
    ArrayList<FeedBack> feedbacks = new ArrayList<>();
    Context mContext;
    DatabaseReference databaseReference;
    UserFeedBackInterface userFeedInterface;

    public FeedBackAdapter(Context mContext,ArrayList<FeedBack> feedbacks){
        this.mContext = mContext;
        this.feedbacks = feedbacks;
        databaseReference = FirebaseDatabase.getInstance().getReference().child("users");
        userFeedInterface = (UserFeedBackInterface) mContext;
    }


    @Override
    public FeedBackAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView;
        itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.design_feedback, viewGroup, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final FeedBackAdapter.ViewHolder viewHolder, final int i) {
        userFeedInterface.initUser(viewHolder.username,viewHolder.circleImageView,feedbacks.get(i));
        viewHolder.textView.setText(feedbacks.get(i).getText());
    }

    @Override
    public int getItemCount() {
        return feedbacks.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView username;
        CircleImageView circleImageView;
        TextView textView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            username = itemView.findViewById(R.id.username);
            circleImageView = itemView.findViewById(R.id.circleImage);
            textView = itemView.findViewById(R.id.text);
        }
    }
}
