package com.example.ppswe.view.patient.vitalsign;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.ppswe.R;
import com.example.ppswe.model.SingletonVitalSign;

public class Weight_HeightFragment extends Fragment {

    private EditText etHeight, etWeight;
    private TextView tvBMIResult;
    private Button btnCalculateBMI, btnNextFragment_vitalSign;
    private NavController navController;
    double height, weight;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_weight__height, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Init singleton
        SingletonVitalSign singletonVitalSign = SingletonVitalSign.getInstance();

        etHeight = view.findViewById(R.id.etHeightPatient);
        etWeight = view.findViewById(R.id.etWeightPatient);
        tvBMIResult = view.findViewById(R.id.tvBMIResult);

        btnCalculateBMI = view.findViewById(R.id.btnCalculateBMI);
        btnNextFragment_vitalSign = view.findViewById(R.id.btnNextFragment_VitalSign);
        navController = Navigation.findNavController(view);

        // Calculate BMI
        btnCalculateBMI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                try {
                    height = Double.parseDouble(etHeight.getText().toString().trim());
                    weight = Double.parseDouble(etWeight.getText().toString().trim());

                    if (validateInfo(etHeight.getText().toString(), etWeight.getText().toString())) {
                        String resultBMI = calculateBMI(height, weight);
                        tvBMIResult.setText(resultBMI);
                    } else {
                        return;
                    }
                } catch (NumberFormatException e) {
                    Log.d("NUMBER_ERR", e.getMessage());
                }
            }
        });

        // Next page
        btnNextFragment_vitalSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (validateInfo(etHeight.getText().toString(), etWeight.getText().toString())){

                    singletonVitalSign.setHeight(height);
                    singletonVitalSign.setWeight(weight);

                    navController.navigate(R.id.action_weight_HeightFragment_to_vitalSign_ReadyFragment);
                } else {
                    return;
                }

            }
        });

    }

    private String calculateBMI(double height, double weight) {

        double bmi = weight / (height * height);

        if (bmi < 18.5) {
            return "Underweight";
        } else if (bmi >= 18.5 && bmi <= 24.9) {
            return "Normal";
        } else if (bmi >= 25.0 && bmi <= 29.9) {
            return "Overweight";
        } else {
            return "Obese";
        }
    }

    // Input validation
    private Boolean validateInfo(String height, String weight) {

        if (height.isEmpty()) {
            etHeight.requestFocus();
            etHeight.setError("Please enter height before calculate.");
            return false;

        } else if (weight.isEmpty()) {
            etWeight.requestFocus();
            etWeight.setError("Please enter weight before calculate.");
            return false;

        } else {
            return true;
        }

    }


}