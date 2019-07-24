package com.example.ainurbayanova.kolesa.mvp.view.interfaces;

import com.example.ainurbayanova.kolesa.mvp.modules.Challenges_done;

import java.util.ArrayList;

public interface UserStoriesInterface  {
    interface Model{
        interface OnFinishedListener{
            void onFinished(ArrayList<Challenges_done> challenges);
        }
        void onResponse(UserStoriesInterface.Model.OnFinishedListener onFinishedListener, String fKey);
    }
    interface Presenter{
        void requestFromFirebase(String fKey);
    }
    interface View{
        void showProgressBar();
        void hideProgressBar();
        void setDatasToRecylerView(ArrayList<Challenges_done> challenges);
    }
}
