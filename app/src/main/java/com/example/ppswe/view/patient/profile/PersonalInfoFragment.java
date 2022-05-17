package com.example.ppswe.view.patient.profile;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.ppswe.R;
import com.example.ppswe.model.vitalsign.VitalSign;
import com.example.ppswe.viewmodel.VitalViewModel;

import java.util.ArrayList;

public class PersonalInfoFragment extends Fragment {

    private TextView tvBodyTemperature, tvPulseRate, tvWeight, tvHeight, tvBMI, tvBloodPressure, tvRespiratoryRate;

    private VitalViewModel vitalViewModel;

    VitalSign vital;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        vitalViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication()))
                .get(VitalViewModel.class);
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

        tvWeight = view.findViewById(R.id.tvWeight);
        tvHeight = view.findViewById(R.id.tvHeight);
        tvBMI = view.findViewById(R.id.tvBMI);

        tvBodyTemperature = view.findViewById(R.id.tvBodyTemp);
        tvBloodPressure = view.findViewById(R.id.tvBP);
        tvRespiratoryRate = view.findViewById(R.id.tvRespiratoryRate);
        tvPulseRate = view.findViewById(R.id.tvPulseRate);


        vitalViewModel.getVitalSignData().observe(getActivity(), new Observer<ArrayList<VitalSign>>() {
            @SuppressLint({"SetTextI18n", "DefaultLocale"})
            @Override
            public void onChanged(ArrayList<VitalSign> vitalSigns) {
                vital = new VitalSign();
                vital = vitalSigns.get(0);

                tvWeight.setText(vital.getWeight() + " kg");
                tvHeight.setText(vital.getHeight() + " Metres(m)");
                tvBMI.setText( String.format("%.2f", vital.getBMI()) + " kg/m2");

                tvBodyTemperature.setText( String.format("%.1f", vital.getBodyTemperature()) + "°C");
                tvBloodPressure.setText( vital.getBPrate().get(0).intValue() + "/" + vital.getBPrate().get(1).intValue() + " mmHg");
                tvPulseRate.setText( (int)vital.getPulseRate() + " pulse/min");
                tvRespiratoryRate.setText( (int)vital.getRespirationRate() + " breath/min");
            }
        });
    }
}