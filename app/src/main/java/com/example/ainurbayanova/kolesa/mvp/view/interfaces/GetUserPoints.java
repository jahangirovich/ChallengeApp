package com.example.ainurbayanova.kolesa.mvp.view.interfaces;

import com.example.ainurbayanova.kolesa.mvp.modules.User;

import java.util.ArrayList;

public interface GetUserPoints {
    interface Model{
        interface OnFinishedListener{
            void onFinished(ArrayList<User> users);
        }
        void getPoints(OnFinishedListener onFinishedListener);
    }
    interface Presenter{
        void setRequest();
    }
    interface View{
        void hideProgress();
        void showProgress();
        void setDataToRecycler(ArrayList<User> users);
    }
}
