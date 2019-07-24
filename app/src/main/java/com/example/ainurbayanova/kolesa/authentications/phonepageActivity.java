package com.example.ainurbayanova.kolesa.authentications;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ainurbayanova.kolesa.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class phonepageActivity extends AppCompatActivity {

    FirebaseAuth firebaseAuth;
    Button button;
    EditText editText;
    Intent intent;
    Bundle bundleForSendSms;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phonepage);
        firebaseAuth = FirebaseAuth.getInstance();
        button = findViewById(R.id.button);
        editText = findViewById(R.id.phoneNumber);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String edit = editText.getText().toString();
                bundleForSendSms.putString("number",edit);
                intent = new Intent(phonepageActivity.this,activationsActivity.class);
                startActivity(intent,bundleForSendSms);
            }
        });
    }

}
