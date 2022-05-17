package com.example.ppswe.repo;

import android.app.Application;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.example.ppswe.model.user.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Objects;

public class AuthRepository {

    private Application application;
    private MutableLiveData<FirebaseUser> firebaseUserMutableLiveData;
    private MutableLiveData<Boolean> userLoggedMutableLiveData;
    private MutableLiveData<String> userRolesMutableLiveData;

    private DocumentReference userRef;
    private FirebaseAuth auth;
    private FirebaseFirestore firestore;

    private String uid;

    public AuthRepository(Application application) {
        this.application = application;
        firebaseUserMutableLiveData = new MutableLiveData<>();
        userLoggedMutableLiveData = new MutableLiveData<>();
        userRolesMutableLiveData = new MutableLiveData<>();

        // Init firebase auth and firestore
        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        if(auth.getCurrentUser() != null){
            uid = auth.getUid();
            firebaseUserMutableLiveData.postValue(auth.getCurrentUser());

            // User doc reference
            userRef = firestore.collection("users")
                    .document(uid);
        }
    }

    public MutableLiveData<FirebaseUser> getFirebaseUserMutableLiveData() {
        return firebaseUserMutableLiveData;
    }

    public MutableLiveData<Boolean> getUserLoggedMutableLiveData() {
        return userLoggedMutableLiveData;
    }

    public void register(String username, String email, String password, String phoneNum, String roles){
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    firebaseUserMutableLiveData.postValue(auth.getCurrentUser());

                    User user = new User(username, email, phoneNum, roles);

                    firestore.collection("users")
                            .document(uid)
                            .set(user)
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
                } else {
                    Toast.makeText(application, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void login(String email, String pass){
        auth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    firebaseUserMutableLiveData.postValue(auth.getCurrentUser());
                } else {
                    Toast.makeText(application, Objects.requireNonNull(task.getException()).getMessage(), Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    public void signOut(){
        auth.signOut();
        userLoggedMutableLiveData.postValue(true);
    }
}
