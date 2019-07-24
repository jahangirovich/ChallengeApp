package com.example.ainurbayanova.kolesa.mvp.view.activities;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.ainurbayanova.kolesa.authentications.Login;
import com.example.ainurbayanova.kolesa.MainActivity;
import com.example.ainurbayanova.kolesa.R;
import com.example.ainurbayanova.kolesa.mvp.view.interfaces.UserFirebaseInterface;
import com.example.ainurbayanova.kolesa.mvp.modules.User;
import com.example.ainurbayanova.kolesa.mvp.presenters.MainUserPresenter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashScreen extends AppCompatActivity implements UserFirebaseInterface.View{
    FirebaseUser user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        initUser();
    }

    public void initUser(){
        user = FirebaseAuth.getInstance().getCurrentUser();
        if(user != null){
            MainUserPresenter mainUserPresenter = new MainUserPresenter(this);
            mainUserPresenter.requestFromFirbase(user.getDisplayName());
        }
    }

    @Override
    public void showProgress() {

    }

    public static boolean isNetworkAvaliable(Context ctx) {
        ConnectivityManager connectivityManager = (ConnectivityManager) ctx
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        if ((connectivityManager
                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE) != null && connectivityManager
                .getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED)
                || (connectivityManager
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI) != null && connectivityManager
                .getNetworkInfo(ConnectivityManager.TYPE_WIFI)
                .getState() == NetworkInfo.State.CONNECTED)) {
            return true;
        } else {
            return false;
        }
    }

    @Override
    public void hideProgress() {

    }

    @Override
    public void setData(User user) {
        if(user != null){
            finish();
            startActivity(new Intent(this, MainActivity.class));
        }
        else{
            finish();
            startActivity(new Intent(this, Login.class));
        }
    }
}
