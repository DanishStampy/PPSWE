package com.example.ppswe.view.medicine;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.ppswe.R;
import com.example.ppswe.model.Medicine;

public class MedFormFragment extends Fragment {

    ArrayAdapter<String> adapter_medType;
    ArrayAdapter<String> adapter_medFreq;

    private TextView tvMedTypeDose;
    private EditText etNameMed, etDoseMed, etFreqMed;
    private Button btnNextFragment;
    private NavController navController;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_med_form, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvMedTypeDose = view.findViewById(R.id.tvMedType_Dose);
        navController = Navigation.findNavController(view);

        // Dropdown item
        String[] type = new String[] {"Tablet", "Powder", "Solution", "Drops", "Inhaler", "Injection", "Others"};
        adapter_medType = new ArrayAdapter<>(
                getContext(),
                R.layout.med_type_dropdown,
                type
        );

        String[] frequency = new String[] {"per day", "per two day", "per week"};
        adapter_medFreq = new ArrayAdapter<>(
                getContext(),
                R.layout.med_freq_dropdown,
                frequency
        );

        // Dropdown adapter
        AutoCompleteTextView medType = view.findViewById(R.id.dropdown_medType);
        medType.setAdapter(adapter_medType);

        medType.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                tvMedTypeDose.setText( medType.getText().toString());
                Medicine.setMedType(medType.getText().toString());
            }
        });

        AutoCompleteTextView medFreqDay = view.findViewById(R.id.dropdown_freqMed);
        medFreqDay.setAdapter(adapter_medFreq);

        medFreqDay.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

            }
        });

        etFreqMed = view.findViewById(R.id.etFreqMed);
        etDoseMed = view.findViewById(R.id.etMedDose);
        etNameMed = view.findViewById(R.id.etMedName);

        // Button for next fragment
        btnNextFragment = view.findViewById(R.id.btnNextFragment_submit);
        btnNextFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Medicine.setMedName(etNameMed.getText().toString().trim());
                Medicine.setMedDose(Integer.parseInt(etDoseMed.getText().toString()));
                Medicine.setMedFreq(Integer.parseInt(etFreqMed.getText().toString()));

                navController.navigate(R.id.action_medFormFragment_to_submitMedFragment);
            }
        });

    }
}