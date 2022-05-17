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

import java.util.ArrayList;
import java.util.List;

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

    public void writeVitalSign (double height, double weight, List<Double> BPrate, double pulseRate, double respirationRate, double bodyTemp) {
        VitalSign vitalSign = new VitalSign(height, weight, BPrate, pulseRate, respirationRate, bodyTemp);

        firestore.collection("vitalSigns")
                .document(uid)
                .set(vitalSign)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("Success", "Document successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("FAIL", "Error writing doc", e);
                    }
                });
    }

    public MutableLiveData<ArrayList<VitalSign>> getVitalSignMutableLiveData() {
        documentReference.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {

                        ArrayList<VitalSign> vitalList = new ArrayList<>();
                        if (documentSnapshot.exists()){

                            vitalList.add(documentSnapshot.toObject(VitalSign.class));

                            Log.d("VITAL_SIGN", "This is " + documentSnapshot.getLong("bodyTemperature").intValue());
                        } else {
                            Log.d("NOT_EXISTS", "Document didnt exist.");
                        }
                        vitalSignMutableLiveData.postValue(vitalList);
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("ERR", "Something went wrong.");
                    }
                });
        return vitalSignMutableLiveData;
    }
}
