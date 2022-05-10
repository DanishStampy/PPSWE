package com.example.ppswe.adapter;

import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ppswe.R;

import java.util.ArrayList;

public class buttonTimePickerAdapter extends RecyclerView.Adapter<buttonTimePickerAdapter.DesignViewHolder> {
    ArrayList<Integer> medTimes;
    private OnAdapterItemClickListener listener = null;

    //this will hold the Data
    public buttonTimePickerAdapter(OnAdapterItemClickListener onAdapterItemClickListener) {
        this.listener = onAdapterItemClickListener;
    }

    public void setMedTimes(ArrayList<Integer> medTimes){
        this.medTimes = medTimes;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public DesignViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.btn_timepicker,parent,false);
        DesignViewHolder DesignViewHolder=new DesignViewHolder(view);

        return DesignViewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull DesignViewHolder holder, int position) {
        //main function to bind the design
        //pass down the position
        int setMedTimes = medTimes.get(position);

        // set text before pick any text
        if (setMedTimes == 0){
            Log.d("deez", String.valueOf(setMedTimes));
            holder.btnTimePicker.setText("Pick your time");

        } else {

            double dhour = (double) setMedTimes / 60 / 60;
            double dminute = dhour % 1 * 60;

            if ((int) Math.round(dminute) < 10) {
                holder.btnTimePicker.setText((setMedTimes / 60 / 60) + ":0" + (int) Math.round(dminute));
            } else {
                holder.btnTimePicker.setText((setMedTimes / 60 / 60) + ":" + (int) Math.round(dminute));
            }
        }

    }

    @Override
    public int getItemCount() {
        return medTimes.size();
    }

    //this will hold the View Design
    public class DesignViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
       Button btnTimePicker;

        public DesignViewHolder(@NonNull View itemView) {
            super(itemView);

            //Hooks
            btnTimePicker = itemView.findViewById(R.id.btnTimePicker);
            btnTimePicker.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            listener.onAdapterItemClickListener(getAdapterPosition());
        }
    }


}
