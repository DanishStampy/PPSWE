package com.example.ppswe.view.caregiver;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.ppswe.R;
import com.example.ppswe.view.patient.MedicineActivity;
import com.example.ppswe.view.patient.ProfileActivity;
import com.example.ppswe.view.patient.VitalSignActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class CaregiverProfileActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caregiver_profile);

        bottomNavigationView = findViewById(R.id.bottom_nav_caregiver);
        bottomNavigationView.setSelectedItemId(R.id.profile_caregiver);

        bottomNavigationView.setOnItemSelectedListener(item -> {

            switch (item.getItemId()){
                case R.id.home_caregiver:
                    startActivity(new Intent(getApplicationContext(), CaregiverMainActivity.class));
                    return true;

                case R.id.report_caregiver:
                    startActivity(new Intent(getApplicationContext(), ReportActivity.class));
                    return true;

                case R.id.medication_list:
                    startActivity(new Intent(getApplicationContext(), MedicationListActivity.class));
                    return true;

                case R.id.profile_caregiver:
                    return true;
            }

            return false;
        });
    }
}