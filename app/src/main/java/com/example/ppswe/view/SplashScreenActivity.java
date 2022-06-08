package com.example.ppswe.view;

import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.ppswe.view.authentication.LoginActivity;
import com.example.ppswe.view.caregiver.CaregiverMainActivity;
import com.example.ppswe.view.patient.MainMenuActivity;
import com.example.ppswe.viewmodel.MedViewModel;
import com.example.ppswe.viewmodel.UserViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class SplashScreenActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseFirestore firestore;
    private MedViewModel medViewModel;
    String uid;
    String userRoles;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Init firebase
        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        uid = auth.getUid();

        if (auth.getCurrentUser() == null) {
            startActivity(new Intent(SplashScreenActivity.this, LoginActivity.class));
            finish();
        } else {
            // Med view model
            medViewModel = new ViewModelProvider.AndroidViewModelFactory(this.getApplication()).create(MedViewModel.class);
            medViewModel.getMedDataCaregiver().observe(this, medicineViews -> {
                //Log.d("PATIENT_screen", "UMMM " +medicineViews.size());

                firestore.collection("users")
                        .document(auth.getUid())
                        .get()
                        .addOnSuccessListener(documentSnapshot -> {
                            userRoles = documentSnapshot.getString("roles");

                            if (userRoles.equals("patient")){
                                //Log.d("PATIENT_screen", "UMMM");
                                startActivity(new Intent(SplashScreenActivity.this, MainMenuActivity.class));
                                finish();
                            } else {
                                Intent intent = new Intent(SplashScreenActivity.this, CaregiverMainActivity.class);
                                intent.putExtra("med_view_data", medicineViews);
                                startActivity(intent);
                                finish();
                            }
                        })
                        .addOnFailureListener(e -> {
                            startActivity(new Intent(SplashScreenActivity.this, LoginActivity.class));
                            finish();
                        });
            });
        }
    }
}
