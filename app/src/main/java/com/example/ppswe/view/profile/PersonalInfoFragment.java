package com.example.ppswe.view.profile;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ppswe.R;
import com.example.ppswe.model.VitalSign;
import com.example.ppswe.viewmodel.UserViewModel;
import com.example.ppswe.viewmodel.VitalViewModel;

import java.util.ArrayList;

public class PersonalInfoFragment extends Fragment {

    private TextView tvBodyTemperature, tvPulseRate, tvWeight;

    private VitalViewModel vitalViewModel;

    VitalSign vital;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        vitalViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication()))
//                .get(VitalViewModel.class);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_personal_info, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvBodyTemperature = view.findViewById(R.id.tvBodyTemp);
        tvPulseRate = view.findViewById(R.id.tvPulseRate);
        tvWeight = view.findViewById(R.id.tvWeight);

//        vitalViewModel.getVitalSignData().observe(getActivity(), new Observer<ArrayList<VitalSign>>() {
//            @Override
//            public void onChanged(ArrayList<VitalSign> vitalSigns) {
//
//            }
//        });
    }
}