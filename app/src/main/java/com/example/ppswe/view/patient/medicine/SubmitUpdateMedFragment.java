package com.example.ppswe.view.patient.medicine;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.ppswe.R;
import com.example.ppswe.adapter.OnAdapterItemClickListener;
import com.example.ppswe.adapter.buttonTimePickerAdapter;
import com.example.ppswe.model.medicine.Medicine;
import com.example.ppswe.view.patient.ListMedicineActivity;
import com.example.ppswe.viewmodel.MedViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

public class SubmitUpdateMedFragment extends Fragment implements OnAdapterItemClickListener {

    private RecyclerView recyclerViewUpdateTimePickerButton;
    private buttonTimePickerAdapter buttonTimePickerAdapter;
    private EditText etUpdateMedDesc;
    private Button btnSubmitUpdateMed;
    private AutoCompleteTextView updateMedInstruction;

    private MedViewModel medViewModel;

    private Medicine medicine;

    ArrayList<Integer> medTimes = new ArrayList<>();
    ArrayList<Integer> list = new ArrayList<>();
    ArrayAdapter<String> adapter_instruction;
    String medInstructChoose;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getActivity().getApplication() != null) {
            medViewModel = new ViewModelProvider(this, ViewModelProvider.AndroidViewModelFactory.getInstance(getActivity().getApplication()))
                    .get(MedViewModel.class);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_submit_update_med, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etUpdateMedDesc = view.findViewById(R.id.etUpdateMedDescription);
        btnSubmitUpdateMed = view.findViewById(R.id.btnSubmitUpdateMedData);

        recyclerViewUpdateTimePickerButton = view.findViewById(R.id.recyclerTime_update);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerViewUpdateTimePickerButton.setLayoutManager(linearLayoutManager);

        // Dropdown item
        String[] instruction = new String[] {"before meal", "after meal", "mix with water", "mix with meal"};
        adapter_instruction = new ArrayAdapter<>(
                getContext(),
                R.layout.med_type_dropdown,
                instruction
        );

        // Dropdown adapter
        updateMedInstruction = view.findViewById(R.id.dropdown_update_medInstruction);
        updateMedInstruction.setAdapter(adapter_instruction);

        updateMedInstruction.setOnItemClickListener((adapterView, view1, i, l) -> {
            medInstructChoose = updateMedInstruction.getText().toString();
        });

        if (getArguments() != null) {
            medicine = getArguments().getParcelable("new_update_data");

            int btnCounter = medicine.getMedFreq();

            // init array list - all zero
            for (int i=0; i<btnCounter; i++){
                medTimes.add(0);
            }

            timePicker();

            int index = Arrays.asList(instruction).indexOf(medicine.getMedInstruction());
            updateMedInstruction.setText(adapter_instruction.getItem(index), false);
            etUpdateMedDesc.setText(medicine.getMedDesc());
        }

        btnSubmitUpdateMed.setOnClickListener(view1 -> {

            medicine.setMedTimes(medTimes);
            medicine.setMedDesc(etUpdateMedDesc.getText().toString());
            medicine.setMedInstruction(medInstructChoose);

            medViewModel.updateMed(medicine);
            Toast.makeText(getActivity(), "Medicine has been updated!", Toast.LENGTH_SHORT).show();

            startActivity(new Intent(getActivity(), ListMedicineActivity.class));
            getActivity().finish();
        });
    }

    // method to init adapter
    public void timePicker(){
        buttonTimePickerAdapter = new buttonTimePickerAdapter(this);
        buttonTimePickerAdapter.setMedTimes(medTimes);
        recyclerViewUpdateTimePickerButton.setAdapter(buttonTimePickerAdapter);

        list = buttonTimePickerAdapter.getMedTimes();
    }

    @SuppressLint("InflateParams")
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onAdapterItemClickListener(int position) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        // Toast.makeText(getContext(), "deez"+position, Toast.LENGTH_SHORT).show();

        View customLayout;

        customLayout = getLayoutInflater().inflate(R.layout.dialog_timepicker, null);
        builder.setView(customLayout);
        TimePicker picker = customLayout.findViewById(R.id.timePicker);
        picker.setIs24HourView(true);

        builder.setPositiveButton("OK", (dialogInterface, i) -> {
            int hour, minute;
            hour = picker.getHour();
            minute = picker.getMinute();

            int time = hour * 60 * 60 + (minute * 60);

            //Toast.makeText(getActivity(), "Hour : " +hour+ " Minute : "+minute, Toast.LENGTH_LONG).show();

            medTimes.set(position, time);
            timePicker();
        });

        builder.show();
    }
}