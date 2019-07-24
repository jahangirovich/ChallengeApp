package com.example.ainurbayanova.kolesa.mvp.view.activities;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.example.ainurbayanova.kolesa.mvp.view.fragments.ProfileFragments.DoneFragment;
import com.example.ainurbayanova.kolesa.mvp.view.fragments.ProfileFragments.GoalDoneFragment;
import com.example.ainurbayanova.kolesa.mvp.view.fragments.ProfileFragments.StatisticFragment;

public class TabPage extends FragmentStatePagerAdapter {
    private int numberOfTabs;
    public static final int CURRENT_TASK_FRAGMENT = 0;
    public static final int DONE_TASK_FRAGMENT = 2;
    public static final int GOAL_DONE_TASK_FRAGMENT = 1;

    StatisticFragment statisticFragment;
    DoneFragment doneFragment;
    GoalDoneFragment goalDoneFragment;

    public TabPage(FragmentManager fm, int numberOfTabs) {
        super(fm);
        this.numberOfTabs = numberOfTabs;
        doneFragment = new DoneFragment();
        statisticFragment = new StatisticFragment();
        goalDoneFragment = new GoalDoneFragment();
    }

    @Override
    public Fragment getItem(int i) {
        switch (i){
            case 0:
                return doneFragment;
            case 1:
                return statisticFragment;
            case 2:
                return goalDoneFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return numberOfTabs;
    }
}