package com.example.ppswe.view.caregiver;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.ppswe.R;
import com.example.ppswe.adapter.medDataAdapterCaregiver;
import com.example.ppswe.model.medicine.MedicineView;
import com.example.ppswe.model.user.SingletonStatusPatient;
import com.example.ppswe.view.authentication.LoginActivity;
import com.example.ppswe.view.patient.MainMenuActivity;
import com.example.ppswe.view.patient.MedicineActivity;
import com.example.ppswe.view.patient.ProfileActivity;
import com.example.ppswe.view.patient.VitalSignActivity;
import com.example.ppswe.view.patient.medicine.MedDetailActivity;
import com.example.ppswe.viewmodel.MedViewModel;
import com.example.ppswe.viewmodel.UserViewModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class CaregiverMainActivity extends AppCompatActivity {

    private ImageButton imgBtnLogout;
    private BottomNavigationView bottomNavigationView;
    private TextView tvTodayDateCaregiver;
    private RecyclerView recyclerViewMedListCaregiver;

    private FirebaseAuth auth;
    private FirebaseFirestore firestore;

    private UserViewModel userViewModel;
    private MedViewModel medViewModel;
    private SingletonStatusPatient singletonStatusPatient;

    private medDataAdapterCaregiver medDataAdapterCaregiver;

    private String role;
    private ArrayList<MedicineView> medicineViewsCaregiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_caregiver_main);

        // Init auth
        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        medicineViewsCaregiver = new ArrayList<>();

        if(auth.getCurrentUser() == null){
            showLoggedOut();
        }

        singletonStatusPatient = SingletonStatusPatient.getInstance();

        tvTodayDateCaregiver = findViewById(R.id.tvTodayDate_caregiver);
        tvTodayDateCaregiver.setText(todayDate(singletonStatusPatient.getPatientName()));



        imgBtnLogout = findViewById(R.id.imgBtnLogout_Caregiver);
        imgBtnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                auth.signOut();
                showLoggedOut();
            }
        });

        // recyclerview
        recyclerViewMedListCaregiver = findViewById(R.id.rcMedList_caregiver);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerViewMedListCaregiver.setLayoutManager(linearLayoutManager);

        // Med viewmodel
        medViewModel = new ViewModelProvider.AndroidViewModelFactory(this.getApplication()).create(MedViewModel.class);
        medViewModel.getMedDataCaregiver().observe(this, medData-> {
            medicineViewsCaregiver = medData;
            Log.d("SIZE_MEDDATA", "Size is " + medData.size());
            medDataAdapterCaregiver = new medDataAdapterCaregiver(medicineViewsCaregiver);
            recyclerViewMedListCaregiver.setAdapter(medDataAdapterCaregiver);
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

    @SuppressLint("SimpleDateFormat")
    private String todayDate(String patientName) {

        String result = "";

        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
        result += simpleDateFormat.format(calendar.getTime()) + ", ";

        Format format = new SimpleDateFormat("EEEE");
        String day = format.format(new Date());

        result += day;

        return result;
    }

    public void showLoggedOut () {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}