package com.example.ppswe.view.patient;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.ppswe.R;
import com.example.ppswe.adapter.medDataAdapter;
import com.example.ppswe.model.medicine.MedicineView;
import com.example.ppswe.view.authentication.LoginActivity;
import com.example.ppswe.view.caregiver.CaregiverMainActivity;
import com.example.ppswe.view.patient.medicine.MedDetailActivity;
import com.example.ppswe.viewmodel.AuthViewModel;
import com.example.ppswe.viewmodel.MedViewModel;
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

public class MainMenuActivity extends AppCompatActivity implements medDataAdapter.OnMedDetailListener {

    private ImageButton imgBtnLogout;
    private BottomNavigationView bottomNavigationView;
    private RecyclerView recyclerViewMedList;
    private MaterialAlertDialogBuilder builder;
    private TextView tvTodayDate;

    private FirebaseAuth auth;
    private FirebaseFirestore firestore;

    private AuthViewModel authViewModel;
    private MedViewModel medViewModel;

    private medDataAdapter medDataAdapter;

    private String role, uid;
    private ArrayList<MedicineView> medicineViews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        // Init auth
        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        medicineViews = new ArrayList<>();

        if(auth.getCurrentUser() == null){
            showLoggedOut();
        } else {
            uid = auth.getUid();
        }

        tvTodayDate = findViewById(R.id.tvTodayDate);
        tvTodayDate.setText(todayDate());

        // Get user roles
        firestore.collection("users")
                .document(uid)
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
            medicineViews = medData;
            medDataAdapter = new medDataAdapter(medicineViews, this);
            recyclerViewMedList.setAdapter(medDataAdapter);

            //Log.d("MED_DATA", medicineViews.get(1).getMedID());
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

                case R.id.report:
                    startActivity(new Intent(getApplicationContext(), ReportPatientActivity.class));
                    return true;

                case R.id.home:
                    return true;
            }

            return false;
        });


        imgBtnLogout = findViewById(R.id.imgMainBtnLogout);
        builder = new MaterialAlertDialogBuilder(MainMenuActivity.this);

        imgBtnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                builder.setTitle("Logout")
                        .setMessage("Are you sure want to logout?")
                        .setCancelable(true)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                auth.signOut();
                                showLoggedOut();
                            }
                        })
                        .setNegativeButton("Nope", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        })
                        .show();
            }
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

    @Override
    public void onMedDetailClick(int position, String medID) {
        Log.d("CHECK_MEDID", "clicked " + medID);
        Intent intent = new Intent(this, MedDetailActivity.class);
        intent.putExtra("med_id", medID);
        startActivity(intent);
    }
}