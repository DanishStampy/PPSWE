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
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ppswe.R;
import com.example.ppswe.adapter.LoadingDialog;
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
import com.google.protobuf.StringValue;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.stream.Collectors;

public class ReportPatientActivity extends AppCompatActivity {

    ArrayAdapter<String> adapter_reportType;
    String selectedReportType;

    private PieChart pieChart;
    private BottomNavigationView bottomNavigationView;
    private TextView tvDateReport;
    private Button btnGenerateReport;

    private MedViewModel medViewModel;
    private LoadingDialog loadingDialog;

    private ArrayList<Integer> statusCountList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report_patient);

        // init loading dialog
        loadingDialog = new LoadingDialog(this);

        statusCountList = new ArrayList<>();

        pieChart = findViewById(R.id.pieChartReport);
        setupPieChart();

        // Med view model
        medViewModel = new ViewModelProvider.AndroidViewModelFactory(this.getApplication()).create(MedViewModel.class);
        medViewModel.getStatusCountList().observe(this, statusCount -> {
            statusCountList = statusCount;
            //Log.d("CHECK_COUNT", " = " + statusCountList.toString());
            loadPieChartData(statusCountList);
        });

        tvDateReport = findViewById(R.id.tvReportDate);
        String dayBefore = getAWeekBeforeCurrentDay();
        tvDateReport.setText(dayBefore);

        // Dropdown item
        String[] type = new String[] {"Daily Report", "Weekly Report", "Monthly Report"};
        adapter_reportType = new ArrayAdapter<>(
                getApplicationContext(),
                R.layout.med_type_dropdown,
                type
        );

        // Dropdown adapter
        AutoCompleteTextView reportType = findViewById(R.id.dropdown_reportType_patient);
        reportType.setAdapter(adapter_reportType);

        reportType.setOnItemClickListener(((adapterView, view, i, l) -> {
            selectedReportType = reportType.getText().toString();

            switch (selectedReportType) {
                case "Weekly Report":
                    //Toast.makeText(this, "week", Toast.LENGTH_SHORT).show();
                    tvDateReport.setText(getAWeekBeforeCurrentDay());
                    break;
                case "Daily Report":
                    //Toast.makeText(this, "day", Toast.LENGTH_SHORT).show();
                    tvDateReport.setText(getACurrentData());
                    break;
                case "Monthly Report":
                    //Toast.makeText(this, "month", Toast.LENGTH_SHORT).show();
                    break;
                default:
                    break;
            }
        }));

        btnGenerateReport = findViewById(R.id.btnGenerateReport);
        btnGenerateReport.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                loadingDialog.showDialog();
                try {
                    //Toast.makeText(ReportPatientActivity.this, "Report type: "+selectedReportType, Toast.LENGTH_SHORT).show();

                    if (selectedReportType != null) {

                        switch (selectedReportType) {
                            case "Weekly Report":
                                createPDF_weekly(String.valueOf(System.currentTimeMillis()));
                                break;
                            case "Daily Report":
                                createPDF_daily(String.valueOf(System.currentTimeMillis()));
                                break;
                            case "Monthly Report":
                                createPDF_monthly(String.valueOf(System.currentTimeMillis()));
                                break;
                        }

                    } else {

                        Toast.makeText(ReportPatientActivity.this, "Please select a report type to continue.", Toast.LENGTH_SHORT).show();
                    }

                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                loadingDialog.hideDialog();
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

    @SuppressLint("SimpleDateFormat")
    private String getACurrentData() {
        String result = "";
        String currentDay, currentMonth;

        SimpleDateFormat monthFormat = new SimpleDateFormat("MMM");
        SimpleDateFormat dayFormat = new SimpleDateFormat("dd");

        Calendar calendar = Calendar.getInstance();

        Date currentDate = calendar.getTime();
        currentDay = dayFormat.format(currentDate);
        currentMonth = monthFormat.format(currentDate);

        result += currentDay + " " + currentMonth + " 2022";
        return result;
    }

    public static String theMonth(int month){
        String[] monthNames = {"January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"};
        return monthNames[month-1];
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
    private void createPDF_weekly(String fileId) throws FileNotFoundException {

        medViewModel.getReportData().observe(this, reportData -> {
            Log.d("REPORT_DATA", " This is it " + reportData.getReportDate());
            ArrayList<String> listMedName = reportData.getMedName();
            ArrayList<String> listReportData = reportData.getReportDate(); // history date for report
            ArrayList<String> listStatusMed = reportData.getMedStatus(); // status for report
            ArrayList<String> listMedTimes = reportData.getMedTimes(); // time data for medicine

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
            // Header
            Paragraph p = new Paragraph("\nName: "+reportData.getPatientName())
                    .add(new Tab())
                    .addTabStops(new TabStop(1000, TabAlignment.RIGHT))
                    .add(String.valueOf(LocalDate.now()))
                    .setBold()
                    .setFontSize(16)
                    .add("\n")
                    .add(new Paragraph("Weekly Report")
                            .setFontSize(24)
                            .setBold())
                    .setTextAlignment(TextAlignment.CENTER)
                    .add("\n\n");
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
                        temp.add(listMedName.get(j)+ " (" + getSpecificMedTime(listMedTimes.get(j)) + ")");
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
            Paragraph generateTimeandDate = new Paragraph("\n Report generate at: "+dtf.format(now) + " from PPSWE apps");
            document.add(generateTimeandDate);

            document.close();
            Toast.makeText(this, "Weekly report has been downloaded.", Toast.LENGTH_SHORT).show();
        });

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createPDF_daily(String fileId) throws FileNotFoundException {

        medViewModel.getReportData().observe(this, reportData -> {
            ArrayList<String> listMedName = reportData.getMedName();
            ArrayList<String> listReportData = reportData.getReportDate(); // history date for report
            ArrayList<String> listStatusMed = reportData.getMedStatus(); // status for report
            ArrayList<String> listMedTimes = reportData.getMedTimes(); // time data for medicine

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

            String todayDate = String.valueOf(java.time.LocalDate.now());

            // Header
            Paragraph p = new Paragraph("\nName: "+reportData.getPatientName())
                    .add(new Tab())
                    .addTabStops(new TabStop(1000, TabAlignment.RIGHT))
                    .add(String.valueOf(LocalDate.now()))
                    .setBold()
                    .setFontSize(16)
                    .add("\n")
                    .add(new Paragraph("Daily Report")
                            .setFontSize(24)
                            .setBold())
                    .setTextAlignment(TextAlignment.CENTER)
                    .add("\n\n");
            document.add(p);

            // Table
            float[] pointColumnWidths = {150F, 150F, 150F}; // 3 columns

            // header**
            Table table = new Table(UnitValue.createPercentArray(new float[]{5, 5, 5}))
                    .setWidth(UnitValue.createPercentValue(100))
                    .addCell(new Cell().add(new Paragraph("Date").setBold()).setTextAlignment(TextAlignment.CENTER).setMargin(5))
                    .addCell(new Cell().add(new Paragraph("Medicine").setBold()).setTextAlignment(TextAlignment.CENTER).setMargin(5))
                    .addCell(new Cell().add(new Paragraph("Status").setBold()).setTextAlignment(TextAlignment.CENTER).setMargin(5));

            int rowToSpan = 0;
            ArrayList<String> temp = new ArrayList<>();
            ArrayList<String> tempStatus = new ArrayList<>();
            for (int j = 0; j < listReportData.size(); j++) {
                if (todayDate.equals(listReportData.get(j))){
                    ++rowToSpan;
                    temp.add(listMedName.get(j)+ " (" + getSpecificMedTime(listMedTimes.get(j)) + ")");
                    tempStatus.add(listStatusMed.get(j));
                }
            }
            // Nothing just to streak HAHAH
            // Rowspan each date
            Cell cell = new Cell(rowToSpan, 1)
                    .add(new Paragraph(todayDate))
                    .setMargin(5);
            table.addCell(cell);

            for (int n = 0; n < temp.size(); n++) {
                table.addCell(new Cell().add(new Paragraph(temp.get(n))).setMargin(5));
                table.addCell(new Cell().add(new Paragraph(tempStatus.get(n))).setMargin(5));
            }

            document.add(table);

            // generate time and date
            DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
            LocalDateTime now = LocalDateTime.now();
            Paragraph generateTimeandDate = new Paragraph("\n Report generate at: "+dtf.format(now) + " from PPSWE apps");
            document.add(generateTimeandDate);

            document.close();
            Toast.makeText(this, "Daily report has been downloaded.", Toast.LENGTH_SHORT).show();
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    private void createPDF_monthly(String fileId) throws  FileNotFoundException {

        medViewModel.getReportData().observe(this, reportData -> {
            Log.d("REPORT_DATA", " This is it " + reportData.getReportDate());
            ArrayList<String> listMedName = reportData.getMedName();
            ArrayList<String> listReportData = reportData.getReportDate(); // history date for report
            ArrayList<String> listStatusMed = reportData.getMedStatus(); // status for report
            ArrayList<String> listMedTimes = reportData.getMedTimes(); // time data for medicine

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

            String currMonth = getCurrMonth();

            ArrayList<String> tempDate = new ArrayList<>();
            String tempStrDate;
            for (int i = 0; i < listReportData.size(); i++) {
                String[] tempDateArr = listReportData.get(i).split("-");
                tempStrDate = listReportData.get(i);
                if (tempDateArr[1].equals(currMonth)){
                    if (tempDate.size() == 0) {
                        tempDate.add(listReportData.get(i));
                    } else if (!tempDate.contains(listReportData.get(i))){
                        tempDate.add(listReportData.get(i));
                    }
                }
            }


            // Log.d("NEW_ARRAYLIST", "result" + listTable);
            // Header
            Paragraph p = new Paragraph("\nName: "+reportData.getPatientName())
                    .add(new Tab())
                    .addTabStops(new TabStop(1000, TabAlignment.RIGHT))
                    .add(String.valueOf(LocalDate.now()))
                    .setBold()
                    .setFontSize(16)
                    .add("\n")
                    .add(new Paragraph("Monthly Report")
                    .setFontSize(24)
                    .setBold())
                    .setTextAlignment(TextAlignment.CENTER)
                    .add("\n\n");
            document.add(p);

            // Table
            float[] pointColumnWidths = {150F, 150F, 150F}; // 3 columns

            // header**
            Table table = new Table(UnitValue.createPercentArray(new float[]{5, 5, 5}))
                    .setWidth(UnitValue.createPercentValue(100))
                    .addCell(new Cell().add(new Paragraph("Date").setBold()).setTextAlignment(TextAlignment.CENTER).setMargin(5))
                    .addCell(new Cell().add(new Paragraph("Medicine").setBold()).setTextAlignment(TextAlignment.CENTER).setMargin(5))
                    .addCell(new Cell().add(new Paragraph("Status").setBold()).setTextAlignment(TextAlignment.CENTER).setMargin(5));

            for (int i = 0; i<tempDate.size(); i++) {

                int rowToSpan = 1;
                ArrayList<String> temp = new ArrayList<>();
                ArrayList<String> tempStatus = new ArrayList<>();
                for (int j = 0; j < listReportData.size(); j++) {
                    if (listReportData.get(j).equals(tempDate.get(i))){
                        ++rowToSpan;
                        temp.add(listMedName.get(j)+ " (" + getSpecificMedTime(listMedTimes.get(j)) + ")");
                        tempStatus.add(listStatusMed.get(j));
                    }
                }

                // Rowspan each date
                Cell cell = new Cell(rowToSpan - 1, 1)
                        .add(new Paragraph(tempDate.get(i)))
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
            Paragraph generateTimeandDate = new Paragraph("\n Report generate at: "+dtf.format(now) +" from PPSWE apps");
            document.add(generateTimeandDate);

            document.close();
            Toast.makeText(this, "Monthly report has been downloaded.", Toast.LENGTH_SHORT).show();
//            Toast.makeText(this, "File is download at " + pdfPath, Toast.LENGTH_SHORT).show();
        });
    }

    private String getCurrMonth() {
        String result = "";

        Calendar calendar = Calendar.getInstance();
        int monthNum = calendar.get(Calendar.MONTH) + 1;


        result = (monthNum < 10) ? "0"+monthNum : String.valueOf(monthNum);
        return result;
    }

    @SuppressLint("SimpleDateFormat")
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

    private String getSpecificMedTime(String time) {
        String result = "";

        String am_pm = "AM";

        int medTime = Integer.parseInt(time);

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

        return result;
    }
}