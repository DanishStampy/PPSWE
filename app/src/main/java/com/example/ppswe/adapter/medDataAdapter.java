package com.example.ppswe.adapter;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
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
    private OnMedDeleteListener onMedDeleteListener;


    public medDataAdapter(ArrayList<MedicineView> medicineList, OnMedDetailListener onMedDetailListener, OnMedDeleteListener onMedDeleteListener) {
        this.medicineList = medicineList;
        this.onMedDetailListener = onMedDetailListener;
        this.onMedDeleteListener = onMedDeleteListener;
        //Log.d("GET_COUNT", "COUNT IS " + medicineList.isEmpty());
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
        medDataAdapter.DesignViewHolder DesignViewHolder = new medDataAdapter.DesignViewHolder(view, onMedDetailListener, onMedDeleteListener);

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

            if (medicineList.get(position).getMedStatus().equals("taken")) {
                holder.medStatus.setImageResource(R.drawable.ic_baseline_check_circle_24);
            } else if (medicineList.get(position).getMedStatus().equals("skip")) {
                holder.medStatus.setImageResource(R.drawable.ic_baseline_warning_24);
            } else {
                holder.medStatus.setImageResource(R.drawable.ic_baseline_access_time_24);
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
    public class DesignViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener{

        TextView medName, medFullDesc, medTime;
        ImageView medStatus;
        OnMedDetailListener onMedDetailListener;
        OnMedDeleteListener onMedDeleteListener;

        public DesignViewHolder(@NonNull View itemView, OnMedDetailListener onMedDetailListener, OnMedDeleteListener onMedDeleteListener) {
            super(itemView);

            this.onMedDetailListener = onMedDetailListener;
            this.onMedDeleteListener = onMedDeleteListener;

            if (!medicineList.isEmpty()){
                medName = itemView.findViewById(R.id.tvDisplaySingleMedInfo);
                medFullDesc = itemView.findViewById(R.id.tvDisplaMedDescription);
                medTime = itemView.findViewById(R.id.tvDisplayMedTime);
                medStatus = itemView.findViewById(R.id.imgMedStatus);
                itemView.setOnClickListener(this);
                itemView.setOnLongClickListener(this);
            }
        }

        @Override
        public void onClick(View view) {
            onMedDetailListener.onMedDetailClick(getAdapterPosition(), medicineList.get(getAdapterPosition()).getMedID());
        }

        @Override
        public boolean onLongClick(View view) {
            onMedDeleteListener.onMedDeleteLongClick(getAdapterPosition(), medicineList.get(getAdapterPosition()).getMedID());
            return true;
        }
    }

    public interface OnMedDeleteListener{
        boolean onMedDeleteLongClick(int position, String medId);
    }

    public interface OnMedDetailListener{
        void onMedDetailClick(int position, String medID);
    }
}
