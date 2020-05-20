package com.felix.exeriseproject.ui.slideshow;

import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.felix.exeriseproject.R;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

public class SlideshowFragment extends Fragment  {

    private SlideshowViewModel slideshowViewModel;
    private Button btnFindLocation;
    private TextView tvLatitude;
    private TextView tvLongtitude;
    private FusedLocationProviderClient fusedLocationClient;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_slideshow, container, false);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        tvLatitude = root.findViewById(R.id.tvLatitude);
        tvLongtitude = root.findViewById(R.id.tvLongtitude);
        btnFindLocation = root.findViewById(R.id.btnFindLocation);
        btnFindLocation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fusedLocationClient.getLastLocation()
                        .addOnSuccessListener(new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                if(location != null){
                                    tvLatitude.setText(String.valueOf(location.getLatitude()));
                                    tvLongtitude.setText(String.valueOf(location.getLongitude()));
                                }else {
                                    Toast.makeText(getContext(), "don't retrieve any location", Toast.LENGTH_LONG).show();
                                }
                            }
                        })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
        return root;
    }

}
