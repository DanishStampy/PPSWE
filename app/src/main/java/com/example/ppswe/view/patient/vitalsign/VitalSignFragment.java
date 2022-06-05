package com.example.ppswe.view.patient.vitalsign;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.ppswe.R;
import com.example.ppswe.model.vitalsign.SingletonVitalSign;
import com.example.ppswe.model.vitalsign.VitalSign;
import com.example.ppswe.viewmodel.VitalViewModel;
import com.google.android.material.slider.LabelFormatter;
import com.google.android.material.slider.Slider;

import java.util.ArrayList;
import java.util.List;

public class VitalSignFragment extends Fragment {

    private Slider sliderBodyTemp;
    private Button btnCheckResult;
    private EditText etSystolicBP, etDiastolicBP, etPulseRate, etRespirationRate;
    private NavController navController;
    List<Double> BPrate;
    double systolicBP, diastolicBP, bodyTemp, pulseRate, respirationRate;

    private VitalSign vitalSign;
    private VitalViewModel vitalViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getActivity().getApplication() != null){
            vitalViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication()))
                    .get(VitalViewModel.class);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_vital_sign, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Init singleton
        SingletonVitalSign singletonVitalSign = SingletonVitalSign.getInstance();

        if (getArguments() != null) {
            vitalSign = getArguments().getParcelable("added_BMI");
        }

        etSystolicBP = view.findViewById(R.id.etSystolicBP);
        etDiastolicBP = view.findViewById(R.id.etDiastolicBP);
        etPulseRate = view.findViewById(R.id.etPulseRate);
        etRespirationRate = view.findViewById(R.id.etRespirationRate);


        // Slider listener
        sliderBodyTemp = view.findViewById(R.id.sliderBodyTemp);
        sliderBodyTemp.setLabelFormatter(value -> value+"°C");

        sliderBodyTemp.addOnSliderTouchListener(new Slider.OnSliderTouchListener() {
            @SuppressLint("RestrictedApi")
            @Override
            public void onStartTrackingTouch(@NonNull Slider slider) { }

            @SuppressLint("RestrictedApi")
            @Override
            public void onStopTrackingTouch(@NonNull Slider slider) {
                bodyTemp = sliderBodyTemp.getValue();
            }
        });

        // Navigate to next page
        navController = Navigation.findNavController(view);
        btnCheckResult = view.findViewById(R.id.btnSubmitResultVitalSign);
        btnCheckResult.setOnClickListener(view1 -> {
            try {

                systolicBP = Double.parseDouble(etSystolicBP.getText().toString());
                diastolicBP = Double.parseDouble(etDiastolicBP.getText().toString());
                pulseRate = Double.parseDouble(etPulseRate.getText().toString());
                respirationRate = Double.parseDouble(etRespirationRate.getText().toString());

                BPrate = new ArrayList<>();

                if (validateInfo(systolicBP, diastolicBP, pulseRate, respirationRate)) {

                    BPrate.add(systolicBP);
                    BPrate.add(diastolicBP);

                    vitalSign.setBPrate(BPrate);
                    vitalSign.setBodyTemperature(bodyTemp);
                    vitalSign.setPulseRate(pulseRate);
                    vitalSign.setRespirationRate(respirationRate);

                    vitalViewModel.writeVitalSign(vitalSign);

                    Bundle bundle = new Bundle();
                    bundle.putParcelable("vital_sign_data", vitalSign);

                    navController.navigate(R.id.action_vitalSignFragment_to_vitalSign_ResultFragment, bundle);
                } else {
                    return;
                }

            } catch (NumberFormatException e) {
                Log.d("ERR", e.getMessage());
            }

        });

    }

    // Input validation
    private Boolean validateInfo(double systolicBP, double diastolicBP, double pulseRate, double respirationRate) {

        if (systolicBP == 0) {
            etSystolicBP.requestFocus();
            etSystolicBP.setError("Please enter systolic blood pressure.");
            return false;

        } else if (diastolicBP == 0) {
            etDiastolicBP.requestFocus();
            etDiastolicBP.setError("Please enter diastolic blood pressure.");
            return false;

        } else if (pulseRate == 0) {
            etPulseRate.requestFocus();
            etPulseRate.setError("Please enter pulse rate.");
            return false;

        } else if (respirationRate == 0) {
            etRespirationRate.requestFocus();
            etRespirationRate.setError("Please enter the respiration rate.");
            return false;

        } else {
            return true;
        }

    }
}