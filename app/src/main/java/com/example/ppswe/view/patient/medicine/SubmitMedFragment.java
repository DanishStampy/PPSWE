package com.example.ppswe.view.patient.medicine;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.ppswe.R;
import com.example.ppswe.adapter.LoadingDialog;
import com.example.ppswe.adapter.OnAdapterItemClickListener;
import com.example.ppswe.adapter.buttonTimePickerAdapter;
import com.example.ppswe.adapter.reciever.AlarmReceiver;
import com.example.ppswe.model.medicine.Medicine;
import com.example.ppswe.model.medicine.SingletonMedicine;
import com.example.ppswe.view.patient.MainMenuActivity;
import com.example.ppswe.viewmodel.MedViewModel;

import java.util.ArrayList;
import java.util.Calendar;

import io.grpc.okhttp.internal.Util;

public class SubmitMedFragment extends Fragment implements OnAdapterItemClickListener {

    private RecyclerView recyclerViewTimePickerButton;
    private buttonTimePickerAdapter buttonTimePickerAdapter;
    private Button btnSubmitMedData;
    private EditText etMedInstruction, etMedDesc;

    Calendar[] calendar;
    AlarmManager[] alarmManager;
    ArrayList<PendingIntent> intentArray;
    ArrayList<Integer> medTimes = new ArrayList<>();
    ArrayList<Integer> list = new ArrayList<>();

    private MedViewModel medViewModel;
    private Medicine medicine;

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
        return inflater.inflate(R.layout.fragment_submit_med, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // check argument and get bundle
        if (getArguments() != null) {
            medicine = getArguments().getParcelable("new_med");
        }

        // recyclerview
        recyclerViewTimePickerButton = view.findViewById(R.id.recyclerTime);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);

        recyclerViewTimePickerButton.setLayoutManager(linearLayoutManager);

        // init array list - all zero
        for (int i = 0; i < medicine.getMedFreq(); i++) {
            medTimes.add(0);
        }

        // Set time picker on button in recyclerview
        timePicker();

        etMedInstruction = view.findViewById(R.id.etMedInstruction);
        etMedDesc = view.findViewById(R.id.etMedDescription);

        // Init notification channel
        createNoficationChannel();

        btnSubmitMedData = view.findViewById(R.id.btnSubmitMedData);
        btnSubmitMedData.setOnClickListener(view1 -> {

            String medInstruction = etMedInstruction.getText().toString().trim();
            String medDescription = etMedDesc.getText().toString().trim();

            if (validateInfo(medInstruction, medTimes)) {

                int[] hour = new int[list.size()];
                int[] minute = new int[list.size()];

                for (int i = 0; i < list.size(); i++) {
                    int timeInMillis = list.get(i);
                    hour[i] = timeInMillis / 60 / 60;

                    int tempMin = hour[i] % 1 * 60;
                    minute[i] = Math.round(tempMin);
                }
                setAlarm(list.size(), hour, minute);


                medicine.setMedInstruction(medInstruction);
                medicine.setMedDesc(medDescription);
                medicine.setMedTimes(medTimes);

                medViewModel.writeMed(medicine);

                Toast.makeText(getActivity(), "Medicine has been successfully saved!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getActivity(), MainMenuActivity.class));
            }
        });
    }

    private void createNoficationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            CharSequence name = "medicationChannel";
            String description = "Channel for alarm manager";
            int important = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel("medicationChannel", name, important);
            channel.setDescription(description);

            NotificationManager notificationManager = getActivity().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private Boolean validateInfo(String medInstruction, ArrayList<Integer> medTimes) {

        if (medInstruction.isEmpty()) {
            etMedInstruction.requestFocus();
            etMedInstruction.setError("Please enter medicine instruction.");
            return false;

        } else if (medTimes.contains(0)) {
            Toast.makeText(getActivity(), "Please pick the times.", Toast.LENGTH_SHORT).show();
            return false;

        } else {
            return true;
        }
    }

    // method to init adapter
    public void timePicker() {
        buttonTimePickerAdapter = new buttonTimePickerAdapter(this);
        buttonTimePickerAdapter.setMedTimes(medTimes);
        recyclerViewTimePickerButton.setAdapter(buttonTimePickerAdapter);

        list = buttonTimePickerAdapter.getMedTimes();
    }

    // method to set alarm
    private void setAlarm(int size, int[] hour, int[] minute) {

        calendar = new Calendar[size];
        alarmManager = new AlarmManager[size];
        intentArray = new ArrayList<PendingIntent>();

        for (int i = 0; i < size; i++) {

            calendar[i] = Calendar.getInstance();
            calendar[i].set(Calendar.HOUR_OF_DAY, hour[i]);
            calendar[i].set(Calendar.MINUTE, minute[i]);
            calendar[i].set(Calendar.SECOND, 00);
            calendar[i].set(Calendar.MILLISECOND, 00);

            alarmManager[i] = (AlarmManager) getActivity().getSystemService(Context.ALARM_SERVICE);

            Intent intent = new Intent(getActivity(), AlarmReceiver.class);
            intent.putExtra("reqCode", i);
            final int id = (int) System.currentTimeMillis();

            int pendingFlags;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                pendingFlags = PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE;
                Log.d("set_alarm", "setting");
            } else {
                pendingFlags = PendingIntent.FLAG_UPDATE_CURRENT;
            }


            PendingIntent pendingIntent = PendingIntent.getBroadcast(getActivity(), id, intent, pendingFlags);

            alarmManager[i].setRepeating(AlarmManager.RTC_WAKEUP, calendar[i].getTimeInMillis(),
                    alarmManager[i].INTERVAL_DAY, pendingIntent);

            intentArray.add(pendingIntent);
        }
    }


    @SuppressLint("InflateParams")
    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onAdapterItemClickListener(int position) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        // Toast.makeText(getContext(), "deez"+position, Toast.LENGTH_SHORT).show();

        View customLayout;

        customLayout = getLayoutInflater().inflate(R.layout.dialog_timepicker, null);
        builder.setView(customLayout);
        TimePicker picker = customLayout.findViewById(R.id.timePicker);
        picker.setIs24HourView(true);

        builder.setPositiveButton("OK", (dialogInterface, i) -> {
            int hour, minute;
            hour = picker.getHour();
            minute = picker.getMinute();

            int time = hour * 60 * 60 + (minute * 60);

            //Toast.makeText(getActivity(), "Hour : " + hour + " Minute : " + minute, Toast.LENGTH_LONG).show();

            medTimes.set(position, time);
            timePicker();
        });

        builder.show();
    }
}