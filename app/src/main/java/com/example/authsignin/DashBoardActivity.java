package com.example.authsignin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;

public class DashBoardActivity extends AppCompatActivity {
    private long longBackPressedTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
        TextView email = findViewById(R.id.dashboardEmail);
        TextView uid = findViewById(R.id.dashboardUid);
        //catching values from other activity
        email.setText(getIntent().getStringExtra("email"));
        uid.setText(getIntent().getStringExtra("uid"));
    }

    public void LogoutFromHere(View view) {
        FirebaseAuth.getInstance().signOut();
        startActivity(new Intent(DashBoardActivity.this, MainActivity.class));
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
       //super.onBackPressed();
    }
}