package com.example.ppswe.view.caregiver.medicine;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.ppswe.R;
import com.example.ppswe.model.medicine.Medicine;

import java.util.Arrays;

public class CaregiverUpdateMedFragment extends Fragment {

    private EditText etUpdateMedName, etUpdateMedDose, etUpdateMedFreq;
    private Button btnNextFragment;
    private TextView tvUpdateMedType;

    private NavController navController;

    ArrayAdapter<String> adapter_medType;

    private Medicine medicine;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_caregiver_update_med, container, false);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);

        etUpdateMedName = view.findViewById(R.id.etUpdateCaregiverMedName);
        etUpdateMedDose = view.findViewById(R.id.etUpdateCaregiverMedDose);
        etUpdateMedFreq = view.findViewById(R.id.etUpdateCaregiverFreqMed);

        tvUpdateMedType = view.findViewById(R.id.tvUpdateCaregiverMedType_Dose);

        if (getArguments() != null) {
            medicine = getArguments().getParcelable("update_medicine_caregiver_data");

            etUpdateMedName.setText(medicine.getMedName());
            etUpdateMedDose.setText(""+medicine.getMedDose());
            etUpdateMedFreq.setText(""+medicine.getMedFreq());

            tvUpdateMedType.setText(medicine.getMedType());

            // Dropdown item
            String[] type = new String[] {"Tablet", "Powder", "Solution", "Drops", "Inhaler", "Injection", "Others"};
            adapter_medType = new ArrayAdapter<>(
                    getContext(),
                    R.layout.med_type_dropdown,
                    type
            );
            int index = Arrays.asList(type).indexOf(medicine.getMedType());

            // Dropdown adapter
            AutoCompleteTextView medType = view.findViewById(R.id.dropdown_update_caregiver_medType);
            medType.setAdapter(adapter_medType);
            medType.setText(adapter_medType.getItem(index), false);

            medType.setOnItemClickListener((adapterView, view1, i, l) -> {
                tvUpdateMedType.setText(medType.getText().toString());
                medicine.setMedType(medType.getText().toString());
            });
        }

        btnNextFragment = view.findViewById(R.id.btnNextFragment_submit_update_caregiver);
        btnNextFragment.setOnClickListener(view12 -> {
            medicine.setMedName(etUpdateMedName.getText().toString().trim());
            medicine.setMedDose(Integer.parseInt(etUpdateMedDose.getText().toString()));
            medicine.setMedFreq(Integer.parseInt(etUpdateMedFreq.getText().toString()));

            Bundle bundle = new Bundle();
            bundle.putParcelable("new_update_caregiver_data", medicine);

            navController.navigate(R.id.action_caregiverUpdateMedFragment_to_caregiverSubmitUpdateMedFragment, bundle);
        });
    }
}