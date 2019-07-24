package com.example.ainurbayanova.kolesa.authentications;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class activationsActivity extends AppCompatActivity {
    String verificationCode = null;
    FirebaseAuth firebaseAuth;
    Bundle bundle;
    String number;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activations);
        firebaseAuth = FirebaseAuth.getInstance();
        bundle = getIntent().getExtras();
        if(bundle != null){
            number = bundle.getString("number");
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
    private void sendVerificationCode(String number){
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                number,
                60,
                TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD,
                mCallback
        );
    }

    private void verifyCode(String code){
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationCode,code);
        signInWithCredentials(credential);
    }

    private void signInWithCredentials(PhoneAuthCredential credential){
        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(activationsActivity.this,"Done",Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(activationsActivity.this,"Fail",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallback = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
        @Override
        public void onCodeSent(String s, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
            super.onCodeSent(s, forceResendingToken);
            verificationCode = s;
        }

        @Override
        public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {
            String code = phoneAuthCredential.getSmsCode();
            verifyCode(code);
        }

        @Override
        public void onVerificationFailed(FirebaseException e) {
            Toast.makeText(activationsActivity.this,"Failed in verification",Toast.LENGTH_SHORT).show();
        }
    };
}
