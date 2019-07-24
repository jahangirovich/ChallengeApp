package com.example.ainurbayanova.kolesa.mvp.presenters;

import com.example.ainurbayanova.kolesa.mvp.view.interfaces.UserFirebaseInterface;
import com.example.ainurbayanova.kolesa.mvp.modules.User;

public class MainUserPresenter implements UserFirebaseInterface.Presenter,UserFirebaseInterface.model.OnFinishedListener {
    UserFirebaseInterface.View view;
    UserFirebaseInterface.model model;

    public MainUserPresenter(UserFirebaseInterface.View view){
        this.view = view;
        this.model = new GetUserPresenter();
    }

    @Override
    public void onFinished(User user) {
        if(view != null){
            view.setData(user);
            view.hideProgress();
        }
    }

    @Override
    public void requestFromFirbase(String username) {
        if(view != null){
            view.showProgress();
        }
        model.onResponse(this,username);
    }
}
