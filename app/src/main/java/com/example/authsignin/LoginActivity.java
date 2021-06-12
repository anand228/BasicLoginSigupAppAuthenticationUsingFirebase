package com.example.authsignin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    private EditText loginPassword;
    private EditText loginEmail;
    private Button loginButton;
    private FirebaseAuth mAuth;
    private String getEmailLogin;
    private String getPasswordLogin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        setView();
        OnClickListener();
    }

    private  void  setView(){
        loginEmail = findViewById(R.id.loginTextEmailAddress);
        loginPassword = findViewById(R.id.loginTextPassword);
        loginButton = findViewById(R.id.loginButton);
    }

    private void OnClickListener() {
        loginButton.setOnClickListener(view -> {
            getEmailLogin = loginEmail.getText().toString().trim();
            getPasswordLogin = loginPassword.getText().toString().trim();
            add_user();
        });
    }

    private void add_user(){
        mAuth.signInWithEmailAndPassword(getEmailLogin, getPasswordLogin)
                .addOnCompleteListener(LoginActivity.this, task -> {
                    if(task.isSuccessful()){
                        Intent intent = new Intent(LoginActivity.this, DashBoardActivity.class);
                        //sending values to other activities
                        intent.putExtra("email", Objects.requireNonNull(mAuth.getCurrentUser()).getEmail());
                        intent.putExtra("uid", mAuth.getCurrentUser().getUid());
                        startActivity(intent);
                    }
                    else{
                        loginPassword.setText("");
                        loginEmail.setText("");
                        Toast.makeText(this, "login failed",Toast.LENGTH_SHORT).show();
                    }
                });
    }
}