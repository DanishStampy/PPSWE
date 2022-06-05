package com.example.ppswe.view.caregiver.medicine;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.ppswe.R;
import com.example.ppswe.adapter.medDataDetailAdapter;
import com.example.ppswe.model.medicine.Medicine;
import com.example.ppswe.viewmodel.MedViewModel;

import java.util.ArrayList;

public class CaregiverListAllMedicineFragment extends Fragment implements medDataDetailAdapter.OnMedFullDetailListener {

    private MedViewModel medViewModel;
    private RecyclerView recyclerViewMedList;

    private ArrayList<Medicine> medList;
    private NavController navController;
    
    private medDataDetailAdapter medDataDetailAdapter;

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
        return inflater.inflate(R.layout.fragment_caregiver_list_all_medicine, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerViewMedList = view.findViewById(R.id.rcMedDetailList_caregiver);
        navController = Navigation.findNavController(view);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerViewMedList.setLayoutManager(linearLayoutManager);

        medViewModel.getMedListDataCaregiver().observe( getActivity(), medicines -> {
            medList = medicines;
            medDataDetailAdapter = new medDataDetailAdapter(medList, this);
            recyclerViewMedList.setAdapter(medDataDetailAdapter);
        });
    }

    @Override
    public void onMedFullDetailClick(int position, Medicine medicine) {
        Toast.makeText(getActivity(), "Name " + medicine.getMedName(), Toast.LENGTH_SHORT).show();
        Bundle bundle = new Bundle();
        bundle.putParcelable("medicine_data", medicine);

        navController.navigate(R.id.action_caregiverListAllMedicineFragment_to_caregiverMedFullDetailFragment, bundle);
    }
}