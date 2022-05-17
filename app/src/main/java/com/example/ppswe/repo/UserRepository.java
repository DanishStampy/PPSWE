package com.example.ppswe.repo;

import android.app.Application;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.ppswe.model.user.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class UserRepository {

    private Application application;
    private MutableLiveData<List<User>> userMutableLiveData;

    private FirebaseAuth auth;
    private FirebaseFirestore firestore;
    private DocumentReference documentReference;

    private String uid;

    public UserRepository(Application application) {
        this.application = application;
        userMutableLiveData = new MutableLiveData<>();

        // Init firebase auth and firestore
        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        if(auth.getCurrentUser() != null) {
            uid = auth.getUid();
            documentReference = firestore.collection("users").document(uid);
        }
    }

    public MutableLiveData<List<User>> getUserMutableLiveData() {

        Log.i("TAG", "getBlogListMutableLiveData: ");
        documentReference.get()
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
}
