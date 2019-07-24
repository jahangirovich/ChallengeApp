package com.example.ainurbayanova.kolesa.mvp.presenters;

import com.example.ainurbayanova.kolesa.mvp.view.interfaces.GetUserPoints;
import com.example.ainurbayanova.kolesa.mvp.modules.User;

import java.util.ArrayList;

public class MainPointPresenter implements GetUserPoints.Model.OnFinishedListener,GetUserPoints.Presenter {
    GetUserPoints.Model model;
    GetUserPoints.View view;

    public MainPointPresenter(GetUserPoints.View view){
        this.view = view;
        this.model = new GetPointsPresenter();
    }

    @Override
    public void onFinished(ArrayList<User> points) {
        if(view != null){
            view.hideProgress();
            view.setDataToRecycler(points);
        }
    }

    @Override
    public void setRequest() {
        if(view != null){
            view.showProgress();
        }
        model.getPoints(this);
    }
}
