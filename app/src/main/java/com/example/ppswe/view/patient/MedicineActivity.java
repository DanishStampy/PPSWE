package com.example.ppswe.view.patient;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.ppswe.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MedicineActivity extends AppCompatActivity {

    private BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_medicine);

        bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setSelectedItemId(R.id.medicine);

        bottomNavigationView.setOnItemSelectedListener(item -> {

            switch (item.getItemId()){
                case R.id.profile:
                    startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                    return true;

                case R.id.medicine:
                    return true;

                case R.id.vitalSign:
                    startActivity(new Intent(getApplicationContext(), VitalSignActivity.class));
                    return true;

                case R.id.report:
                    startActivity(new Intent(getApplicationContext(), ReportPatientActivity.class));
                    return true;

                case R.id.home:
                    startActivity(new Intent(getApplicationContext(), MainMenuActivity.class));
                    return true;
            }

            return false;
        });


    }
}