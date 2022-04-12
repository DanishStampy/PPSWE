package com.example.ppswe.view.authentication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import com.example.ppswe.R;
import com.example.ppswe.view.MainMenuActivity;
import com.example.ppswe.view.authentication.LoginActivity;
import com.example.ppswe.viewmodel.AuthViewModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class RegisterActivity extends AppCompatActivity {

    private EditText etUsername, etEmail, etPhoneNum, etPassword, etConfirmPass;
    private Button btnRegister;
    private TextView tvLogin;
    private RadioButton radioPatient, radioCaregiver;

    private AuthViewModel authViewModel;

    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        authViewModel = new ViewModelProvider.AndroidViewModelFactory(this.getApplication()).create(AuthViewModel.class);

        // Init firebase auth
        authViewModel.getUserData().observe(this, new Observer<FirebaseUser>() {
            @Override
            public void onChanged(FirebaseUser firebaseUser) {
                if (firebaseUser != null){
                    Log.d("FIREBASE_USER", firebaseUser.getEmail());
                    showMainActivity();
                }
            }
        });

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
                String phoneNum = etPhoneNum.getText().toString();
                String password = etPassword.getText().toString();
                String confirmPassword = etConfirmPass.getText().toString();
                String roles = radioPatient.isChecked() ? "patient" : "caregiver";

                validation(email, phoneNum, password, confirmPassword);

                authViewModel.register(username, email, password, phoneNum, roles);
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

    private void showLogInActivity(){
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    private boolean validation(String email, String phoneNum, String password, String confirmPass){

        if (password != confirmPass) {
            return false;
        }

        return true;
    }

}