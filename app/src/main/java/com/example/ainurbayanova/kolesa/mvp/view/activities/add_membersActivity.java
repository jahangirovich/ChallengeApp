package com.example.ainurbayanova.kolesa.mvp.view.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;

import com.example.ainurbayanova.kolesa.adapters.MemberAdapter;
import com.example.ainurbayanova.kolesa.R;
import com.example.ainurbayanova.kolesa.mvp.view.interfaces.AddMembers;
import com.example.ainurbayanova.kolesa.mvp.modules.User;
import com.example.ainurbayanova.kolesa.mvp.modules.UserForChallenge;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;

public class add_membersActivity extends AppCompatActivity {
    Toolbar toolbar;
    RecyclerView recyclerView;
    MemberAdapter memberAdapter;
    ArrayList<User> users = new ArrayList<>();
    ArrayList<User> userListCopy = new ArrayList<>();
    ArrayList<User> getMemberForArray = new ArrayList<>();
    ArrayList<UserForChallenge> keys = new ArrayList<>();
    ArrayList<UserForChallenge> add_users = new ArrayList<>();
    DatabaseReference databaseReference;
    ProgressBar progressBar;
    MaterialSearchView searchView;
    User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_members);
        initWidgets();
        initToolbar();
        initRecycler();
        uploadUsers();
    }

    public void initWidgets(){
        recyclerView = findViewById(R.id.recyclerView);
        databaseReference = FirebaseDatabase.getInstance().getReference().child("users");
        progressBar = findViewById(R.id.progressBar);
        searchView = findViewById(R.id.search_view);
        searchView.setOnQueryTextListener(new MaterialSearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter(newText);
                return false;
            }
        });
        user = new User(FirebaseAuth.getInstance().getCurrentUser().getDisplayName(),FirebaseAuth.getInstance().getCurrentUser().getPhotoUrl().toString());

    }

    private void uploadUsers() {
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot data:dataSnapshot.getChildren()){
                    if(!user.getUsername().equals(data.child("username").getValue().toString())){
                        User user = data.getValue(User.class);
                        users.add(user);
                        userListCopy.add(user);
                        add_users.add(new UserForChallenge(data.getKey()));
                    }
                }
                initRecycler();
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    private void initToolbar(){
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(add_membersActivity.this,add_challengeActivity.class));
                finish();
            }
        });
        getSupportActionBar().setTitle("Add member");
    }

    public void initRecycler(){

        memberAdapter = new MemberAdapter(this,users);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(memberAdapter);
        memberAdapter.setOnAddClickListener(new AddMembers() {
            @Override
            public void addClick(int position) {
                getMemberForArray.add(users.get(position));
                keys.add(add_users.get(position));
            }

            @Override
            public void cancelClick(int position) {
                getMemberForArray.remove(users.get(position));
                keys.remove(add_users.get(position));
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.add_menu_in_member,menu);
        MenuItem item = menu.findItem(R.id.action_search);
        searchView.setMenuItem(item);
        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {
                searchView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onSearchViewClosed() {

            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.add:
                Intent intent = new Intent();
                intent.putExtra("users", keys);
                setResult(RESULT_OK, intent);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void filter(String text) {
        users.clear();
        if (text.isEmpty()) {
            users.addAll(userListCopy);
        } else {
            text = text.toLowerCase();
            for (User item : userListCopy) {
                if (item.getUsername().toLowerCase().contains(text) || item.getEmail().toLowerCase().contains(text) ||
                        item.getKey().toUpperCase().contains(text)) {
                    users.add(item);
                }
            }
        }
        memberAdapter.notifyDataSetChanged();
    }
}
