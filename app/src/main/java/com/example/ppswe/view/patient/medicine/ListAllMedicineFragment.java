package com.example.ppswe.view.patient.medicine;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.ppswe.R;
import com.example.ppswe.adapter.medDataDetailAdapter;
import com.example.ppswe.model.medicine.Medicine;
import com.example.ppswe.viewmodel.MedViewModel;

import java.util.ArrayList;

public class ListAllMedicineFragment extends Fragment implements medDataDetailAdapter.OnMedFullDetailListener {

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
        return inflater.inflate(R.layout.fragment_list_all_medicine, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerViewMedList = view.findViewById(R.id.rcMedDetailList);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerViewMedList.setLayoutManager(linearLayoutManager);

        medViewModel.getMedListData().observe( getActivity(), medListData -> {
            medList = medListData;
            Log.d("SIZE_MED_LIST", "hm " + medList.size());
            medDataDetailAdapter = new medDataDetailAdapter(medList, this);
            Log.d("SIZE_MED_LIST", "hm what " + medDataDetailAdapter.getItemCount());
            recyclerViewMedList.setAdapter(medDataDetailAdapter);
        });

    }

    @Override
    public void onMedFullDetailClick(int position, Medicine medicine) {
        //Toast.makeText(getActivity(), "Id and name is " + medicine.getMedId() + " & " + medicine.getMedName(), Toast.LENGTH_SHORT).show();
        Bundle bundle = new Bundle();
        bundle.putParcelable("medicine_data", medicine);

        navController.navigate(R.id.action_listAllMedicineFragment_to_medFullDetailFragment, bundle);
    }
}