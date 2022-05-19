package com.example.ppswe.repo;

import android.annotation.SuppressLint;
import android.app.Application;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.MutableLiveData;

import com.example.ppswe.model.medicine.Medicine;
import com.example.ppswe.model.medicine.MedicineStatus;
import com.example.ppswe.model.medicine.MedicineView;
import com.example.ppswe.model.report.ReportFile;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Objects;

public class MedRepository {

    private Application application;
    private MutableLiveData<ArrayList<MedicineView>> medicineArrayList;
    private MutableLiveData<ArrayList<Integer>> reportStatusCountList;
    private MutableLiveData<ReportFile> reportDetail;

    private FirebaseAuth auth;
    private FirebaseFirestore firestore;

    private CollectionReference medRef;
    private DocumentReference userRef;
    private CollectionReference medHistoryRef;

    private String uid;
    public String existence;

    private static final int MED_STATUS_TAKEN = 0;
    private static final int MED_STATUS_SKIP = 1;
    private static final int MED_STATUS_POSTPONE = 2;

    public MedRepository(Application application) {
        this.application = application;

        medicineArrayList = new MutableLiveData<>();
        reportStatusCountList = new MutableLiveData<>();
        reportDetail = new MutableLiveData<>();

        // Init firebase auth and firestore
        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();

        if (auth.getCurrentUser() != null) {
            uid = auth.getUid();

            // User doc reference
            userRef = firestore.collection("users")
                    .document(uid);

            // Medicine doc reference
            medRef = userRef.collection("medLists");

            // Medicine history doc reference
            medHistoryRef = userRef.collection("medHistory");
        }
    }

    // Insert med in firestore
    public void writeMed (String medName, String medType, int medDose, int medFreq, List<Integer> medTimes, String medInstruction, String medDesc) {
        Medicine med = new Medicine(medName, medType, medDose, medFreq, medTimes, medInstruction, medDesc);

        userRef.collection("medLists")
                .document(System.currentTimeMillis()+"."+medName)
                .set(med)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("Success", "Medicine successfully written!");

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("FAIL", "Error writing doc", e);
                    }
                });
    }

    // Set/Update med status
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void updateMedStatus (int i, String medId, String medTime){
        String statusTemp;

        if( i == MED_STATUS_TAKEN){
            statusTemp = "taken";
        } else if ( i == MED_STATUS_SKIP){
            statusTemp = "skip";
        } else {
            statusTemp = "postpone";
        }

        MedicineStatus status = new MedicineStatus(medId, medTime, java.time.LocalDate.now().toString(), statusTemp);

        medHistoryRef.document()
                .set(status)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Log.d("Success", "Medicine status successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("FAIL", "Error writing doc", e);
                    }
                });

//        medHistoryRef.get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//                            for (QueryDocumentSnapshot doc : task.getResult()){
//                                if (doc.getString("medId").equals(medId) && doc.getString("date").equals(status.getDate()) && doc.getString("medTime").equals(medTime)){
//                                    Log.d("Update_medstatus", statusTemp);
//                                }
//                            }
//                        } else {
//                            Log.d("ERR", "Error getting doc: ", task.getException());
//                        }
//                    }
//                });
    }


    // Get all med
    public MutableLiveData<ArrayList<MedicineView>> getMedicineArrayList() {
        medRef.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                ArrayList<MedicineView> medicineList = new ArrayList<>();
                ArrayList<Integer> medicineTime;

                if (value != null){
                    for (QueryDocumentSnapshot doc : value) {
                        if (doc != null) {
                            medicineTime = (ArrayList<Integer>) doc.get("medTimes");
                            //Log.d("MED_TIMES", medicineTime.toString());

                            for (int i = 0 ; i < medicineTime.size() ; i++){
                                MedicineView medicineView = new MedicineView(doc.getId(), doc.getString("medName"), doc.getString("medInstruction"), doc.getLong("medDose").intValue(), doc.getString("medType") );

                                // Cast from long into integer
                                medicineView.setMedTime(((Number)medicineTime.get(i)).intValue());
                                //Log.d("DATA_TYPE", "Data type = " + medicineTime.get(i).getClass().getSimpleName());
                                medicineList.add(medicineView);
                            }

                            //Log.d("EXIST", "ada je = " + medicineArrayList.size());
                        }
                    }
                    medicineArrayList.postValue(medicineList);
                } else {
                    return ;
                }

            }
        });
        //Log.d("MED_COUNT", "This is = " + medicineArrayList.size());
        return medicineArrayList;
    }

    public MutableLiveData<ArrayList<Integer>> getReportStatusCountList() {

        medHistoryRef.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<Integer> count = new ArrayList<>(
                                    Arrays.asList(0, 0, 0)  // TAKEN, SKIP, POSTPONE
                            );

                            for (QueryDocumentSnapshot doc : task.getResult()){

                                switch (Objects.requireNonNull(doc.getString("medStatus"))){

                                    case "taken":
                                        int takenLatestCount = count.get(0);
                                        takenLatestCount++;
                                        count.set(0, takenLatestCount);
                                        break;

                                    case "skip":
                                        int skipLatestCount = count.get(1);
                                        skipLatestCount++;
                                        count.set(1, skipLatestCount);
                                        break;

                                    case "postpone":
                                        int postponeLatestCount = count.get(2);
                                        postponeLatestCount++;
                                        count.set(2, postponeLatestCount);
                                        break;
                                }
                                reportStatusCountList.postValue(count);
                            }
                        } else {
                            Log.d("ERR", "Error getting doc: ", task.getException());
                        }
                    }
                });

        return reportStatusCountList;
    }

    // Get all date from med history
    public MutableLiveData<ReportFile> getReportDetail() {

        medHistoryRef.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ReportFile file;

                            ArrayList<String> medHistoryDate = new ArrayList<>();
                            ArrayList<String> medStatus = new ArrayList<>();
                            ArrayList<String> medTimes = new ArrayList<>();

                            for (QueryDocumentSnapshot doc : task.getResult()) {
                                medHistoryDate.add(doc.getString("date"));
                                medStatus.add(doc.getString("medStatus"));
                                medTimes.add(doc.getString("medTime"));
                            }

                            file = new ReportFile(medHistoryDate, medStatus, medTimes);
                            reportDetail.postValue(file);
                        }
                    }
                });
        return reportDetail;
    }
}
