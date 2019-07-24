package com.example.ainurbayanova.kolesa.mvp.view.interfaces;

import com.example.ainurbayanova.kolesa.mvp.modules.Challenge;

import java.util.ArrayList;

public interface FirebaseLoadInterface {
    interface model{
        interface OnFinishedListener{
            void onFinished(ArrayList<Challenge> challenges);
        }
        void onResponse(OnFinishedListener onFinishedListener,String fKey);
    }
    interface Presenter{
        void requestFromFirbase(String fKey);
    }
    interface View{
        void showProgress();
        void hideProgress();
        void setDatasToRecylerView(ArrayList<Challenge> challenges);
    }
}
