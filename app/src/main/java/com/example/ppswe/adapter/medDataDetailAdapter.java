package com.example.ppswe.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ppswe.R;
import com.example.ppswe.model.medicine.Medicine;

import java.util.ArrayList;

public class medDataDetailAdapter extends RecyclerView.Adapter<medDataDetailAdapter.DesignViewHolder> {

    ArrayList<Medicine> medicinesData;

    public medDataDetailAdapter(ArrayList<Medicine> medicinesData) {
        this.medicinesData = medicinesData;
    }

    @NonNull
    @Override
    public DesignViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_med_full_data, parent, false);
        DesignViewHolder DesignViewHolder = new DesignViewHolder(view);

        return DesignViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull DesignViewHolder holder, int position) {

        holder.medName.setText(medicinesData.get(position).getMedName());
        holder.medDesc.setText("Take everyday " + medicinesData.get(position).getMedInstruction());

        int timesCount = medicinesData.get(position).getMedTimes().size();
        String result = "";

        for (int i = 0; i < timesCount; i++) {
            String am_pm = "AM";

            int medTime = medicinesData.get(position).getMedTimes().get(i);

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

        holder.medTime.setText(result);
     }

    @Override
    public int getItemCount() {
        return medicinesData.size();
    }

    public class DesignViewHolder extends RecyclerView.ViewHolder {

        TextView medName, medTime, medDesc;
        ImageButton imgBtnMedDetails;

        public DesignViewHolder(@NonNull View itemView) {
            super(itemView);

            medName = itemView.findViewById(R.id.tvDisplaySingelMedName);
            medTime = itemView.findViewById(R.id.tvDisplaySingleMedTime);
            medDesc = itemView.findViewById(R.id.tvDisplaySingleMedInfo);

            imgBtnMedDetails = itemView.findViewById(R.id.imgBtnMedDetails);
        }
    }
}
