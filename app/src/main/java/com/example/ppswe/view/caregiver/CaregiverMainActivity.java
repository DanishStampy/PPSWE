package com.example.ppswe.view.caregiver;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.example.ppswe.R;
import com.example.ppswe.view.authentication.LoginActivity;
import com.example.ppswe.view.patient.MainMenuActivity;
import com.example.ppswe.view.patient.MedicineActivity;
import com.example.ppswe.view.patient.ProfileActivity;
import com.example.ppswe.view.patient.VitalSignActivity;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class CaregiverMainActivity extends AppCompatActivity {

    private ImageButton imgBtnLogout;
    private BottomNavigationView bottomNavigationView;

    private FirebaseAuth auth;
    private FirebaseFirestore firestore;

    private String role;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caregiver_main);

        // Init auth
        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        if(auth.getCurrentUser() == null){
            showLoggedOut();
        }

        // Get user roles
        firestore.collection("users")
                .document(auth.getUid())
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        role = documentSnapshot.getString("roles");
                        Log.d("USER_ROLE", "My role is " + role);
                        Log.d("BOOLEAN_ROLE", "That are " + role.equals("caregiver"));

                        if(role.equals("patient"))
                            showPatientActivity();
                    }
                });

        imgBtnLogout = findViewById(R.id.imgBtnLogout_Caregiver);
        imgBtnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.signOut();
                showLoggedOut();
            }
        });

        bottomNavigationView = findViewById(R.id.bottom_nav_caregiver);
        bottomNavigationView.setSelectedItemId(R.id.home_caregiver);

        bottomNavigationView.setOnItemSelectedListener(item -> {

            switch (item.getItemId()){
                case R.id.profile_caregiver:
                    startActivity(new Intent(getApplicationContext(), CaregiverProfileActivity.class));
                    return true;

                case R.id.report_caregiver:
                    startActivity(new Intent(getApplicationContext(), ReportActivity.class));
                    return true;

                case R.id.medication_list:
                    startActivity(new Intent(getApplicationContext(), MedicationListActivity.class));
                    return true;

                case R.id.home_caregiver:
                    return true;
            }

            return false;
        });
    }

    private void showPatientActivity(){
        Intent intent = new Intent(this, MainMenuActivity.class);
        startActivity(intent);
        finish();
    }

    private void showCaregiverActivity(){
        Intent intent = new Intent(this, CaregiverMainActivity.class);
        startActivity(intent);
        finish();
    }

    public void showLoggedOut () {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}