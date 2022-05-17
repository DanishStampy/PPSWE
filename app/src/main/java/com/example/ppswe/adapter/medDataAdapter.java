package com.example.ppswe.adapter;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ppswe.R;
import com.example.ppswe.model.medicine.MedicineView;

import java.util.ArrayList;

public class medDataAdapter extends RecyclerView.Adapter<medDataAdapter.DesignViewHolder> {

    private static final int VIEW_TYPE_MEDICINE = 0;
    private static final int VIEW_TYPE_EMPTY = 1;

    ArrayList<MedicineView> medicineList;
    private OnMedDetailListener onMedDetailListener;

    public medDataAdapter(ArrayList<MedicineView> medicineList, OnMedDetailListener onMedDetailListener) {
        this.medicineList = medicineList;
        this.onMedDetailListener = onMedDetailListener;
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
        medDataAdapter.DesignViewHolder DesignViewHolder = new medDataAdapter.DesignViewHolder(view, onMedDetailListener);

        return DesignViewHolder;
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull DesignViewHolder holder, int position) {

        if (!medicineList.isEmpty()){
            String am_pm = "AM";

            holder.medName.setText(medicineList.get(position).getMedName());
            holder.medFullDesc.setText(medicineList.get(position).getAllDescription());

            int medTime = medicineList.get(position).getMedTime();

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
                holder.medTime.setText(hour + ":0" + (int) Math.round(dminute) + "" + am_pm);
            } else {
                holder.medTime.setText(hour + ":" + (int) Math.round(dminute) + "" + am_pm);
            }

            //holder.medTime.setText(medicineList.get(position).getMedTime() + "AM");
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
        OnMedDetailListener onMedDetailListener;

        public DesignViewHolder(@NonNull View itemView, OnMedDetailListener onMedDetailListener) {
            super(itemView);

            this.onMedDetailListener = onMedDetailListener;

            if (!medicineList.isEmpty()){
                medName = itemView.findViewById(R.id.tvDisplayMedName);
                medFullDesc = itemView.findViewById(R.id.tvDisplaMedDescription);
                medTime = itemView.findViewById(R.id.tvDisplayMedTime);
            }

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            onMedDetailListener.onMedDetailClick(getAdapterPosition(), medicineList.get(getAdapterPosition()).getMedID());
        }
    }

    public interface OnMedDetailListener{
        void onMedDetailClick(int position, String medID);
    }
}
