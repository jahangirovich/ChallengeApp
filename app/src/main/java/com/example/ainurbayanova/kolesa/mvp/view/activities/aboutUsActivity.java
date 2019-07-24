package com.example.ainurbayanova.kolesa.mvp.view.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.GridView;

import com.example.ainurbayanova.kolesa.adapters.GridAdapter;
import com.example.ainurbayanova.kolesa.R;
import com.example.ainurbayanova.kolesa.mvp.modules.About;

import java.util.ArrayList;

public class aboutUsActivity extends AppCompatActivity {
    Toolbar toolbar;
    GridView gridView;
    ArrayList<About> aboutUsArrayList = new ArrayList<>();
    GridAdapter gridAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        initAll();
        initGrid();
    }
    public void initAll(){
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("About Us");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        gridView = findViewById(R.id.gridView);
    }

    public void initGrid(){
        aboutUsArrayList.add(new About(R.drawable.zhigeragai,"Zhiger","CEO"));
        aboutUsArrayList.add(new About(R.drawable.me,"Zhakhangir","It"));
        aboutUsArrayList.add(new About(R.drawable.baurzhanagai,"Baurzhan","Moderator"));
        gridAdapter = new GridAdapter(this,aboutUsArrayList);
        gridView.setAdapter(gridAdapter);
    }
}
