package com.example.ppswe.view.patient.profile;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.ppswe.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class SyncCaregiverFragment extends Fragment {

    private EditText etCaregiverEmail_Sync;
    private Button btnSubmitCaregiverEmail;
    private FirebaseAuth auth;
    private FirebaseFirestore firestore;

    private static final String URL_SUBMIT = "http://192.168.57.101/api_ppswe/sync_account.php";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Init
        auth = FirebaseAuth.getInstance();
        firestore = FirebaseFirestore.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_sync_caregiver, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        etCaregiverEmail_Sync = view.findViewById(R.id.etCaregiverEmail_sync);
        btnSubmitCaregiverEmail = view.findViewById(R.id.btnSubmitSyncCaregiver);

        btnSubmitCaregiverEmail.setOnClickListener(view1 -> {

            if (validation(etCaregiverEmail_Sync.getText().toString())) {

                final String uid = auth.getUid();
                final String email = etCaregiverEmail_Sync.getText().toString();

                firestore.collection("users")
                        .whereEqualTo("roles", "caregiver")
                        .whereEqualTo("email", email)
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                if (task.isSuccessful()) {
                                    if (task.getResult().isEmpty()) {
                                        Toast.makeText(getActivity().getApplicationContext(), "Caregiver account doesn't exists", Toast.LENGTH_SHORT).show();
                                    } else {

                                        //Toast.makeText(getActivity(), "hallo" + uid, Toast.LENGTH_SHORT).show();
                                        StringRequest stringRequest = new StringRequest(Request.Method.POST, URL_SUBMIT,
                                                new Response.Listener<String>() {
                                                    @Override
                                                    public void onResponse(String response) {
                                                        try {
                                                            // on below line we are passing our response
                                                            // to json object to extract data from it.

                                                            Log.i("tagconvertstr", "[" + response + "]");
                                                            JSONObject respObj = new JSONObject(response);

                                                            // below are the strings which we
                                                            // extract from our json object.
                                                            String id = respObj.getString("uid");
                                                            String email = respObj.getString("email");

                                                            // we just toast the value we got from API, 1 for success, 0 otherwise
                                                            Toast.makeText(getActivity(), "Verification link has been sent to the caregiver email.", Toast.LENGTH_SHORT).show();
                                                        } catch (JSONException e) {
                                                            e.printStackTrace();
                                                            Toast.makeText(getActivity(), "Error: " + e.toString(), Toast.LENGTH_LONG).show();
                                                        }
                                                    }
                                                },
                                                error1 -> Toast.makeText(getActivity(), "Error volley: " + error1.toString(), Toast.LENGTH_SHORT).show()) {
                                            @NonNull
                                            @Override
                                            protected Map<String, String> getParams() {

                                                Map<String, String> params = new HashMap<>();
                                                params.put("uid", uid);
                                                params.put("email", email);

                                                return params;

                                            }
                                        };

                                        stringRequest.setRetryPolicy(new RetryPolicy() {
                                            @Override
                                            public int getCurrentTimeout() {
                                                return 50000;
                                            }

                                            @Override
                                            public int getCurrentRetryCount() {
                                                return 50000;
                                            }

                                            @Override
                                            public void retry(VolleyError error) throws VolleyError {

                                            }
                                        });

                                        RequestQueue requestQueue = Volley.newRequestQueue(getActivity());
                                        requestQueue.add(stringRequest);
                                    }
                                } else {
                                    Log.d("err", "Error getting documents");
                                }
                            }
                        });
            }
        });
    }

    private boolean validation(String email) {

        Pattern pattern = Pattern.compile(getString(R.string.email_pattern));
        Matcher matcher;

        if (email.isEmpty()) {
            etCaregiverEmail_Sync.requestFocus();
            etCaregiverEmail_Sync.setError("Please enter caregiver email to continue.");
            return false;
        } else if (!(pattern.matcher(email).matches())) {
            etCaregiverEmail_Sync.requestFocus();
            etCaregiverEmail_Sync.setError("Please enter the email correct format.");
            return false;
        }
        return true;
    }
}