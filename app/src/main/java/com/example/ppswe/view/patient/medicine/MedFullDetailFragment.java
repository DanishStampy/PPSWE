package com.example.ppswe.view.patient.medicine;

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
import com.example.ppswe.adapter.LoadingDialog;
import com.example.ppswe.model.medicine.Medicine;
import com.example.ppswe.view.patient.MainMenuActivity;
import com.example.ppswe.viewmodel.MedViewModel;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.List;

public class MedFullDetailFragment extends Fragment {

    private TextView tvMedName, tvMedTime, tvMedType, tvMedInstruction;
    private Button btnUpdateMed, btnDeleteMed;

    private NavController navController;
    private MaterialAlertDialogBuilder builder;
    private LoadingDialog loadingDialog;

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
        return inflater.inflate(R.layout.fragment_med_full_detail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // init loading dialog
        loadingDialog = new LoadingDialog(getContext());

        // TextView
        tvMedName = view.findViewById(R.id.tvDetailMedName);
        tvMedTime = view.findViewById(R.id.tvDetailMedTime);
        tvMedType = view.findViewById(R.id.tvDetailMedType);
        tvMedInstruction = view.findViewById(R.id.tvMedDetailInfo_intrusction_dose);
        navController = Navigation.findNavController(view);

        loadingDialog.showDialog();

        if (getArguments() != null) {
            Medicine medicine = getArguments().getParcelable("medicine_data");

            String medInfo = "Take " + medicine.getMedDose() + " dose(s), " + medicine.getMedInstruction() + " everyday";
            String listMedTimes = getListMedTimes(medicine.getMedTimes());
            loadingDialog.hideDialog();

            tvMedName.setText(medicine.getMedName());
            tvMedType.setText(medicine.getMedType());
            tvMedInstruction.setText(medInfo);
            tvMedTime.setText(listMedTimes);
            builder = new MaterialAlertDialogBuilder(getActivity());

            // Button
            btnDeleteMed = view.findViewById(R.id.btnDeleteDetailMed);
            btnDeleteMed.setOnClickListener(view1 -> builder.setTitle("Delete medicine info")
                    .setMessage("Are you sure want to delete this medicine?")
                    .setCancelable(true)
                    .setPositiveButton("Yes", (dialogInterface, i) -> {
                        medViewModel.deleteMedData(medicine.getMedId());
                        Toast.makeText(getActivity(), "Medicine has been successfully deleted! " + medicine.getMedId(), Toast.LENGTH_SHORT).show();
                        navController.navigate(R.id.action_medFullDetailFragment_to_listAllMedicineFragment);
                    })
                    .setNegativeButton("Nope", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.cancel();
                        }
                    })
                    .show());

            btnUpdateMed = view.findViewById(R.id.btnUpdateDetailMed);
            btnUpdateMed.setOnClickListener(view12 -> {
                Bundle bundle = new Bundle();
                bundle.putParcelable("update_medicine_data", medicine);
                navController.navigate(R.id.action_medFullDetailFragment_to_updateMedFragment, bundle);
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