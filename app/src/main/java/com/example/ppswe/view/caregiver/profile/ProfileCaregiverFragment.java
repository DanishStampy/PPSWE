package com.example.ppswe.view.caregiver.profile;

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
import com.example.ppswe.viewmodel.UserViewModel;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.firebase.auth.FirebaseAuth;

import java.util.List;

public class ProfileCaregiverFragment extends Fragment {

    private TextView tvUsername, tvEmail, tvPhoneNum;
    private ImageButton btnImgLogout;
    private NavController navController;
    private MaterialAlertDialogBuilder builder;

    private UserViewModel userViewModel;

    private FirebaseAuth auth;

    User user;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        auth = FirebaseAuth.getInstance();

        if(getActivity().getApplication() != null) {
            userViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication()))
                    .get(UserViewModel.class);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile_caregiver, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvUsername = view.findViewById(R.id.tvUsernameProfile_caregiver);
        tvEmail = view.findViewById(R.id.tvEmailProfile_caregiver);
        tvPhoneNum = view.findViewById(R.id.tvPhoneNumProfile_caregiver);

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

        btnImgLogout = view.findViewById(R.id.imgBtnLogOut_profile_caregiver);
        builder = new MaterialAlertDialogBuilder(getActivity());

        btnImgLogout.setOnClickListener(view1 -> {
            builder.setTitle("Logout")
                    .setMessage("Are you sure want to logout?")
                    .setCancelable(true)
                    .setPositiveButton("Yes", (dialogInterface, i) -> {
                        auth.signOut();
                        startActivity(new Intent(getActivity(), LoginActivity.class));
                    })
                    .setNegativeButton("Nope", (dialogInterface, i) -> {
                        dialogInterface.cancel();
                    })
                    .show();

        });
    }
}