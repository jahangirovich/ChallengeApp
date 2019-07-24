package com.example.ainurbayanova.kolesa.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ainurbayanova.kolesa.R;
import com.example.ainurbayanova.kolesa.mvp.view.interfaces.AddMembers;

import java.util.ArrayList;

public class MenuAdapter extends RecyclerView.Adapter<MenuAdapter.ViewHolder> {
    ArrayList<String> titles = new ArrayList<>();
    Context mContext;
    AddMembers addInterface;

    public MenuAdapter(Context mContext,ArrayList<String> titles){
        this.mContext = mContext;
        this.titles = titles;
    }

    public void setOnAddClickListener(AddMembers addInterface){
        this.addInterface = addInterface;
    }

    @Override
    public MenuAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView;
        itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.design_of_menu, viewGroup, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuAdapter.ViewHolder viewHolder, int i) {
        viewHolder.name.setText(titles.get(i));
    }

    @Override
    public int getItemCount() {
        return titles.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView name;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.title);
        }
    }
}
