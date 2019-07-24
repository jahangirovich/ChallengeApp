package com.example.ainurbayanova.kolesa.mvp.view.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.ainurbayanova.kolesa.R;

public class ImageDetailActivity extends AppCompatActivity {
    Toolbar toolbar;
    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_detail);
        initToolbar();
        initWidgets();
        initBundle();
    }

    public void initToolbar(){
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Image");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public void initWidgets(){
        imageView = findViewById(R.id.imageOfUser);
    }

    public void initBundle(){
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            Glide.with(this)
                    .load(bundle.getString("image"))
                    .into(imageView);
        }
    }
}
