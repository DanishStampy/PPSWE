package com.example.ppswe.view.patient.medicine;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
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
import com.example.ppswe.adapter.LoadingDialog;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Map;

public class SearchMedFragment extends Fragment {

    private Button btnToMedForm;
    private NavController navController;

    private EditText etSearchQuery;
    private ImageView btnImgSearchQuery, btnImgGuide;
    private ListView lvResultsQuery;
    private MaterialAlertDialogBuilder builder;

    private LoadingDialog loadingDialog;
    private static final String BASE_URL = "https://api.fda.gov/other/substance.json?search=names.name:";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_search_med, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        navController = Navigation.findNavController(view);
        // init loading dialog
        loadingDialog = new LoadingDialog(getContext());

        etSearchQuery = view.findViewById(R.id.etSearchKeyword);
        btnImgSearchQuery = view.findViewById(R.id.btnImgSearch);
        btnImgGuide = view.findViewById(R.id.imgViewGuideBtn);
        lvResultsQuery = view.findViewById(R.id.lvMedResult);

        btnImgGuide.setOnClickListener(view1 -> {
            navController.navigate(R.id.action_searchMedFragment_to_guideSearchFragment);
        });

        btnImgSearchQuery.setOnClickListener(view1 -> {

            loadingDialog.showDialog();
            String keyword = etSearchQuery.getText().toString().toUpperCase().trim();
            String url_submit = BASE_URL+keyword;

            StringRequest stringRequest = new StringRequest(Request.Method.GET, url_submit, new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {

                        JSONObject jObj = new JSONObject(response);
                        JSONArray nameArray = jObj.getJSONArray("results").getJSONObject(0).getJSONArray("names");

                        ArrayList<String> nameMedFDA = new ArrayList<>();

                        // retrieve names value from JSON
                        for (int i = 0; i < nameArray.length(); i++){
                            JSONObject name = nameArray.getJSONObject(i);
                            String medName = name.getString("name");
                            nameMedFDA.add(medName);
                            Log.d("med_name_fda", "This is " +medName);
                        }

                        // setup list view
                        setupListView(nameMedFDA);
                        Toast.makeText(getActivity(), "Found " + nameArray.length() + " result(s).", Toast.LENGTH_SHORT).show();

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            },
                    error -> {
                        loadingDialog.hideDialog();
                        Toast.makeText(getActivity(), "Not found. Please enter another medicine.", Toast.LENGTH_SHORT).show();
                        ArrayList<String> nameMedFDA = new ArrayList<>();
                        setupListView(nameMedFDA);
                    }) {
                @Nullable
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    return super.getParams();
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
        });

        btnToMedForm = view.findViewById(R.id.btnNextForm);
        btnToMedForm.setOnClickListener(view1 -> navController.navigate(R.id.action_searchMedFragment_to_medFormFragment));


    }

    private void setupListView(ArrayList<String> nameMedFDA) {
        loadingDialog.hideDialog();

        ArrayAdapter arrayAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_list_item_1, nameMedFDA);
        lvResultsQuery.setAdapter(arrayAdapter);

        lvResultsQuery.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                Bundle bundle = new Bundle();
                bundle.putString("med_name", nameMedFDA.get(i));

                navController.navigate(R.id.action_searchMedFragment_to_medFormFragment, bundle);
                //Toast.makeText(getActivity(), "Select " + nameMedFDA.get(i), Toast.LENGTH_SHORT).show();
            }
        });
    }
}