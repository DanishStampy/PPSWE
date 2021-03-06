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
import com.example.ppswe.adapter.LoadingDialog;
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
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
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
    private MaterialAlertDialogBuilder builder;

    private FirebaseAuth auth;
    private FirebaseFirestore firestore;

    private MedViewModel medViewModel;
    private medDataAdapterCaregiver medDataAdapterCaregiver;
    private ArrayList<MedicineView> medicineViewsCaregiver;
    private LoadingDialog loadingDialog;

    @SuppressLint("NonConstantResourceId")
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

        // init loading dialog
        loadingDialog = new LoadingDialog(this);

        // recyclerview
        recyclerViewMedListCaregiver = findViewById(R.id.rcMedList_caregiver);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerViewMedListCaregiver.setLayoutManager(linearLayoutManager);

        // get parcelable object
        Intent intent = new Intent();
        medicineViewsCaregiver = intent.getParcelableExtra("med_view_data");
        //Log.d("Size", "Size is " +medicineViewsCaregiver.size());

        loadingDialog.showDialog();
        // Med viewmodel
        medViewModel = new ViewModelProvider.AndroidViewModelFactory(this.getApplication()).create(MedViewModel.class);
        medViewModel.getMedDataCaregiver().observe(this, medData-> {
            loadingDialog.hideDialog();

            medicineViewsCaregiver = medData;
            //Log.d("SIZE_MEDDATA", "Size is " + medData.size());
            medDataAdapterCaregiver = new medDataAdapterCaregiver(medicineViewsCaregiver);
            recyclerViewMedListCaregiver.setAdapter(medDataAdapterCaregiver);
        });

        tvTodayDateCaregiver = findViewById(R.id.tvTodayDate_caregiver);
        tvTodayDateCaregiver.setText(todayDate());


        imgBtnLogout = findViewById(R.id.imgBtnLogout_Caregiver);
        builder = new MaterialAlertDialogBuilder(CaregiverMainActivity.this);

        imgBtnLogout.setOnClickListener(view -> {
            builder.setTitle("Logout")
                    .setMessage("Are you sure want to logout?")
                    .setCancelable(true)
                    .setPositiveButton("Yes", (dialogInterface, i) -> {
                        auth.signOut();
                        showLoggedOut();
                    })
                    .setNegativeButton("Nope", (dialogInterface, i) -> {
                        dialogInterface.cancel();
                    })
                    .show();

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
    private String todayDate() {

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