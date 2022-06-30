package com.example.ppswe.view.authentication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ppswe.R;
import com.example.ppswe.adapter.LoadingDialog;
import com.example.ppswe.view.caregiver.CaregiverMainActivity;
import com.example.ppswe.view.patient.MainMenuActivity;
import com.example.ppswe.viewmodel.UserViewModel;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {

    private EditText etEmail, etPassword;
    private Button btnLogin;
    private TextView tvRegister;

    //private AuthViewModel authViewModel;
    private FirebaseFirestore firestore;
    private FirebaseAuth auth;

    private LoadingDialog loadingDialog;

    String userRoles;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Init firestore
        firestore = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        // init loading dialog
        loadingDialog = new LoadingDialog(this);

        etEmail = findViewById(R.id.etLoginEmail);
        etPassword = findViewById(R.id.etLoginPassword);

        btnLogin = findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(view -> {
            loadingDialog.showDialog();

            String email = etEmail.getText().toString();
            String password = etPassword.getText().toString();

            auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()){

                            firestore.collection("users")
                                    .document(auth.getUid())
                                    .get()
                                    .addOnSuccessListener(documentSnapshot -> {
                                        loadingDialog.hideDialog();
                                        userRoles = documentSnapshot.getString("roles");
                                        if (userRoles.equals("patient")){
                                            showPatientActivity();
                                        } else {
                                            showCaregiverActivity();
                                        }
                                    })
                                    .addOnFailureListener(e -> Toast.makeText(LoginActivity.this, "Something went wrong!", Toast.LENGTH_SHORT).show());

                        } else {
                            loadingDialog.hideDialog();
                            Toast.makeText(LoginActivity.this, "User account doesn't exist! Please register one new account.", Toast.LENGTH_SHORT).show();
                        }
                    });
        });

        tvRegister = findViewById(R.id.tvRegisterHere);
        tvRegister.setOnClickListener(view -> showRegisterActivity());
    }

    private void showPatientActivity(){
        Intent intent = new Intent(this, MainMenuActivity.class);
        startActivity(intent);
        finish();
    }

    private void showCaregiverActivity(){
        Intent intent = new Intent(this, CaregiverMainActivity.class);
        startActivity(intent);
        finish();
    }

    private void showRegisterActivity(){
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }
}