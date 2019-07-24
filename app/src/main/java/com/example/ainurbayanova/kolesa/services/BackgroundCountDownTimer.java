package com.example.ainurbayanova.kolesa.services;

import android.app.Service;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

public class BackgroundCountDownTimer extends Service {
    CountDownTimer cTimer = null;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        startTimer();

        return START_STICKY;
    }

    void startTimer() {
        cTimer = new CountDownTimer(30000, 1000) {
            public void onTick(long millisUntilFinished) {
                Log.i("info",millisUntilFinished/1000 + "");
            }
            public void onFinish() {
            }
        };
        cTimer.start();
    }


    //cancel timer
    void cancelTimer() {
        if(cTimer!=null)
            cTimer.cancel();
    }
}
