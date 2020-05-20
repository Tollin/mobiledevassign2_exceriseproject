package com.felix.exeriseproject.ui.gallery;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.felix.exeriseproject.CountryModel;
import com.felix.exeriseproject.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GalleryFragment extends Fragment {

    private static String SearchCountryUrl = "https://api.covid19api.com/countries";
    private static String SummaryUrl ="https://api.covid19api.com/summary";
    private RequestQueue queue;
    private ProgressBar pbSearchBar;
    private AutoCompleteTextView actvCounties;
    private List<CountryModel> countries;
    private Button btnSearch;
    private String currentCountryName;
    private TextView tvNewConfirm;
    private TextView tvTotalConfirm;
    private TextView tvNewDeath;
    private TextView tvTotalDeath;
    private TextView tvRecover;
    private TextView tvTotalRecover;
    private TextView tvDate;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);
        queue = Volley.newRequestQueue(getContext());
        pbSearchBar = root.findViewById(R.id.pbLoading2);
        actvCounties = root.findViewById(R.id.actvCounties);
        btnSearch = root.findViewById(R.id.btnSearch2);

        actvCounties.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                currentCountryName = parent.getItemAtPosition(position).toString();
            }
        });
        tvNewConfirm = root.findViewById(R.id.tvNewConfirm2);
        tvTotalConfirm = root.findViewById(R.id.tvTotalConfirm2);
        tvNewDeath = root.findViewById(R.id.tvNewDeath2);
        tvTotalDeath = root.findViewById(R.id.tvTotalDeath2);
        tvRecover = root.findViewById(R.id.tvNewRecover2);
        tvTotalRecover = root.findViewById(R.id.tvTotalRecover2);
        tvDate = root.findViewById(R.id.tvDate2);
        SearchCountries();
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(currentCountryName == null || currentCountryName == ""){
                    return;
                }
                SearchOneCountrySummary(currentCountryName);
            }
        });
        return root;
    }

    private void SearchOneCountrySummary(final String countryName){
        StringRequest stringRequest = new StringRequest(Request.Method.GET, SummaryUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pbSearchBar.setVisibility(View.INVISIBLE);
                        try {
                            JSONObject jobject= new JSONObject(response);
                            JSONArray countries = jobject.getJSONArray("Countries");
                            int lenght = countries.length();
                            for (int i = 0; i < lenght; i++) {
                                JSONObject jObject = countries.getJSONObject(i);
                                String thisCountryName = jObject.getString("Country");
                                if( countryName.equalsIgnoreCase(thisCountryName)){
                                    SetTextViewText(tvNewConfirm, "NewConfirmed", jObject);
                                    SetTextViewText(tvTotalConfirm, "TotalConfirmed", jObject);
                                    SetTextViewText(tvNewDeath, "NewDeaths", jObject);
                                    SetTextViewText(tvTotalDeath, "TotalDeaths", jObject);
                                    SetTextViewText(tvRecover, "NewRecovered", jObject);
                                    SetTextViewText(tvTotalRecover, "TotalRecovered", jObject);
                                    String dataStr = null;
                                    try {
                                        dataStr = jObject.getString("Date");
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                    tvDate.setText(dataStr.substring(0, 10));
                                    break;
                                }

                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                byte[] errormessage = error.networkResponse.data;
                String msg = new String(errormessage);
                pbSearchBar.setVisibility(View.INVISIBLE);
                Toast.makeText(getContext(), msg, Toast.LENGTH_LONG).show();
            }
        });
        queue.add(stringRequest);
    }

    private void SetTextViewText(TextView tv, String key, JSONObject jObject){
        try {
            tv.setText(String.valueOf(jObject.getInt(key)));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void SearchCountries() {
        StringRequest stringRequest = new StringRequest(Request.Method.GET, SearchCountryUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pbSearchBar.setVisibility(View.INVISIBLE);
                        try {
                            JSONArray array = new JSONArray(response);
                            int lenght = array.length();
                            countries = new ArrayList<>(lenght);
                            String[] countryNames = new String[lenght];
                            for (int i = 0; i < lenght; i++) {
                                JSONObject jObject = array.getJSONObject(i);
                                String countryName = jObject.getString("Country");
                                countryNames[i] = countryName;
                                countries.add(new CountryModel(countryName, jObject.getString("Slug"), jObject.getString("ISO2")));
                            }
                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.select_dialog_item, countryNames);
                            actvCounties.setAdapter(adapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pbSearchBar.setVisibility(View.INVISIBLE);
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
        queue.add(stringRequest);
    }
}
