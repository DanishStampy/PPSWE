package com.example.ppswe.view.patient.profile;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.ppswe.R;
import com.example.ppswe.model.user.User;
import com.example.ppswe.view.authentication.LoginActivity;
import com.example.ppswe.view.patient.MainMenuActivity;
import com.example.ppswe.viewmodel.UserViewModel;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class ProfileFragment extends Fragment {

    private TextView tvUsername, tvEmail, tvPhoneNum, tvPersonalInfo;
    private ImageButton btnImgLogout, btnImgPersonalInfo;
    private NavController navController;
    private MaterialAlertDialogBuilder builder;

    private UserViewModel userViewModel;

    private FirebaseAuth auth;

    User user;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getActivity().getApplication() != null) {
            userViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication()))
                    .get(UserViewModel.class);
        }

        auth = FirebaseAuth.getInstance();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvUsername = view.findViewById(R.id.tvUsernameProfile);
        tvEmail = view.findViewById(R.id.tvEmailProfile);
        tvPhoneNum = view.findViewById(R.id.tvPhoneNumProfile);

        navController = Navigation.findNavController(view);

        try {

            userViewModel.getUserMutableLiveData().observe(getActivity(), new Observer<List<User>>() {
                @Override
                public void onChanged(List<User> users) {
                    user = new User();
                    user = users.get(0);

                    Log.d("user_profile", user.getUsername());

                    tvUsername.setText(user.getUsername());
                    tvEmail.setText(user.getEmail());
                    tvPhoneNum.setText(user.getPhoneNumber());
                }
            });

        } catch (NullPointerException e) {
            Log.d("NULL_POINTER", e.getMessage());
        }


        btnImgPersonalInfo = view.findViewById(R.id.imgBtnLatestVitalSign);
        btnImgPersonalInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navController.navigate(R.id.action_profileFragment_to_personalInfoFragment);
            }
        });


        btnImgLogout = view.findViewById(R.id.imgBtnLogOut);
        builder = new MaterialAlertDialogBuilder(getActivity());

        btnImgLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                builder.setTitle("Logout")
                        .setMessage("Are you sure want to logout?")
                        .setCancelable(true)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                auth.signOut();
                                startActivity(new Intent(getActivity(), LoginActivity.class));
                                getActivity().finish();
                            }
                        })
                        .setNegativeButton("Nope", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        })
                        .show();
            }
        });
    }
}
