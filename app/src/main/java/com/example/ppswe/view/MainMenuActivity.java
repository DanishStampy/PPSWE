package com.example.ppswe.view;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.ppswe.R;
import com.example.ppswe.adapter.medDataAdapter;
import com.example.ppswe.view.authentication.LoginActivity;
import com.example.ppswe.viewmodel.AuthViewModel;
import com.example.ppswe.viewmodel.MedViewModel;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;

public class MainMenuActivity extends AppCompatActivity {

    private ImageButton imgBtnLogout;
    private BottomNavigationView bottomNavigationView;
    private RecyclerView recyclerViewMedList;

    FirebaseAuth auth;
    private AuthViewModel authViewModel;
    private MedViewModel medViewModel;

    private medDataAdapter medDataAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        // Init auth
        auth = FirebaseAuth.getInstance();

        if(auth.getCurrentUser() == null){
            showLoggedOut();
        }

        // Auth view model
        authViewModel = new ViewModelProvider.AndroidViewModelFactory(this.getApplication()).create(AuthViewModel.class);
        authViewModel.getLoggedStatus().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    showLoggedOut();
                }
            }
        });

        // recyclerview
        recyclerViewMedList = findViewById(R.id.rcMedList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerViewMedList.setLayoutManager(linearLayoutManager);

        // Med view model
        medViewModel = new ViewModelProvider.AndroidViewModelFactory(this.getApplication()).create(MedViewModel.class);
        //Log.d("TEST_MED_DATA", "This is = " + medViewModel.getMedData().size());
        medViewModel.getMedData().observe(this, medData-> {
            medDataAdapter = new medDataAdapter(medData);
            recyclerViewMedList.setAdapter(medDataAdapter);
        });

        bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setSelectedItemId(R.id.home);

        bottomNavigationView.setOnItemSelectedListener(item -> {

            switch (item.getItemId()){
                case R.id.profile:
                    startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                    return true;

                case R.id.medicine:
                    startActivity(new Intent(getApplicationContext(), MedicineActivity.class));
                    return true;

                case R.id.vitalSign:
                    startActivity(new Intent(getApplicationContext(), VitalSignActivity.class));
                    return true;

                case R.id.home:
                    return true;
            }

            return false;
        });




        imgBtnLogout = findViewById(R.id.imgMainBtnLogout);
        imgBtnLogout.setOnClickListener(new View.OnClickListener() {
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