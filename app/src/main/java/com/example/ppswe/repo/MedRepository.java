package com.example.ppswe.repo;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.example.ppswe.model.Medicine;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class MedRepository {

    private Application application;
    private MutableLiveData<ArrayList<Medicine>> medicineArrayList;

    private FirebaseAuth auth;
    private FirebaseFirestore firestore;

    private CollectionReference medRef;
    private DocumentReference userRef;

    private String uid;

    public MedRepository(Application application) {
        this.application = application;
        medicineArrayList = new MutableLiveData<>();

        // Init firebase auth and firestore
        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        if (auth.getCurrentUser() != null) {
            uid = auth.getUid();

            // User doc reference
            userRef = firestore.collection("users")
                    .document(uid);

            // Medicine doc reference
            medRef = userRef.collection("medLists");
        }
    }

    // Insert med in firestore
    public void writeMed (String medName, String medType, int medDose, int medFreq, List<Integer> medTimes, String medInstruction, String medDesc) {
        Medicine med = new Medicine(medName, medType, medDose, medFreq, medTimes, medInstruction, medDesc);

        userRef.collection("medLists")
                .document(new Timestamp(new Date())+"."+medName)
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

    // Get all med

    public MutableLiveData<ArrayList<Medicine>> getMedicineArrayList() {
        medRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                ArrayList<Medicine> medicineList = new ArrayList<>();

                if (!value.equals(null)){
                    for (QueryDocumentSnapshot doc : value) {
                        if (doc != null) {
                            medicineList.add(doc.toObject(Medicine.class));
                            //Log.d("EXIST", "ada je = " + medicineArrayList.size());
                        }
                    }
                    medicineArrayList.postValue(medicineList);
                }

            }
        });
        //Log.d("MED_COUNT", "This is = " + medicineArrayList.size());
        return medicineArrayList;
    }
}
