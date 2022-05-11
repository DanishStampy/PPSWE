package com.example.ppswe.view.patient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;

import com.example.ppswe.R;
import com.example.ppswe.adapter.medDataAdapter;
import com.example.ppswe.view.authentication.LoginActivity;
import com.example.ppswe.view.caregiver.CaregiverMainActivity;
import com.example.ppswe.viewmodel.AuthViewModel;
import com.example.ppswe.viewmodel.MedViewModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MainMenuActivity extends AppCompatActivity {

    private ImageButton imgBtnLogout;
    private BottomNavigationView bottomNavigationView;
    private RecyclerView recyclerViewMedList;

    private FirebaseAuth auth;
    private FirebaseFirestore firestore;

    private AuthViewModel authViewModel;
    private MedViewModel medViewModel;

    private medDataAdapter medDataAdapter;

    private String role;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

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
                        Log.d("BOOLEAN_ROLE", "That are " + role.equals("patient"));

                        if(role.equals("caregiver"))
                            showCaregiverActivity();
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