package com.example.authsignin;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    private EditText signupPassword;
    private EditText signupEmail;
    private EditText signupCourse, signupName;
    ImageView imgView;
    Bitmap bitmap;
    private Button signupButton;
    private Button signupBrowse;
    private FirebaseAuth mAuth;
    private String  getEmailSignup;
    private String getPasswordSignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        setView();
        OnClickListener();
    }

    private  void  setView(){
        signupEmail = findViewById(R.id.signupTextEmailAddress);
        signupPassword = findViewById(R.id.signupTextPassword);
        signupButton = findViewById(R.id.signupButton);
        imgView = (ImageView)findViewById(R.id.imageView);
        signupBrowse = findViewById(R.id.signupBrowse);
    }

    private void OnClickListener(){
        signupButton.setOnClickListener(view -> {
            getEmailSignup = signupEmail.getEditableText().toString().trim();
            getPasswordSignup = signupPassword.getEditableText().toString().trim();
            add_user();
        });

        signupBrowse.setOnClickListener(view -> {
            Dexter.withActivity(MainActivity.this)
                    .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    .withListener(new PermissionListener() {
                        @Override
                        public void onPermissionGranted(PermissionGrantedResponse response) {
                            Intent intent = new Intent(Intent.ACTION_PICK);
                            intent.setType("image/*");

                        }

                        @Override
                        public void onPermissionDenied(PermissionDeniedResponse response) {

                        }

                        @Override
                        public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                            token.continuePermissionRequest();
                        }
                    }).check();

        });
    }

    private void add_user(){
        mAuth.createUserWithEmailAndPassword(getEmailSignup, getPasswordSignup)
                .addOnCompleteListener(MainActivity.this , task -> {
                    if (task.isSuccessful()) {
                        FirebaseUser user = mAuth.getCurrentUser();
                        Intent intent = new Intent(MainActivity.this, DashBoardActivity.class);
                        //sending values to other activities
                        intent.putExtra("email", Objects.requireNonNull(mAuth.getCurrentUser()).getEmail());
                        intent.putExtra("uid", mAuth.getCurrentUser().getUid());
                        startActivity(intent);
                    } else {
                        Toast.makeText(MainActivity.this, "signup unsuccessful", Toast.LENGTH_LONG).show();
                    }
                });
    }

    public void GotoLogin(View view) {
        startActivity(new Intent(MainActivity.this, LoginActivity.class));
    }
}