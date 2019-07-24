package com.example.ainurbayanova.kolesa.firebase;


import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class MyFirebaseInstanceIdService extends FirebaseInstanceIdService {
    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        setNewTokenRefresh(FirebaseInstanceId.getInstance().getToken());
    }
    public void setNewTokenRefresh(String token){

        Log.i("info",String.valueOf(token));
    }

}
