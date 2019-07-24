package com.example.ainurbayanova.kolesa.mvp.view.fragments.ProfileFragments;


import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ainurbayanova.kolesa.adapters.AddedMembersAdapter;
import com.example.ainurbayanova.kolesa.adapters.DoneChallengeAdapter;
import com.example.ainurbayanova.kolesa.R;
import com.example.ainurbayanova.kolesa.mvp.view.interfaces.DetailOfChallenges;
import com.example.ainurbayanova.kolesa.mvp.modules.Challenges_done;
import com.example.ainurbayanova.kolesa.mvp.modules.User;
import com.example.ainurbayanova.kolesa.mvp.modules.UserForChallenge;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;


/**
 * A simple {@link Fragment} subclass.
 */
public class DoneFragment extends Fragment {


    public DoneFragment(){

    }

    User main_user = null;

    View view;
    RecyclerView recyclerView;
    DoneChallengeAdapter doneChallengeAdapter;
    DatabaseReference databaseReference;
    TextView thereIsNo;
    ImageView flash;
    ArrayList<Challenges_done> challenges_dones = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_done, container, false);
        initWidgets();
        initBundle();
        if(main_user != null){
            getChallengesDone(main_user.getKey());
        }
        return view;
    }

    public void initBundle(){
        Bundle bundle = getArguments();
        if(bundle != null){
            main_user = (User) bundle.getSerializable("main_user");
        }
    }

    public void initWidgets(){
        databaseReference = FirebaseDatabase.getInstance().getReference();
        recyclerView = view.findViewById(R.id.recyclerView);
        flash = view.findViewById(R.id.flash);
        thereIsNo = view.findViewById(R.id.thereIsNo);

    }

    public void initRecycler(){
        doneChallengeAdapter = new DoneChallengeAdapter(getActivity(),challenges_dones);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setAdapter(doneChallengeAdapter);
        setClickListener();
    }

    public void getChallengesDone(final String key){
        databaseReference.child("challenges_done").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                challenges_dones.clear();
                for (DataSnapshot data:dataSnapshot.getChildren()){
                    if(data.child("userKey").getValue().toString().equals(key)){
                        Challenges_done challenges_done = data.getValue(Challenges_done.class);
                        challenges_dones.add(challenges_done);
                    }
                }
                Collections.reverse(challenges_dones);
                if(challenges_dones.size() == 0){
                    flash.setVisibility(View.VISIBLE);
                    thereIsNo.setVisibility(View.VISIBLE);
                }
                else{
                    flash.setVisibility(View.GONE);
                    thereIsNo.setVisibility(View.GONE);
                }
                initRecycler();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void setClickListener(){
        doneChallengeAdapter.setOnLongClickListener(new DetailOfChallenges() {
            @Override
            public void ClickI(int position) {
                initDialog(position);
            }
        });
    }

    public void initDialog(final int position){
        LayoutInflater factory = LayoutInflater.from(getActivity());

        final View listDialog = factory.inflate(R.layout.dialog_of_menu, null);
        final AlertDialog.Builder alertTimeDialog = new AlertDialog.Builder(getActivity());

        RelativeLayout detailLayout = listDialog.findViewById(R.id.detail);
        RelativeLayout deleteLayout = listDialog.findViewById(R.id.remove);
        alertTimeDialog.setView(listDialog);
        final AlertDialog hi = alertTimeDialog.show();

        deleteLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                yesOrNo(position);
                hi.dismiss();
            }
        });

        detailLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                initializeDialogDetail(position);
            }
        });
    }

    public void yesOrNo(final int position){
        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        databaseReference.child("challenges_done").child(challenges_dones.get(position).getfKey()).removeValue();
                        Toast.makeText(getActivity(),"Removed!",Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                        break;
                    case DialogInterface.BUTTON_NEGATIVE:
                        break;
                }
            }
        };
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Are your sure").setPositiveButton("Yes", dialogClickListener)
                .setNegativeButton("No", dialogClickListener).show();
    }

    public void initializeDialogDetail(int position){
        LayoutInflater factory = LayoutInflater.from(getActivity());

        final View listDialog = factory.inflate(R.layout.design_of_detail_done, null);
        final AlertDialog.Builder alertTimeDialog = new AlertDialog.Builder(getActivity());
        alertTimeDialog.setView(listDialog);
        final AlertDialog hi = alertTimeDialog.show();
        uploadUserByKey(position,listDialog);

        TextView motivation = listDialog.findViewById(R.id.motivation);
        TextView name = listDialog.findViewById(R.id.name);
        TextView date = listDialog.findViewById(R.id.date);
        TextView time = listDialog.findViewById(R.id.time);
        TextView point = listDialog.findViewById(R.id.points);

        setTextAll(motivation,name,date,time,point,position);
    }

    public void uploadUserByKey(final int position, final View listdialog){
        databaseReference.child("users").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                ArrayList<User> users = new ArrayList<>();
                for (DataSnapshot data:dataSnapshot.getChildren()){
                    for (UserForChallenge user:challenges_dones.get(position).getChallenge().getUsers()){
                        if(data.getKey().equals(user.getfKey())){
                            User usering = data.getValue(User.class);
                            users.add(usering);
                        }
                    }
                }
                RecyclerView recyclerView = listdialog.findViewById(R.id.recyclerView);
                AddedMembersAdapter addedMembersAdapter = new AddedMembersAdapter(listdialog.getContext(),users);
                LinearLayoutManager layoutManager
                        = new LinearLayoutManager(listdialog.getContext(), LinearLayoutManager.HORIZONTAL, false);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setAdapter(addedMembersAdapter);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    public void setTextAll(TextView motivation, TextView name, TextView time, TextView date, TextView point,int pos){
        name.setText(challenges_dones.get(pos).getChallenge().getName());
        motivation.setText(challenges_dones.get(pos).getChallenge().getMotivation());
        point.setText(challenges_dones.get(pos).getChallenge().getPoints() + "");
        time.setText(challenges_dones.get(pos).getChallenge().getTime().getHour() + ":" + challenges_dones.get(pos).getChallenge().getTime().getMinute());
        date.setText(challenges_dones.get(pos).getChallenge().getDate().getMonth() + "/" + challenges_dones.get(pos).getChallenge().getDate().getDay());
    }
}
