package com.example.ainurbayanova.kolesa.authentications;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ainurbayanova.kolesa.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class Forgot_password extends AppCompatActivity {
    EditText email;
    Button send;
    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        initWidgets();
        setClickListener();
    }

    private void initWidgets() {
        email = findViewById(R.id.email);
        send = findViewById(R.id.send);
        auth = FirebaseAuth.getInstance();
    }
    public void setClickListener(){
        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final ProgressDialog dialog = new ProgressDialog(Forgot_password.this);
                dialog.setMessage("Loading...");
                dialog.show();
                if (TextUtils.isEmpty(email.getText().toString())){
                    Toast.makeText(Forgot_password.this,"Please fill email",Toast.LENGTH_SHORT).show();
                    dialog.dismiss();
                }
                else{
                    auth.sendPasswordResetEmail(email.getText().toString().trim())
                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        dialog.dismiss();
                                        finish();
                                        Snackbar.make(send,"Email sent",Snackbar.LENGTH_SHORT).show();
                                    }
                                    else{
                                        dialog.dismiss();
                                        Snackbar.make(send,"Email is not found",Snackbar.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
            }
        });
    }
}
