package com.example.ppswe.repo;

import android.app.Application;
import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.ppswe.model.Medicine;
import com.example.ppswe.view.MainMenuActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.List;

public class MedRepository {

    private Application application;
    private MutableLiveData<FirebaseUser> firebaseUserMutableLiveData;
    private FirebaseAuth auth;
    private FirebaseFirestore firestore;
    private DocumentReference userRef;

    public MedRepository(Application application) {
        this.application = application;
        firebaseUserMutableLiveData = new MutableLiveData<>();

        // Init firebase auth and firestore
        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        if (auth.getCurrentUser() != null) {
            firebaseUserMutableLiveData.postValue(auth.getCurrentUser());
        }

        // User doc reference
        userRef = firestore.collection("users")
                .document(auth.getUid());
    }

    public MutableLiveData<FirebaseUser> getFirebaseUserMutableLiveData() {
        return firebaseUserMutableLiveData;
    }

    public void writeMed (String medName, String medType, int medDose, int medFreq, List<String> medTimes, String medInstruction, String medDesc) {
        Medicine med = new Medicine(medName, medType, medDose, medFreq, medTimes, medInstruction, medDesc);

        userRef.collection("medLists")
                .document(String.valueOf(new Timestamp(new Date()))+"."+medName)
                .set(med)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("Success", "Medicine successfully written!");

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("FAIL", "Error writing doc", e);
                    }
                });
    }

}
