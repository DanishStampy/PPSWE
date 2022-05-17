package com.example.ppswe.adapter;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ppswe.R;

import java.util.ArrayList;
import java.util.Calendar;

public class buttonTimePickerAdapter extends RecyclerView.Adapter<buttonTimePickerAdapter.DesignViewHolder> {
    ArrayList<Integer> medTimes;
    private OnAdapterItemClickListener listener = null;

    public int hour, minute;

    //this will hold the Data
    public buttonTimePickerAdapter(OnAdapterItemClickListener onAdapterItemClickListener) {
        this.listener = onAdapterItemClickListener;
    }

    @SuppressLint("NotifyDataSetChanged")
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

    public ArrayList<Integer> getMedTimes() {
        return medTimes;
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

    @SuppressLint("SetTextI18n")
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

            this.hour = setMedTimes / 60 / 60;
            this.minute = (int)Math.round(dminute);

            if ((int) Math.round(dminute) < 10) {
                holder.btnTimePicker.setText(hour + ":0" + minute);
            } else {
                holder.btnTimePicker.setText(hour + ":" + minute);
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
