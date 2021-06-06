package com.example.authsignin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {

    private EditText signupPassword;
    private EditText signupEmail;
    private Button signupButton;
    private EditText signupName;
    private FirebaseAuth mAuth;
    private  String  getEmailSignup;
    private  String getPasswordSignup;
    private  String getNameSignup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        setView();
    }

    private  void  setView(){
        signupEmail = findViewById(R.id.signupTextEmailAddress);
        signupPassword = findViewById(R.id.signupTextPassword);
        signupButton = findViewById(R.id.signupButton);
        signupName = findViewById(R.id.signupTextPersonName);
    }

    private void OnClickListener(){
        signupButton.setOnClickListener(view -> {
            getEmailSignup = signupEmail.getText().toString().trim();
            getPasswordSignup = signupPassword.getText().toString().trim();
            add_user();
        });
    }

    private void add_user(){
        mAuth.createUserWithEmailAndPassword(getEmailSignup, getPasswordSignup)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Intent intent = new Intent(this, MainActivity.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(this, "signup unsuccessful", Toast.LENGTH_LONG).show();
                    }
                });
    }
}