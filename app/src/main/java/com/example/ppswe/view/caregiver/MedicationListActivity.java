package com.example.ppswe.view.caregiver;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.ppswe.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MedicationListActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medication_list);

        bottomNavigationView = findViewById(R.id.bottom_nav_caregiver);
        bottomNavigationView.setSelectedItemId(R.id.medication_list);

        bottomNavigationView.setOnItemSelectedListener(item -> {

            switch (item.getItemId()){
                case R.id.home_caregiver:
                    startActivity(new Intent(getApplicationContext(), CaregiverMainActivity.class));
                    return true;

                case R.id.report_caregiver:
                    startActivity(new Intent(getApplicationContext(), ReportActivity.class));
                    return true;

                case R.id.profile_caregiver:
                    startActivity(new Intent(getApplicationContext(), CaregiverProfileActivity.class));
                    return true;

                case R.id.medication_list:
                    return true;
            }

            return false;
        });
    }
}