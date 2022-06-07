package com.example.ppswe.view.patient;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ppswe.R;
import com.example.ppswe.adapter.medDataAdapter;
import com.example.ppswe.model.report.ReportFile;
import com.example.ppswe.viewmodel.MedViewModel;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.parser.listener.TextChunk;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Tab;
import com.itextpdf.layout.element.TabStop;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.properties.TabAlignment;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.UnitValue;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.lang.reflect.Array;
import java.text.DateFormat;
import java.text.Format;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.stream.Collectors;

public class ReportPatientActivity extends AppCompatActivity {

    private PieChart pieChart;
    private BottomNavigationView bottomNavigationView;
    private TextView tvDateReport;
    private Button btnGenerateReport;

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
            Log.d("CHECK_COUNT", " = " + statusCountList.toString());
            setupPieChart();
            loadPieChartData(statusCountList);
        });


        tvDateReport = findViewById(R.id.tvReportDate);
        String dayBefore = getAWeekBeforeCurrentDay();
        tvDateReport.setText(dayBefore);

        btnGenerateReport = findViewById(R.id.btnGenerateReport);
        btnGenerateReport.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                try {
                    createPDF(String.valueOf(System.currentTimeMillis()));
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }
        });

        bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setSelectedItemId(R.id.report);

        bottomNavigationView.setOnItemSelectedListener(item -> {

            switch (item.getItemId()) {
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
        entries.add(new PieEntry((statusCountList.get(2) / totalCount) * 100, "Postpone"));
        entries.add(new PieEntry((statusCountList.get(1) / totalCount) * 100, "Skip"));

        // Color
        ArrayList<Integer> colors = new ArrayList<>();
        for (int color : ColorTemplate.MATERIAL_COLORS) {
            colors.add(color);
        }

        for (int color : ColorTemplate.VORDIPLOM_COLORS) {
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

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createPDF(String fileId) throws FileNotFoundException {

        medViewModel.getReportData().observe(this, reportData -> {
            Log.d("REPORT_DATA", " This is it " + reportData.getReportDate());
            ArrayList<String> listMedName = reportData.getMedName();
            ArrayList<String> listReportData = reportData.getReportDate(); // history date for report
            ArrayList<String> listStatusMed = reportData.getMedStatus(); // status for report

            String pdfPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString();
            File file = new File(pdfPath, fileId + ".pdf");

            PdfWriter pdfWriter = null;
            try {
                pdfWriter = new PdfWriter(file);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            PdfDocument pdfDocument = new PdfDocument(pdfWriter);
            Document document = new Document(pdfDocument);

            ArrayList<String> dateOfWeek = getAllDateForWeek();
            Log.d("DATE_WEEK", "" + dateOfWeek);

            ArrayList<String> listTable = new ArrayList<>();
            listTable = (ArrayList<String>) dateOfWeek.stream()
                    .filter(listReportData::contains)
                    .collect(Collectors.toList());

            // Log.d("NEW_ARRAYLIST", "result" + listTable);
            // Header
            Paragraph p = new Paragraph("\n\n\n\nName: Danish Irfan")
                    .add(new Tab())
                    .addTabStops(new TabStop(1000, TabAlignment.RIGHT))
                    .add(String.valueOf(java.time.LocalDate.now()))
                    .setBold()
                    .setFontSize(16)
                    .add("\n\n\n");
            document.add(p);

            // Table
            float[] pointColumnWidths = {150F, 150F, 150F}; // 3 columns

            // header**
            Table table = new Table(UnitValue.createPercentArray(new float[]{5, 5, 5}))
                    .setWidth(UnitValue.createPercentValue(100))
                    .addCell(new Cell().add(new Paragraph("Date").setBold()).setTextAlignment(TextAlignment.CENTER).setMargin(5))
                    .addCell(new Cell().add(new Paragraph("Medicine").setBold()).setTextAlignment(TextAlignment.CENTER).setMargin(5))
                    .addCell(new Cell().add(new Paragraph("Status").setBold()).setTextAlignment(TextAlignment.CENTER).setMargin(5));

            // content**
            for (int i = 0; i < listTable.size(); i++) {
                int rowToSpan = 1;
                ArrayList<String> temp = new ArrayList<>();
                ArrayList<String> tempStatus = new ArrayList<>();
                for (int j = 0; j < listReportData.size(); j++) {
                    if (listTable.get(i).equals(listReportData.get(j))){
                        ++rowToSpan;
                        temp.add(listMedName.get(j));
                        tempStatus.add(listStatusMed.get(j));
                    }
                }
                // Nothing just to streak HAHAH
                // Rowspan each date
                Cell cell = new Cell(rowToSpan - 1, 1)
                        .add(new Paragraph(listTable.get(i)))
                        .setMargin(5);
                table.addCell(cell);

                for (int n = 0; n < temp.size(); n++) {
                    table.addCell(new Cell().add(new Paragraph(temp.get(n))).setMargin(5));
                    table.addCell(new Cell().add(new Paragraph(tempStatus.get(n))).setMargin(5));
                }
            }
            document.add(table);

            // generate time and date
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();
            Paragraph generateTimeandDate = new Paragraph("\n Report generate at: "+dtf.format(now));
            document.add(generateTimeandDate);

            document.close();
            Toast.makeText(this, "File is download at " + pdfPath, Toast.LENGTH_SHORT).show();
        });

    }

    private ArrayList<String> getAllDateForWeek() {

        ArrayList<String> resultList = new ArrayList<>();

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

        for (int i = 0; i >= -6; i--) {

            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.DATE, i);
            Date currentDate = calendar.getTime();
            String result = format.format(currentDate);

            resultList.add(result);
        }


        return resultList;
    }
}