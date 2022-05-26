package com.example.ppswe.repo;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.ppswe.model.user.SingletonStatusPatient;
import com.example.ppswe.model.user.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class UserRepository {

    private Application application;
    private MutableLiveData<List<User>> userMutableLiveData;

    private FirebaseAuth auth;
    private FirebaseFirestore firestore;
    private DocumentReference userRef;
    private CollectionReference userCollection;
    private SingletonStatusPatient singletonStatusPatient;

    private String uid;

    public UserRepository(Application application) {
        this.application = application;
        userMutableLiveData = new MutableLiveData<>();

        // Init firebase auth and firestore
        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
        singletonStatusPatient = SingletonStatusPatient.getInstance();

        if(auth.getCurrentUser() != null) {
            uid = auth.getUid();
            userCollection = firestore.collection("users");
            userRef = firestore.collection("users").document(uid);
        }
    }

    public MutableLiveData<List<User>> getUserMutableLiveData() {

        Log.i("TAG", "getBlogListMutableLiveData: ");
        userRef.get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()){
                            List<User> userList = new ArrayList<>();
                            userList.add(documentSnapshot.toObject(User.class));
                            userMutableLiveData.postValue(userList);
                        } else {
                            Log.d("NOT_EXISTS", "Document didnt exist.");
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("ERR", "Something went wrong.");
                    }
                });
        return userMutableLiveData;
    }

    public void setPatientEmailSingleton() {
        userRef.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot documentSnapshot = task.getResult();
                        singletonStatusPatient.setPatientEmail(documentSnapshot.getString("patientEmail"));
                        singletonStatusPatient.setPatientName(" ");
                        Log.d("EMAIL_PATIENT", "This is it " + singletonStatusPatient.getPatientEmail().equals("empty")); // true
                    }
                });

        if (!("empty".equals(singletonStatusPatient.getPatientEmail()))) {
            userCollection.whereEqualTo("email", singletonStatusPatient.getPatientEmail())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot doc : task.getResult()) {
                                    singletonStatusPatient.setPatientName(doc.getString("username"));
                                }
                            }
                        }
                    });
        }
    }
}
