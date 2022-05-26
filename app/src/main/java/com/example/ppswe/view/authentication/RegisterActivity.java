package com.example.ppswe.view.authentication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.ppswe.R;
import com.example.ppswe.model.user.User;
import com.example.ppswe.view.caregiver.CaregiverMainActivity;
import com.example.ppswe.view.patient.MainMenuActivity;
import com.example.ppswe.viewmodel.AuthViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

public class RegisterActivity extends AppCompatActivity {

    private EditText etUsername, etEmail, etPhoneNum, etPassword, etConfirmPass;
    private Button btnRegister;
    private TextView tvLogin;
    private RadioButton radioPatient, radioCaregiver;

    private AuthViewModel authViewModel;

    private FirebaseAuth auth;
    private FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        etUsername = findViewById(R.id.etUsername);
        etEmail = findViewById(R.id.etEmail);
        etPhoneNum = findViewById(R.id.etPhoneNumber);
        etPassword = findViewById(R.id.etPassword);
        etConfirmPass = findViewById(R.id.etConfirmPassword);
        radioPatient = findViewById(R.id.radioBtnPatient);
        radioCaregiver = findViewById(R.id.radioBtnCaregiver);

        btnRegister = findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String username = etUsername.getText().toString();
                String email = etEmail.getText().toString();
                String phoneNum = etPhoneNum.getText().toString().trim();
                String password = etPassword.getText().toString().trim();
                String confirmPassword = etConfirmPass.getText().toString().trim();
                String roles = radioPatient.isChecked() ? "patient" : "caregiver";

                if (validation(username, email, phoneNum, password, confirmPassword)){

                    auth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()){
                                        User user = new User(username, email, phoneNum, roles);
                                        if (roles.equals("caregiver")) {
                                            user.setPatientEmail("empty");
                                        }
                                        String uid = auth.getUid();

                                        firestore.collection("users")
                                                .document(uid)
                                                .set(user)
                                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                    @Override
                                                    public void onSuccess(Void unused) {
                                                        if (roles.equals("patient")){
                                                            showMainActivity();
                                                        } else {
                                                            showCaregiverActivity();
                                                        }

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
                            });

                } else {
                    return;
                }

            }
        });

        tvLogin = findViewById(R.id.tvLoginHere);
        tvLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showLogInActivity();
            }
        });


    }

    private void showMainActivity(){
        Intent intent = new Intent(this, MainMenuActivity.class);
        startActivity(intent);
    }

    private void showCaregiverActivity(){
        Intent intent = new Intent(this, CaregiverMainActivity.class);
        startActivity(intent);
    }

    private void showLogInActivity(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    private boolean validation(String username, String email, String phoneNum, String password, String confirmPass){

        if (password.length() < 6) {
            etConfirmPass.requestFocus();
            etConfirmPass.setError("Please enter more than 6 characters.");
            return false;

        } else if (email.isEmpty()) {
            etEmail.requestFocus();
            etEmail.setError("Please enter the email.");
            return false;

        } else if (phoneNum.isEmpty()) {
            etPhoneNum.requestFocus();
            etPhoneNum.setError("Please enter the phone number.");
            return false;

        } else if (username.isEmpty()){
            etUsername.requestFocus();
            etUsername.setError("Please enter the username.");
            return false;

        } else if (password.isEmpty()) {
            etPassword.requestFocus();
            etPassword.setError("Please enter the password.");
            return false;
        }

        return true;
    }

}