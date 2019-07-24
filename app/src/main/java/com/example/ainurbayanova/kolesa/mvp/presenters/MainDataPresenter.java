package com.example.ainurbayanova.kolesa.mvp.presenters;

import com.example.ainurbayanova.kolesa.mvp.view.interfaces.FirebaseLoadInterface;
import com.example.ainurbayanova.kolesa.mvp.modules.Challenge;

import java.util.ArrayList;

public class MainDataPresenter implements FirebaseLoadInterface.Presenter,FirebaseLoadInterface.model.OnFinishedListener {
    private FirebaseLoadInterface.View view;
    private FirebaseLoadInterface.model model;

    public MainDataPresenter(FirebaseLoadInterface.View view){
        this.view = view;
        model = new GetDataPresenter();
    }

    @Override
    public void onFinished(ArrayList<Challenge> challenges) {
        if(view != null){
            view.setDatasToRecylerView(challenges);
            view.hideProgress();
        }
    }

    @Override
    public void requestFromFirbase(String fKey) {
        if(view != null){
            view.showProgress();
        }
        model.onResponse(this,fKey);
    }
}
