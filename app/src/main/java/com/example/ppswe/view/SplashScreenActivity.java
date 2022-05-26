package com.example.ppswe.view;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.example.ppswe.view.authentication.LoginActivity;
import com.example.ppswe.view.caregiver.CaregiverMainActivity;
import com.example.ppswe.view.patient.MainMenuActivity;
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
    private UserViewModel userViewModel;
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
            // User view model
            userViewModel = new ViewModelProvider.AndroidViewModelFactory(this.getApplication()).create(UserViewModel.class);

            firestore.collection("users")
                    .document(auth.getUid())
                    .get()
                    .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                        @Override
                        public void onSuccess(DocumentSnapshot documentSnapshot) {
                            userRoles = documentSnapshot.getString("roles");
                            if (userRoles.equals("patient")){
                                startActivity(new Intent(SplashScreenActivity.this, MainMenuActivity.class));
                                finish();
                            } else {
                                userViewModel.setPatientEmail();

                                startActivity(new Intent(SplashScreenActivity.this, CaregiverMainActivity.class));
                                finish();
                            }
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            startActivity(new Intent(SplashScreenActivity.this, LoginActivity.class));
                            finish();
                        }
                    });
        }
    }
}
