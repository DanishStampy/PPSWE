package com.example.ppswe.view.patient.medicine;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.ppswe.R;
import com.example.ppswe.adapter.OnAdapterItemClickListener;
import com.example.ppswe.adapter.buttonTimePickerAdapter;
import com.example.ppswe.model.SingletonMedicine;
import com.example.ppswe.view.patient.MainMenuActivity;
import com.example.ppswe.viewmodel.MedViewModel;

import java.util.ArrayList;

public class SubmitMedFragment extends Fragment implements OnAdapterItemClickListener {

    private RecyclerView recyclerViewTimePickerButton;
    private buttonTimePickerAdapter buttonTimePickerAdapter;
    private Button btnSubmitMedData;
    private EditText etMedInstruction, etMedDesc;
    ArrayList<Integer> medTimes = new ArrayList<>();

    private MedViewModel medViewModel;

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
        return inflater.inflate(R.layout.fragment_submit_med, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SingletonMedicine singletonMedicine = SingletonMedicine.getInstance();
        int btnCounter = singletonMedicine.getMedFreq();

        // recyclerview
        recyclerViewTimePickerButton = view.findViewById(R.id.recyclerTime);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerViewTimePickerButton.setLayoutManager(linearLayoutManager);

        // init array list - all zero
        for (int i=0; i<btnCounter; i++){
            medTimes.add(0);
        }

        timePicker();

        etMedInstruction = view.findViewById(R.id.etMedInstruction);
        etMedDesc = view.findViewById(R.id.etMedDescription);

        btnSubmitMedData = view.findViewById(R.id.btnSubmitMedData);
        btnSubmitMedData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String medInstruction = etMedInstruction.getText().toString().trim();
                String medDescription = etMedDesc.getText().toString().trim();

                if (validateInfo(medInstruction, medTimes)) {

                    singletonMedicine.setMedInstruction(medInstruction);
                    singletonMedicine.setMedDesc(medDescription);
                    singletonMedicine.setMedTimes(medTimes);

                    medViewModel.writeMed(singletonMedicine.getMedName(), singletonMedicine.getMedType(), singletonMedicine.getMedDose(), singletonMedicine.getMedFreq(), singletonMedicine.getMedTimes(), singletonMedicine.getMedInstruction(), singletonMedicine.getMedDesc());

                    startActivity(new Intent(getActivity(), MainMenuActivity.class));
                }
            }
        });
    }

    private Boolean validateInfo(String medInstruction, ArrayList<Integer> medTimes) {

        if(medInstruction.isEmpty()) {
            etMedInstruction.requestFocus();
            etMedInstruction.setError("Please enter medicine instruction.");
            return false;

        } else if (medTimes.contains(0)) {
            Toast.makeText(getActivity(), "Please pick the times.", Toast.LENGTH_SHORT).show();
            return false;

        } else {
            return true;
        }
    }

    // method to init adapter
    public void timePicker(){
        buttonTimePickerAdapter = new buttonTimePickerAdapter(this);
        buttonTimePickerAdapter.setMedTimes(medTimes);
        recyclerViewTimePickerButton.setAdapter(buttonTimePickerAdapter);

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onAdapterItemClickListener(int position) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        Toast.makeText(getContext(), "deez"+position, Toast.LENGTH_SHORT).show();

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

            Toast.makeText(getActivity(), "Hour : " +hour+ " Minute : "+minute, Toast.LENGTH_LONG).show();

           medTimes.set(position, time);
           timePicker();
        });

        builder.show();
    }
}