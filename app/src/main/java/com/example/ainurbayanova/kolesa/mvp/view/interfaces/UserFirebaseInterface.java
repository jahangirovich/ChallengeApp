package com.example.ainurbayanova.kolesa.mvp.view.interfaces;

import com.example.ainurbayanova.kolesa.mvp.modules.User;

public interface UserFirebaseInterface {
    interface model{
        interface OnFinishedListener{
            void onFinished(User user);
        }
        void onResponse(OnFinishedListener onFinishedListener, String username);
    }
    interface Presenter{
        void requestFromFirbase(String username);
    }
    interface View{
        void showProgress();
        void hideProgress();
        void setData(User user);
    }
}
