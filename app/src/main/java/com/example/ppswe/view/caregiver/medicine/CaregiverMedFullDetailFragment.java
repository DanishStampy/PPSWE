package com.example.ppswe.view.caregiver.medicine;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ppswe.R;
import com.example.ppswe.model.medicine.Medicine;
import com.example.ppswe.viewmodel.MedViewModel;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.List;

public class CaregiverMedFullDetailFragment extends Fragment {

    private TextView tvMedName, tvMedTime, tvMedType, tvMedInfo;
    private Button btnUpdateMed, btnDeleteMed;

    private NavController navController;
    private MaterialAlertDialogBuilder builder;

    private Medicine medicine;
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
        return inflater.inflate(R.layout.fragment_caregiver_med_full_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        tvMedName = view.findViewById(R.id.tvCaregiverDetailMedName);
        tvMedTime = view.findViewById(R.id.tvCaregiverDetailMedTime);
        tvMedType = view.findViewById(R.id.tvCaregiverDetailMedType);
        tvMedInfo = view.findViewById(R.id.tvCaregiverMedDetailInfo_intrusction_dose);
        navController = Navigation.findNavController(view);

        if ( getArguments() != null) {
            medicine = getArguments().getParcelable("medicine_data");

            String medInfo = "Take " + medicine.getMedDose() + " dose(s), " + medicine.getMedInstruction() + " everyday";
            String listMedTimes = getListMedTimes(medicine.getMedTimes());

            tvMedName.setText(medicine.getMedName());
            tvMedType.setText(medicine.getMedType());
            tvMedInfo.setText(medInfo);
            tvMedTime.setText(listMedTimes);
            builder = new MaterialAlertDialogBuilder(getActivity());

            // Button
            btnDeleteMed = view.findViewById(R.id.btnCaregiverDeleteDetailMed);
            btnDeleteMed.setOnClickListener(view1 ->
                    builder.setTitle("Delete medicine info")
                    .setMessage("Are you sure want to delete this medicine?")
                    .setCancelable(true)
                    .setPositiveButton("Yes", (dialogInterface, i) -> {
                        medViewModel.deleteCaregiverMedData(medicine.getMedId());
                        Toast.makeText(getActivity(), "Medicine has been successfully deleted! " + medicine.getMedId(), Toast.LENGTH_SHORT).show();
                        navController.navigate(R.id.action_caregiverMedFullDetailFragment_to_caregiverListAllMedicineFragment);
                    })
                    .setNegativeButton("Nope", (dialogInterface, i) -> dialogInterface.cancel())
                    .show());

            btnUpdateMed = view.findViewById(R.id.btnCaregiverUpdateDetailMed);
            btnUpdateMed.setOnClickListener(view12 -> {
                Bundle bundle = new Bundle();
                bundle.putParcelable("update_medicine_caregiver_data", medicine);
                navController.navigate(R.id.action_caregiverMedFullDetailFragment_to_caregiverUpdateMedFragment, bundle);
            });
        }
    }

    private String getListMedTimes(List<Integer> medTimes) {
        int timesCount = medTimes.size();
        String result = "";

        for (int i = 0; i < timesCount; i++) {
            String am_pm = "AM";

            int medTime = medTimes.get(i);

            double dhour = (double) medTime / 60 / 60;
            double dminute = dhour % 1 * 60;

            int hour = medTime / 60 / 60;

            if (hour > 12) {
                am_pm = "PM";
                hour = hour - 12;
            } else if (hour == 12) {
                am_pm = "PM";
            } else {
                am_pm = "AM";
            }

            if ((int) Math.round(dminute) < 10) {
                result += hour + ":0" + (int) Math.round(dminute) + " " + am_pm;
            } else {
                result += hour + ":" + (int) Math.round(dminute) + " " + am_pm;
            }

            if ( i != (timesCount-1)) {
                result += ", ";
            }
        }
        return result;
    }
}