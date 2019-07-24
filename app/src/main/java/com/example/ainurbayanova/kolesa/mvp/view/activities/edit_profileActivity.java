package com.example.ainurbayanova.kolesa.mvp.view.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.ainurbayanova.kolesa.R;
import com.example.ainurbayanova.kolesa.mvp.modules.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.theartofdev.edmodo.cropper.CropImage;
import com.theartofdev.edmodo.cropper.CropImageView;

import java.util.UUID;

import de.hdodenhof.circleimageview.CircleImageView;

public class edit_profileActivity extends AppCompatActivity {
    Toolbar toolbar;
    CircleImageView profileCircleImageView;
    EditText email;
    User main_user = null;
    EditText username;
    Uri fileUri;
    private static final int PERMISSION_REQUEST_CODE = 200;
    StorageReference storageReference;
    FirebaseUser user;
    final public static int REQUEST_CODE_CAMERA = 1;
    DatabaseReference databaseReference;
    String downloadUri, imgStorageName;
    EditText pointsEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());
        initToolbar();
        initWidgets();
        initBundle();
        setValues();
        setClickListeners();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public void initToolbar(){
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Edit profile");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
    public void initWidgets(){
        profileCircleImageView = findViewById(R.id.profileImage);
        email = findViewById(R.id.email);
        username = findViewById(R.id.username);
        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        user = FirebaseAuth.getInstance().getCurrentUser();
        pointsEditText = findViewById(R.id.pointsEditText);
    }
    public void initBundle(){
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            Bundle new_bundle = bundle.getBundle("bundle");
            if(new_bundle != null){
                main_user = (User) new_bundle.getSerializable("main_user");
            }
        }
    }
    public void setValues(){
        Glide.with(this)
                .load(Uri.parse(main_user.getImage()))
                .into(profileCircleImageView);
        email.setText(main_user.getEmail());
        username.setText(main_user.getUsername());
        pointsEditText.setText(main_user.getPoints() + "");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.edit_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void setClickListeners(){
        profileCircleImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startTakeImage();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                fileUri = result.getUri();
                profileCircleImageView.setImageURI(fileUri);
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                Exception error = result.getError();
            }
        }
    }

    public void startTakeImage() {

        CropImage.activity()
                .setGuidelines(CropImageView.Guidelines.ON)
                .start(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId() == R.id.Done){

            if(TextUtils.isEmpty(username.getText().toString()) || TextUtils.isEmpty(email.getText().toString())){
                Toast.makeText(edit_profileActivity.this,"Please fill all credentials",Toast.LENGTH_SHORT).show();
            }
            else if(!email.getText().toString().contains("@") && email.getText().toString().length() > 5){
                Toast.makeText(edit_profileActivity.this,"Please fill email correctly",Toast.LENGTH_SHORT).show();
            }
            else{
                if(fileUri != null){
                    uploadImage();
                }
                else{
                    final ProgressDialog progressDialog = new ProgressDialog(this);
                    progressDialog.setMessage("Loading....");
                    progressDialog.show();

                    User user = new User(username.getText().toString(),
                            main_user.getPassword(),
                            email.getText().toString(),
                            main_user.getKey(),
                            main_user.getImage(),
                            Integer.parseInt(pointsEditText.getText().toString()));
                    databaseReference.child("users").child(main_user.getKey()).setValue(user);
                    userUpdateEmail();
                    updateUsernameAndImage(main_user.getKey(),progressDialog);
                }
            }
        }
        return super.onOptionsItemSelected(item);
    }

    public void userUpdateEmail(){
        FirebaseUser users = FirebaseAuth.getInstance().getCurrentUser();
        AuthCredential credential = EmailAuthProvider
                .getCredential(main_user.getEmail(), main_user.getPassword());
        users.reauthenticate(credential)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                        user.updateEmail(email.getText().toString())
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(edit_profileActivity.this,"Success",Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                    }
                });
    }

    public void updateUsernameAndImage(String image, final ProgressDialog progressDialog){
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setPhotoUri(Uri.parse(image))
                .setDisplayName(username.getText().toString())
                .build();
        user.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(edit_profileActivity.this,"User Updated",Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                    finish();
                }
                else{
                    Toast.makeText(edit_profileActivity.this,"User Failure",Toast.LENGTH_SHORT).show();
                    progressDialog.dismiss();
                }
            }
        });
    }

    private void uploadImage() {
        if (fileUri != null) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Loading...");
            progressDialog.show();

            imgStorageName = UUID.randomUUID().toString();
            final String photoPath = "users/" + imgStorageName;
            final StorageReference ref = storageReference.child(photoPath);
            ref.putFile(fileUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            downloadUri = taskSnapshot.getDownloadUrl().toString();
                            User user = new User(username.getText().toString(),
                                    main_user.getPassword(),
                                    email.getText().toString(),
                                    main_user.getKey(),
                                    downloadUri,
                                    Integer.parseInt(pointsEditText.getText().toString()));
                            databaseReference.child("users").child(main_user.getKey()).setValue(user);
                            userUpdateEmail();
                            updateUsernameAndImage(downloadUri,progressDialog);
                            progressDialog.dismiss();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(edit_profileActivity.this, "Failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred() / taskSnapshot.getTotalByteCount());
                            progressDialog.setMessage("Uploaded " + (int) progress + "%");
                        }
                    });
        }
    }

}

