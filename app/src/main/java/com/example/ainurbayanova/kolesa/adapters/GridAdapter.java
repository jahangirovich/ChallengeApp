package com.example.ainurbayanova.kolesa.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.ainurbayanova.kolesa.R;
import com.example.ainurbayanova.kolesa.mvp.modules.About;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class GridAdapter extends BaseAdapter {
    private ArrayList<About> listData;
    private LayoutInflater layoutInflater;
    private Context context;

    public GridAdapter(Context aContext,  ArrayList<About> listData) {
        this.context = aContext;
        this.listData = listData;
        layoutInflater = LayoutInflater.from(aContext);
    }


    @Override
    public int getCount() {
        return listData.size();
    }

    @Override
    public Object getItem(int position) {
        return listData.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if(convertView == null){
            convertView = layoutInflater.inflate(R.layout.design_of_about_us,null);
            holder = new ViewHolder();
            holder.circleImageView = convertView.findViewById(R.id.image);
            holder.name = convertView.findViewById(R.id.name);
            holder.duty = convertView.findViewById(R.id.duty);

            convertView.setTag(holder);
        }
        else{
            holder = (ViewHolder) convertView.getTag();
        }
        holder.circleImageView.setImageResource(listData.get(position).getImage());
        holder.name.setText(listData.get(position).getName());
        holder.duty.setText(listData.get(position).getDuty());
        return convertView;
    }

    static class ViewHolder {
        CircleImageView circleImageView;
        TextView name;
        TextView duty;
    }
}
