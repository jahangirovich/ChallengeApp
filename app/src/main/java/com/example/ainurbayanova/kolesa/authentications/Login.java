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

import com.example.ainurbayanova.kolesa.MainActivity;
import com.example.ainurbayanova.kolesa.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {
    TextView forgot;
    TextView create;
    Button login;
    EditText email;
    EditText password;
    FirebaseAuth auth;
    FirebaseUser user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_layout);
        initWidgets();
        checkUser();
        setClickListener();
    }

    public void initWidgets() {
        forgot = findViewById(R.id.forgot);
        create = findViewById(R.id.create);
        login = findViewById(R.id.login);
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        auth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
    }

    public void checkUser() {
        if (user != null) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
        else{

        }
    }

    public void setClickListener() {
        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, Forgot_password.class));
            }
        });
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this, Registration.class));
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkAllRight();
            }
        });
    }

    public void checkAllRight() {
        final ProgressDialog dialog = new ProgressDialog(Login.this);
        dialog.setMessage("Loading...");
        dialog.show();
        String emails = email.getText().toString();
        String passwords = password.getText().toString();
        if (emails.isEmpty() || passwords.isEmpty()) {
            Snackbar.make(login, "Please fill all credentials", Snackbar.LENGTH_SHORT).show();
            dialog.dismiss();
        } else {
            auth.signInWithEmailAndPassword(emails, passwords).addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        startActivity(new Intent(Login.this, MainActivity.class));
                        dialog.dismiss();
                    } else {
                        Snackbar.make(login, "Something went wrong please check all credentials", Snackbar.LENGTH_SHORT).show();
                        dialog.dismiss();
                    }
                }
            });
        }

    }
}
