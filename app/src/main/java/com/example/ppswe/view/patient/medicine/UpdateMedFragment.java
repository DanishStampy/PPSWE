package com.example.ppswe.view.patient.medicine;

import android.annotation.SuppressLint;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.ppswe.R;
import com.example.ppswe.model.medicine.Medicine;

import java.util.Arrays;

public class UpdateMedFragment extends Fragment {

    private EditText etUpdateMedName, etUpdateMedDose, etUpdateMedType, etUpdateMedFreq;
    private Button btnNextFragment;
    private TextView tvUpdateMedType;

    private NavController navController;

    ArrayAdapter<String> adapter_medType;
    ArrayAdapter<String> adapter_medFreq;

    private Medicine medicine;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_update_med, container, false);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);

        etUpdateMedName = view.findViewById(R.id.etUpdateMedName);
        etUpdateMedDose = view.findViewById(R.id.etUpdateMedDose);
        etUpdateMedFreq = view.findViewById(R.id.etUpdateFreqMed);

        tvUpdateMedType = view.findViewById(R.id.tvUpdateMedType_Dose);

        if (getArguments() != null) {
            medicine = getArguments().getParcelable("update_medicine_data");

            etUpdateMedName.setText(medicine.getMedName());
            Log.d("MED_DOSE", "dose = " + medicine.getMedDose());
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
            AutoCompleteTextView medType = view.findViewById(R.id.dropdown_update_medType);
            medType.setAdapter(adapter_medType);
            medType.setText(adapter_medType.getItem(index), false);

            medType.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    tvUpdateMedType.setText(medType.getText().toString());
                    medicine.setMedType(medType.getText().toString());
                }
            });
        }

        btnNextFragment = view.findViewById(R.id.btnNextFragment_submit_update);
        btnNextFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                medicine.setMedName(etUpdateMedName.getText().toString().trim());
                medicine.setMedDose(Integer.parseInt(etUpdateMedDose.getText().toString()));
                medicine.setMedFreq(Integer.parseInt(etUpdateMedFreq.getText().toString()));

                Bundle bundle = new Bundle();
                bundle.putParcelable("new_update_data", medicine);

                navController.navigate(R.id.action_updateMedFragment_to_submitUpdateMedFragment, bundle);
            }
        });
    }
}