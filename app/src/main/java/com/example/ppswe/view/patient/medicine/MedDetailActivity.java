package com.example.ppswe.view.patient.medicine;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.ppswe.R;
import com.example.ppswe.model.medicine.MedicineStatus;
import com.example.ppswe.view.patient.MainMenuActivity;
import com.example.ppswe.viewmodel.MedViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class MedDetailActivity extends AppCompatActivity {

    private TextView tvMedTime, tvMedNameAndDose, tvMedInstruction;
    private Button btnTaken, btnSkip, btnPostpone;
    private MaterialAlertDialogBuilder builder;

    private FirebaseAuth auth;
    private FirebaseFirestore firestore;
    private DocumentReference documentReference;

    private MedViewModel medViewModel;

    private String medId, uid, docId;
    private String timeInMillis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_med_detail);

        // Init auth and firestore
        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        medViewModel = new ViewModelProvider.AndroidViewModelFactory(this.getApplication()).create(MedViewModel.class);

        tvMedNameAndDose = findViewById(R.id.tvMedNameandDoseDetail);
        tvMedTime = findViewById(R.id.tvMedTimeDetails);
        tvMedInstruction = findViewById(R.id.tvMedInstructionDetail);

        uid = auth.getUid();

        // Get intent data
        if (getIntent().hasExtra("med_id")) {
            medId = getIntent().getStringExtra("med_id");
            //Log.d("CHECK_MED_ID", "my med is " + medId);

            String[] split = medId.split("\\.");
            docId = split[0] + "." + split[1];
            //Log.d("CHECK_DOC_ID", "my med is " + docId);

            timeInMillis = split[2];
            //Log.d("CHECK_DOC_ID", "time in millis " + timeInMillis);
        }

        // Get all data base on medId
        firestore.collection("users")
                .document(uid)
                .collection("medLists")
                .document(docId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        ArrayList<Integer> time = new ArrayList<>();
                        DocumentSnapshot documentSnapshot = task.getResult();

                        time = (ArrayList<Integer>) documentSnapshot.get("medTimes");

                        for (int i = 0; i < time.size(); i++) {

                            if (((Number)time.get(i)).intValue() == Integer.parseInt(timeInMillis)){
                                properTime(Integer.parseInt(timeInMillis)); // Change millis to proper time
                            }
                        }

                        tvMedNameAndDose.setText( documentSnapshot.getLong("medDose").intValue() + " " + documentSnapshot.getString("medType") + " of " + documentSnapshot.getString("medName"));
                        tvMedInstruction.setText("Don't forget to take " + documentSnapshot.getString("medInstruction"));
                    }
                });

        builder = new MaterialAlertDialogBuilder(MedDetailActivity.this);

        btnTaken = findViewById(R.id.btnTakenMed);
        btnTaken.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                MedicineStatus status = new MedicineStatus(docId, timeInMillis, java.time.LocalDate.now().toString(), "taken");
                medViewModel.updateMedStatus(status);
                showPatientActivity();
            }
        });

        btnSkip = findViewById(R.id.btnSkipMed);
        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                builder.setTitle("Skip Medicine")
                        .setMessage("Are you sure want to skip this medicine?")
                        .setCancelable(true)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @RequiresApi(api = Build.VERSION_CODES.O)
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                MedicineStatus status = new MedicineStatus(docId, timeInMillis, java.time.LocalDate.now().toString(), "skip");
                                medViewModel.updateMedStatus(status);
                                showPatientActivity();
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

        btnPostpone = findViewById(R.id.btnPostponeMed);
        btnPostpone.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                MedicineStatus status = new MedicineStatus(docId, timeInMillis, java.time.LocalDate.now().toString(), "postpone");
                medViewModel.updateMedStatus(status);
                showPatientActivity();
            }
        });


    }

    @SuppressLint("SetTextI18n")
    private void properTime(int i) {
        String am_pm;

        double dhour = (double) i / 60 / 60;
        double dminute = dhour % 1 * 60;

        int hour = i / 60 / 60;

        if (hour > 12) {
            am_pm = "PM";
            hour = hour - 12;
        } else if (hour == 12) {
            am_pm = "PM";
        } else {
            am_pm = "AM";
        }

        if ((int) Math.round(dminute) < 10) {
            tvMedTime.setText(hour + ":0" + (int) Math.round(dminute) + "" + am_pm);
        } else {
            tvMedTime.setText(hour + ":" + (int) Math.round(dminute) + "" + am_pm);
        }
    }

    private void showPatientActivity(){
        Intent intent = new Intent(this, MainMenuActivity.class);
        startActivity(intent);
    }

}