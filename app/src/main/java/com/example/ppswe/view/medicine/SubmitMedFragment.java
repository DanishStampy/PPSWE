package com.example.ppswe.view.medicine;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.ppswe.R;
import com.example.ppswe.model.Medicine;

public class SubmitMedFragment extends Fragment {

    private LinearLayout linearLayoutTime;
    private Button btnSubmitMedData, newButton;
    private EditText etMedInstruction, etMedDesc;
    int hour, minute;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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

        int btnCounter = Medicine.getMedFreq();

        linearLayoutTime = view.findViewById(R.id.linearLayoutTime);

        for (int i=0 ; i<btnCounter ; i++) {
            newButton = new Button(getActivity());
            newButton.setId(i);
            newButton.setText("Number = " +i);
            linearLayoutTime.addView(newButton);
        }

        etMedInstruction = view.findViewById(R.id.etMedInstruction);
        etMedInstruction = view.findViewById(R.id.etMedDescription);

        btnSubmitMedData = view.findViewById(R.id.btnSubmitMedData);
        btnSubmitMedData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}