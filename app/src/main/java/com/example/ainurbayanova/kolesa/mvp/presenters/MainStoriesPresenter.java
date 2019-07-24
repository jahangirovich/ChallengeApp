package com.example.ainurbayanova.kolesa.mvp.presenters;

import com.example.ainurbayanova.kolesa.mvp.view.interfaces.UserStoriesInterface;
import com.example.ainurbayanova.kolesa.mvp.modules.Challenges_done;

import java.util.ArrayList;

public class MainStoriesPresenter implements UserStoriesInterface.Model.OnFinishedListener,UserStoriesInterface.Presenter {
    private UserStoriesInterface.View view;
    private UserStoriesInterface.Model model;

    public MainStoriesPresenter(UserStoriesInterface.View view){
        this.view = view;
        model = new GetStoriesPresenter();
    }

    @Override
    public void onFinished(ArrayList<Challenges_done> challenges) {
        if(view != null){
            view.setDatasToRecylerView(challenges);
            view.hideProgressBar();
        }
    }

    @Override
    public void requestFromFirebase(String fKey) {
        if(view != null){
            view.showProgressBar();
        }
        model.onResponse(this,fKey);
    }
}
