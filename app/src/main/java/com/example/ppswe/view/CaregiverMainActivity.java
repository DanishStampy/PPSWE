package com.example.ppswe.view;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.ppswe.R;
import com.example.ppswe.view.authentication.LoginActivity;
import com.google.firebase.auth.FirebaseAuth;

public class CaregiverMainActivity extends AppCompatActivity {

    private FirebaseAuth auth;

    private Button btnLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caregiver_main);

        // Init auth
        auth = FirebaseAuth.getInstance();

        if(auth.getCurrentUser() == null){
            showLoggedOut();
        }

        btnLogout = findViewById(R.id.btnLogout_caregiver);
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.signOut();
                showLoggedOut();
            }
        });
    }

    public void showLoggedOut () {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}