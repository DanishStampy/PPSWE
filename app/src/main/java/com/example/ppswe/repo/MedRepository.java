package com.example.ppswe.repo;

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
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class MedRepository {

    private Application application;
    private MutableLiveData<ArrayList<MedicineView>> medicineArrayList;
    private MutableLiveData<ArrayList<MedicineView>> medicineArrayListCaregiver;
    private MutableLiveData<ArrayList<Integer>> reportStatusCountList;
    private MutableLiveData<ReportFile> reportDetail;
    private MutableLiveData<ArrayList<Medicine>> medicineDataArrayList;
    private MutableLiveData<ArrayList<Medicine>> medicineDataCaregiverArrayList;

    private FirebaseAuth auth;
    private FirebaseFirestore firestore;

    private CollectionReference medRef;
    private CollectionReference medRefCaregiver;
    private DocumentReference userRef;
    private CollectionReference medHistoryRef;

    private String uid, patient_email;

    public MedRepository(Application application) {
        this.application = application;

        medicineArrayList = new MutableLiveData<>();
        reportStatusCountList = new MutableLiveData<>();
        reportDetail = new MutableLiveData<>();
        medicineArrayListCaregiver = new MutableLiveData<>();
        medicineDataArrayList = new MutableLiveData<>();
        medicineDataCaregiverArrayList = new MutableLiveData<>();

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

            // Medicine list for certain patient
            medRefCaregiver = firestore.collection("users");


        }
    }

    // Insert med in firestore
    public void writeMed (Medicine medicine) {

        userRef.collection("medLists")
                .document(System.currentTimeMillis()+"."+medicine.getMedName())
                .set(medicine)
                .addOnSuccessListener(unused -> Log.d("Success", "Medicine successfully written!"))
                .addOnFailureListener(e -> Log.w("FAIL", "Error writing doc", e));
    }

    // Set/Update med status
    @RequiresApi(api = Build.VERSION_CODES.O)
    public void updateMedStatus (MedicineStatus status){

        medHistoryRef.document()
                .set(status)
                .addOnSuccessListener(unused -> Log.d("Success", "Medicine status successfully written!"))
                .addOnFailureListener(e -> Log.w("FAIL", "Error writing doc", e));
    }


    // Get all med
    public MutableLiveData<ArrayList<MedicineView>> getMedicineArrayList() {
        medRef.addSnapshotListener((value, error) -> {
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

                        Log.d("EXIST", "ada je = " + medicineList.size());
                    }
                }
                medicineArrayList.postValue(medicineList);
            }
        });
        //Log.d("MED_COUNT", "This is = " + medicineArrayList.size());
        return medicineArrayList;
    }

    // Get all med caregiver
    public MutableLiveData<ArrayList<MedicineView>> getMedicineArrayListCaregiver() {

        userRef.get()
                .addOnCompleteListener(task -> {
                    DocumentSnapshot documentSnapshot = task.getResult();

                    String patientEmail = documentSnapshot.getString("patientEmail");

                    if ( !patientEmail.equals("empty")) {
                        Query queryPatientEmail = medRefCaregiver.whereEqualTo("email", patientEmail);
                        queryPatientEmail.addSnapshotListener((value, error) -> {
                            if (value != null) {
                                ArrayList<MedicineView> medicineViews = new ArrayList<>();
                                for ( DocumentChange doc : value.getDocumentChanges()) {
                                    doc.getDocument().getReference().collection("medLists")
                                            .addSnapshotListener((value1, error1) -> {

                                                ArrayList<Integer> medicineTime;
                                                Log.d("TEST", "EXISTS?");
                                                if (value1 != null){
                                                    for (QueryDocumentSnapshot doc1 : value1) {
                                                        if (doc1 != null) {
                                                            medicineTime = (ArrayList<Integer>) doc1.get("medTimes");
                                                            //Log.d("MED_TIMES", medicineTime.toString());

                                                            for (int i = 0 ; i < medicineTime.size() ; i++){
                                                                MedicineView medicineView = new MedicineView(doc1.getId(), doc1.getString("medName"), doc1.getString("medInstruction"), doc1.getLong("medDose").intValue(), doc1.getString("medType") );

                                                                // Cast from long into integer
                                                                medicineView.setMedTime(((Number)medicineTime.get(i)).intValue());
                                                                medicineViews.add(medicineView);
                                                            }
                                                        }
                                                    }

                                                }
                                            });
                                }
                                medicineArrayListCaregiver.postValue(medicineViews);
                            } else {
                                Log.d("TEST", "NOPE?");
                            }
                        });
                    }
                });
        return medicineArrayListCaregiver;
    }

    // get important data for report: count status
    public MutableLiveData<ArrayList<Integer>> getReportStatusCountList() {

        medHistoryRef.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @RequiresApi(api = Build.VERSION_CODES.N)
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            ArrayList<Integer> count = new ArrayList<>(
                                    Arrays.asList(0, 0, 0)  // TAKEN, SKIP, POSTPONE
                            );

                            ArrayList<String> currentDates = getAllDateForWeeks();

                            for (QueryDocumentSnapshot doc : task.getResult()){

                                String status = doc.getString("medStatus");
                                String date = doc.getString("date");

                                if (currentDates.contains(date)) {
                                    switch (status){

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
                                }
                            }
                            reportStatusCountList.postValue(count);
                        } else {
                            Log.d("ERR", "Error getting doc: ", task.getException());
                        }
                    }

                    private ArrayList<String> getAllDateForWeeks() {
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
                });

        return reportStatusCountList;
    }

    // Get all date from med history
    public MutableLiveData<ReportFile> getReportDetail() {

        medHistoryRef.get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        ReportFile file;

                        ArrayList<String> medHistoryDate = new ArrayList<>();
                        ArrayList<String> medStatus = new ArrayList<>();
                        ArrayList<String> medTimes = new ArrayList<>();
                        ArrayList<String> medName = new ArrayList<>();

                        for (QueryDocumentSnapshot doc : task.getResult()) {
                            String[] split = doc.getString("medId").split("\\.");

                            medName.add(split[1]);
                            medHistoryDate.add(doc.getString("date"));
                            medStatus.add(doc.getString("medStatus"));
                            medTimes.add(doc.getString("medTime"));
                        }

                        file = new ReportFile(medHistoryDate, medStatus, medTimes, medName);
                        reportDetail.postValue(file);
                    }
                });
        return reportDetail;
    }

    // Delete whole med data
    public void deleteMedData(String medId) {

        medRef.document(medId)
                .delete()
                .addOnCompleteListener(task -> Log.d("DELETE_WWHOLE_MED_DATA", "SUCCESSFULLY DELETED!"));
    }

    // Delete specific med time
    public void deleteMedTime(String medId, int medTime) {

        medRef.document(medId)
                .update("medTimes", FieldValue.arrayRemove(medTime))
                .addOnCompleteListener(task -> Log.d("DELETE_MEDTIME", "SUCCESSFULLY DELETED!"));
    }

    // get all med data details
    // no time separation
    public MutableLiveData<ArrayList<Medicine>> getMedicineDataArrayList() {

        medRef.addSnapshotListener((value, error) -> {
            ArrayList<Medicine> listData = new ArrayList<>();

            if (value != null){
                for (QueryDocumentSnapshot doc : value) {
                    if (doc != null) {
                        Medicine medicine = new Medicine();
                        medicine = doc.toObject(Medicine.class);
                        medicine.setMedId(doc.getId());
                        listData.add(medicine);
                        Log.d("CHECK_ID", "ID = " + medicine.getMedId());
                    }
                }
                medicineDataArrayList.postValue(listData);
            }
        });

        return medicineDataArrayList;
    }

    // update med data details
    public void updateMed (Medicine medicine) {

        userRef.collection("medLists")
                .document(medicine.getMedId())
                .set(medicine)
                .addOnSuccessListener(unused -> Log.d("Success", "Medicine successfully udpated!"))
                .addOnFailureListener(e -> Log.w("FAIL", "Error updating doc", e));

        Map<String,Object> delete_id = new HashMap<>();
        delete_id.put("medId", FieldValue.delete());

        userRef.collection("medLists")
                .document(medicine.getMedId())
                .update(delete_id)
                .addOnCompleteListener(task -> Log.d("Success_delete_med_id", "Medicine successfully udpated!"));
    }

    public MutableLiveData<ArrayList<Medicine>> getMedicineDataCaregiverArrayList() {

        getPatientEmail();
        Log.d("PATIENT_EMAIL_FETCH", "email = "+patient_email);

        userRef.get()
                .addOnCompleteListener(task -> {
                    DocumentSnapshot documentSnapshot = task.getResult();

                    String patientEmail = documentSnapshot.getString("patientEmail");

                    if ( !patientEmail.equals("empty")) {
                        Query queryPatientEmail = medRefCaregiver.whereEqualTo("email", patientEmail);
                        queryPatientEmail.addSnapshotListener((value, error) -> {

                            if (value != null) {
                                for (QueryDocumentSnapshot doc : value) {
                                    if (doc != null) {

                                        String uid = doc.getId();

                                        medRefCaregiver.document(uid)
                                                .collection("medLists")
                                                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                                                    @Override
                                                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                                                        ArrayList<Medicine> listNewData = new ArrayList<>();

                                                        for (QueryDocumentSnapshot doc : value) {
                                                            if (doc != null) {
                                                                Medicine medicine = doc.toObject(Medicine.class);
                                                                medicine.setMedId(doc.getId());
                                                                listNewData.add(medicine);
                                                                Log.d("CHECK_ID", "ID = " + medicine.getMedId());
                                                            }
                                                        }
                                                        Log.d("listdata", "size " + listNewData.size());
                                                        medicineDataCaregiverArrayList.postValue(listNewData);
                                                    }
                                                });
                                    }
                                }

                            } else {
                                Log.d("TEST", "NOPE?");
                            }
                        });
                    }
                });

        return medicineDataCaregiverArrayList;
    }

    private void getPatientEmail() {
        userRef.get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot documentSnapshot = task.getResult();
                        patient_email = documentSnapshot.getString("patientEmail");
                    }
                });
    }
}
