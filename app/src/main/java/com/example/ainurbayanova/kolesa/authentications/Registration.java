package com.example.ainurbayanova.kolesa.authentications;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ainurbayanova.kolesa.MainActivity;
import com.example.ainurbayanova.kolesa.R;
import com.example.ainurbayanova.kolesa.mvp.modules.Point;
import com.example.ainurbayanova.kolesa.mvp.modules.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Registration extends AppCompatActivity {
    TextView have;
    EditText username;
    EditText email;
    EditText password;
    EditText passwordRepeat;
    Button register;
    FirebaseAuth auth;
    DatabaseReference databaseReference;
    String picture;
    boolean usernameExist = false;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_layout);
        initWidgets();
        setClickListener();
    }

    private void setClickListener() {
        have.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAllCredentials();
            }
        });
    }

    public void checkAllCredentials(){
        final ProgressDialog dialog = new ProgressDialog(Registration.this);
        dialog.setMessage("Loading...");
        dialog.show();
        final String usernames = username.getText().toString();
        final String emails = email.getText().toString();
        final String passwords = password.getText().toString();
        String passwordRepeats = passwordRepeat.getText().toString();

        if(usernames.isEmpty() || emails.isEmpty() || passwords.isEmpty() || passwordRepeats.isEmpty()){
            Snackbar.make(register, "Please fill all credentials", Snackbar.LENGTH_SHORT).show();
            dialog.dismiss();
        }
        else if(!passwords.equals(passwordRepeats)){
            Snackbar.make(register, "Please fill equal password", Snackbar.LENGTH_SHORT).show();
            dialog.dismiss();
        }
        else if(passwords.length() < 7){
            Snackbar.make(register, "Symbols in password must be 7 symbols", Snackbar.LENGTH_SHORT).show();
            dialog.dismiss();
        }
        else if(!emails.contains("@")){
            Snackbar.make(register, "Please fill email correctly.", Snackbar.LENGTH_SHORT).show();
            dialog.dismiss();
        }
        else{
            databaseReference.child("users").addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    usernameExist = false;
                    for (DataSnapshot userId:dataSnapshot.getChildren()){

                        if(userId.child("username").getValue().toString().equals(usernames)){
                            usernameExist = true;
                        }
                    }
                    if(!usernameExist){
                        authMethod(emails,passwords,dialog,usernames);
                    }
                    else{
                        Toast.makeText(Registration.this, "Username already exist", Toast.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                }
            });
        }
    }

    public void authMethod(final String emails,final String passwords,
                           final ProgressDialog dialog,final String usernames){
        auth.createUserWithEmailAndPassword(emails,passwords).addOnCompleteListener(Registration.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(!task.isSuccessful()){
                    Snackbar.make(register, "Something went wrong", Snackbar.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
                else{
                    String key = databaseReference.child("users").push().getKey();
                    User user = new User(usernames,passwords,emails,key,picture,0);
                    Point point = new Point(0,key,usernames);
                    databaseReference.child("users").child(key).setValue(user);
                    databaseReference.child("points").child(key).setValue(point);

                    Intent intent = new Intent(Registration.this, MainActivity.class);
                    intent.putExtra("user",usernames);
                    startActivity(intent);
                    finish();
                    dialog.dismiss();
                }
            }
        });
    }

    public void initWidgets(){
        have = findViewById(R.id.have);
        username = findViewById(R.id.username);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        passwordRepeat = findViewById(R.id.passwordRepeat);
        register = findViewById(R.id.register);
        auth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        picture = "https://i.pinimg.com/736x/0b/6e/90/0b6e90105524663fee93fb9bb7f0c860.jpg";
    }


}
