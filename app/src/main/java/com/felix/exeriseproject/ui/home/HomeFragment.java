package com.felix.exeriseproject.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.felix.exeriseproject.R;

import org.json.JSONException;
import org.json.JSONObject;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private static String url ="https://api.covid19api.com/summary";
    private TextView tvNewConfirm;
    private TextView tvTotalConfirm;
    private TextView tvNewDeath;
    private TextView tvTotalDeath;
    private TextView tvNewRecover;
    private TextView tvTotalRecover;
    private Button refresh;
    private ProgressBar pbSearching;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        tvNewConfirm = root.findViewById(R.id.tvNewConfirm);
        tvTotalConfirm = root.findViewById(R.id.tvTotalConfirmed);
        tvNewDeath = root.findViewById(R.id.tvNewDeath);
        tvTotalDeath = root.findViewById(R.id.tvTotalDeath);
        tvNewRecover = root.findViewById(R.id.tvNewRecovered);
        tvTotalRecover = root.findViewById(R.id.tvTotalRecord);
        refresh = root.findViewById(R.id.btnRefresh);
        pbSearching= root.findViewById(R.id.pbSearching);
        LoadGlobalInfo();
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoadGlobalInfo();
            }
        });
        return root;
    }

    private void LoadGlobalInfo() {
        RequestQueue queue = Volley.newRequestQueue(getContext());
        pbSearching.setVisibility(View.VISIBLE);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pbSearching.setVisibility(View.INVISIBLE);
                        try {
                            JSONObject jobject= new JSONObject(response);
                            JSONObject global = jobject.getJSONObject("Global");
                            Object newConfirm = global.getInt("NewConfirmed");
                            tvNewConfirm.setText(String.valueOf(newConfirm));
                            int totalConfirm = global.getInt("TotalConfirmed");
                            tvTotalConfirm.setText(String.valueOf(totalConfirm));
                            tvNewDeath.setText(String.valueOf(global.getInt("NewDeaths")));
                            tvTotalDeath.setText(String.valueOf(global.getInt("TotalDeaths")));
                            tvNewRecover.setText(String.valueOf(global.getInt("NewRecovered")));
                            tvTotalRecover.setText(String.valueOf(global.getInt("TotalRecovered")));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                pbSearching.setVisibility(View.INVISIBLE);
                Toast.makeText(getContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        });

        queue.add(stringRequest);
    }
}
