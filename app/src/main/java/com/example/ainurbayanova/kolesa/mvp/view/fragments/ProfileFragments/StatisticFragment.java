package com.example.ainurbayanova.kolesa.mvp.view.fragments.ProfileFragments;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.ainurbayanova.kolesa.mvp.view.activities.Profile;
import com.example.ainurbayanova.kolesa.adapters.StatisticAdapter;
import com.example.ainurbayanova.kolesa.R;
import com.example.ainurbayanova.kolesa.mvp.view.interfaces.DetailOfChallenges;
import com.example.ainurbayanova.kolesa.mvp.view.interfaces.GetUserPoints;
import com.example.ainurbayanova.kolesa.mvp.modules.User;
import com.example.ainurbayanova.kolesa.mvp.presenters.MainPointPresenter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class StatisticFragment extends Fragment implements GetUserPoints.View{


    User main_user = null;
    public StatisticFragment() {
        // Required empty public constructor
    }

    View view;
    RecyclerView recyclerView;
    StatisticAdapter adapter;
    LinearLayout loadLinear;
    ArrayList<User> pointsList = new ArrayList<>();
    DatabaseReference databaseReference;
    ClickInterface clickInterface;
    boolean first = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_statistic, container, false);
        initWidgets();
        initBundle();
        initRecycler();
        initMainPresenter();
        return view;
    }

    public void initMainPresenter(){
        MainPointPresenter mainPointPresenter = new MainPointPresenter(this);
        mainPointPresenter.setRequest();
    }

    @Override
    public void hideProgress() {
        loadLinear.setVisibility(View.GONE);
    }

    @Override
    public void showProgress() {
        loadLinear.setVisibility(View.VISIBLE);
    }

    @Override
    public void setDataToRecycler(ArrayList<User> points) {
        if(first){
            pointsList.addAll(points);
            first = false;
        }
        adapter.notifyDataSetChanged();
    }

    public interface ClickInterface{
        void clicked(boolean isClicked);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        try{
            clickInterface =(ClickInterface) context;
        }catch (Exception e){

        }
    }

    public void initBundle(){
        Bundle bundle = getArguments();
        if(bundle != null){
            main_user = (User) bundle.getSerializable("main_user");
        }
    }

    public void initWidgets() {
        recyclerView = view.findViewById(R.id.recyclerView);
        databaseReference = FirebaseDatabase.getInstance().getReference();
        loadLinear = view.findViewById(R.id.loadLayout);
    }

    public void initRecycler() {
        adapter = new StatisticAdapter(getActivity(), pointsList ,main_user.getKey());
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(adapter);
        adapter.setOnAddClickListener(new DetailOfChallenges() {
            @Override
            public void ClickI(final int position) {
                if(main_user != null){
                    Intent intent = new Intent(getActivity(), Profile.class);
                    intent.putExtra("main_user",pointsList.get(position));
                    startActivity(intent);
                    clickInterface.clicked(true);
                }
            }
        });
    }

}
