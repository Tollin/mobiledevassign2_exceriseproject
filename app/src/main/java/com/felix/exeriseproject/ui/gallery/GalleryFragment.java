package com.felix.exeriseproject.ui.gallery;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.felix.exeriseproject.R;

import org.json.JSONException;
import org.json.JSONObject;

public class GalleryFragment extends Fragment {

    private GalleryViewModel galleryViewModel;
    private static String SearchCountryUrl = "https://api.covid19api.com/countries";
    private RequestQueue queue = Volley.newRequestQueue(getContext());
    private ProgressBar pbSearchBar;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);
        pbSearchBar = root.findViewById(R.id.pbLoading2);
        return root;
    }

    private void SearchCountries(){
        StringRequest stringRequest = new StringRequest(Request.Method.GET, SearchCountryUrl,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        pbSearchBar.setVisibility(View.INVISIBLE);
                        try {
                            JSONObject jobject= new JSONObject(response);
                            JSONObject global = jobject.getJSONObject("Global");
                            Object newConfirm = global.getInt("NewConfirmed");

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
    }
}
