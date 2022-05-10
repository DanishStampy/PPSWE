package com.example.ppswe.adapter;

import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ppswe.R;
import com.example.ppswe.model.Medicine;

import java.util.ArrayList;
import java.util.List;

public class medDataAdapter extends RecyclerView.Adapter<medDataAdapter.DesignViewHolder> {

    private static final int VIEW_TYPE_MEDICINE = 0;
    private static final int VIEW_TYPE_EMPTY = 1;

    ArrayList<Medicine> medicineList;

    public medDataAdapter(ArrayList<Medicine> medicineList) {
        this.medicineList = medicineList;
        Log.d("GET_COUNT", "COUNT IS " + medicineList.isEmpty());
    }

    @NonNull
    @Override
    public DesignViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view;

        if (viewType == VIEW_TYPE_EMPTY){
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.empty_med_data, parent, false);
        } else {
            view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_med_data, parent, false);
        }
        medDataAdapter.DesignViewHolder DesignViewHolder = new medDataAdapter.DesignViewHolder(view);

        return DesignViewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull DesignViewHolder holder, int position) {

        if (!medicineList.isEmpty()){
            holder.medName.setText(medicineList.get(position).getMedName());
            holder.medFullDesc.setText("Take " + medicineList.get(position).getMedDose() + " " + medicineList.get(position).getMedType() + " " + medicineList.get(position).getMedInstruction());
        }

    }

    @Override
    public int getItemViewType(int position) {
        if (medicineList.size() == 0) {
            return VIEW_TYPE_EMPTY;
        } else {
            return VIEW_TYPE_MEDICINE;
        }
    }

    @Override
    public int getItemCount() {
        if (medicineList.size() == 0) {
            return 1;
        } else {
            return medicineList.size();
        }
    }


    //this will hold the View Design
    public class DesignViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView medName, medFullDesc, medTime;

        public DesignViewHolder(@NonNull View itemView) {
            super(itemView);

            if (!medicineList.isEmpty()){
                medName = itemView.findViewById(R.id.tvDisplayMedName);
                medFullDesc = itemView.findViewById(R.id.tvDisplaMedDescription);
                medTime = itemView.findViewById(R.id.tvDisplayMedTime);
            }

        }

        @Override
        public void onClick(View view) {
        }
    }


}
