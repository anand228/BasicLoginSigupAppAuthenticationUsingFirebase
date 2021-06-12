package com.example.authsignin;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;
public class MainActivity extends AppCompatActivity {

    private EditText signupPassword;
    private EditText signupEmail;
    private Button signupButton;
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
    }

    private void OnClickListener(){
        signupButton.setOnClickListener(view -> {
            getEmailSignup = signupEmail.getEditableText().toString().trim();
            getPasswordSignup = signupPassword.getEditableText().toString().trim();
            add_user();
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