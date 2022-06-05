package com.example.ppswe.view.patient.vitalsign;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.ppswe.R;
import com.example.ppswe.model.vitalsign.VitalSign;

public class VitalSign_ReadyFragment extends Fragment {

    private Button btnGoToVitalSign_Form;
    private NavController navController;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_vital_sign__ready, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);
        btnGoToVitalSign_Form = view.findViewById(R.id.btnGoToVitalSignForm);

        btnGoToVitalSign_Form.setOnClickListener(view1 -> {

            if (getArguments() != null) {
                VitalSign vitalSign = getArguments().getParcelable("new_vital_sign");

                Bundle bundle = new Bundle();
                bundle.putParcelable("added_BMI", vitalSign);

                navController.navigate(R.id.action_vitalSign_ReadyFragment_to_vitalSignFragment, bundle);
            }
        });


    }
}