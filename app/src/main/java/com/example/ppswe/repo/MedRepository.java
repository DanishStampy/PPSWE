package com.example.ppswe.repo;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;

import com.example.ppswe.model.Medicine;
import com.example.ppswe.model.MedicineView;
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
    private MutableLiveData<ArrayList<MedicineView>> medicineArrayList;

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

    public MutableLiveData<ArrayList<MedicineView>> getMedicineArrayList() {
        medRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                ArrayList<MedicineView> medicineList = new ArrayList<>();
                ArrayList<Integer> medicineTime;

                if (!value.equals(null)){
                    for (QueryDocumentSnapshot doc : value) {
                        if (doc != null) {
                            medicineTime = (ArrayList<Integer>) doc.get("medTimes");
                            Log.d("MED_TIMES", medicineTime.toString());

                            for (int i = 0 ; i < medicineTime.size() ; i++){
                                MedicineView medicineView = new MedicineView(doc.getString("medName"), doc.getString("medInstruction"), doc.getLong("medDose").intValue(), doc.getString("medType") );

                                // Cast from long into integer
                                medicineView.setMedTime(((Number)medicineTime.get(i)).intValue());
                                //Log.d("DATA_TYPE", "Data type = " + medicineTime.get(i).getClass().getSimpleName());
                                medicineList.add(medicineView);
                            }

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
