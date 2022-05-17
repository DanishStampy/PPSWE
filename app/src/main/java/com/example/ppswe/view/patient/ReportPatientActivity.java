package com.example.ppswe.view.patient;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.TextView;

import com.example.ppswe.R;
import com.example.ppswe.adapter.medDataAdapter;
import com.example.ppswe.viewmodel.MedViewModel;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ReportPatientActivity extends AppCompatActivity {

    private PieChart pieChart;
    private BottomNavigationView bottomNavigationView;
    private TextView tvDateReport;

    private MedViewModel medViewModel;

    private ArrayList<Integer> statusCountList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_patient);

        statusCountList = new ArrayList<>();

        pieChart = findViewById(R.id.pieChartReport);

        // Med view model
        medViewModel = new ViewModelProvider.AndroidViewModelFactory(this.getApplication()).create(MedViewModel.class);
        medViewModel.getStatusCountList().observe(this, statusCount -> {
            statusCountList = statusCount;

            setupPieChart();
            loadPieChartData(statusCountList);
        });


        tvDateReport = findViewById(R.id.tvReportDate);
        String dayBefore = getAWeekBeforeCurrentDay();
        tvDateReport.setText(dayBefore);

        bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setSelectedItemId(R.id.report);

        bottomNavigationView.setOnItemSelectedListener(item -> {

            switch (item.getItemId()){
                case R.id.profile:
                    startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                    return true;

                case R.id.medicine:
                    startActivity(new Intent(getApplicationContext(), MedicineActivity.class));
                    return true;

                case R.id.vitalSign:
                    startActivity(new Intent(getApplicationContext(), VitalSignActivity.class));
                    return true;

                case R.id.report:
                    return true;

                case R.id.home:
                    startActivity(new Intent(getApplicationContext(), MainMenuActivity.class));
                    return true;
            }

            return false;
        });
    }

    @NonNull
    @SuppressLint("SimpleDateFormat")
    private String getAWeekBeforeCurrentDay() {
        String result = "";
        String beforeDay, beforeMonth, currentDay, currentMonth;

        SimpleDateFormat monthFormat = new SimpleDateFormat("MMM");
        SimpleDateFormat dayFormat = new SimpleDateFormat("dd");

        Calendar calendar = Calendar.getInstance();

        Date currentDate = calendar.getTime();
        currentDay = dayFormat.format(currentDate);
        currentMonth = monthFormat.format(currentDate);

        calendar.add(Calendar.DATE, -6);
        currentDate = calendar.getTime();
        beforeDay = dayFormat.format(currentDate);
        beforeMonth = monthFormat.format(currentDate);

        result += beforeDay + " " + beforeMonth + " - " + currentDay + " " + currentMonth + " 2022";

        return result;
    }

    private void setupPieChart() {
        pieChart.setDrawHoleEnabled(true);
        pieChart.setUsePercentValues(true);
        pieChart.setEntryLabelTextSize(14);
        pieChart.setEntryLabelColor(Color.BLACK);
        pieChart.setCenterText("REPORT");
        pieChart.setCenterTextSize(18);
    }

    private void loadPieChartData(ArrayList<Integer> statusCountList) {
        ArrayList<PieEntry> entries = new ArrayList<>();

        float totalCount = statusCountList.get(0) + statusCountList.get(1) + statusCountList.get(2);

        // Add entries
        entries.add(new PieEntry((statusCountList.get(0) / totalCount) * 100, "Taken"));
        entries.add(new PieEntry((statusCountList.get(1) / totalCount) * 100, "Skip"));
        entries.add(new PieEntry((statusCountList.get(2) / totalCount) * 100, "Postpone"));

        // Color
        ArrayList<Integer> colors = new ArrayList<>();
        for ( int color: ColorTemplate.MATERIAL_COLORS) {
            colors.add(color);
        }

        for (int color: ColorTemplate.VORDIPLOM_COLORS) {
            colors.add(color);
        }

        PieDataSet dataSet = new PieDataSet(entries, "Report");
        dataSet.setColors(colors);

        PieData data = new PieData(dataSet);
        data.setDrawValues(true);
        data.setValueFormatter(new PercentFormatter(pieChart));
        data.setValueTextSize(14);
        data.setValueTextColor(Color.BLACK);

        pieChart.setData(data);
        pieChart.invalidate();

    }
}