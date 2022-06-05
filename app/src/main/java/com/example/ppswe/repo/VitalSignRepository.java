package com.example.ppswe.repo;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.ppswe.model.vitalsign.VitalSign;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class VitalSignRepository {

    private Application application;
    private MutableLiveData<ArrayList<VitalSign>> vitalSignMutableLiveData;

    private FirebaseAuth auth;
    private FirebaseFirestore firestore;
    private DocumentReference documentReference;
    private String uid;

    public VitalSignRepository(Application application) {
        this.application = application;
        vitalSignMutableLiveData = new MutableLiveData<>();

        // Init firebase auth and firestore
        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        // Vital Sign collection reference
        if (auth.getCurrentUser() != null) {
            uid = auth.getUid();
            documentReference = firestore.collection("vitalSigns")
                    .document(uid);
        }

    }

    public void writeVitalSign (VitalSign vitalSign) {

        SimpleDateFormat format = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
        Date date = new Date();

        vitalSign.setDate(format.format(date));

        firestore.collection("vitalSigns")
                .document(uid)
                .set(vitalSign)
                .addOnSuccessListener(unused -> Log.d("Success", "Document successfully written!"))
                .addOnFailureListener(e -> Log.w("FAIL", "Error writing doc", e));
    }

    public MutableLiveData<ArrayList<VitalSign>> getVitalSignMutableLiveData() {
        documentReference.get()
                .addOnSuccessListener(documentSnapshot -> {

                    ArrayList<VitalSign> vitalList = new ArrayList<>();
                    if (documentSnapshot.exists()){

                        vitalList.add(documentSnapshot.toObject(VitalSign.class));

                        Log.d("VITAL_SIGN", "This is " + Objects.requireNonNull(documentSnapshot.getLong("bodyTemperature")).intValue());
                    } else {
                        Log.d("NOT_EXISTS", "Document didnt exist.");
                    }
                    vitalSignMutableLiveData.postValue(vitalList);
                })
                .addOnFailureListener(e -> Log.d("ERR", "Something went wrong."));
        return vitalSignMutableLiveData;
    }
}
