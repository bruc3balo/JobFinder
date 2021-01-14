package com.example.jobfinder.fragments.job;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.jobfinder.NewJobActivity;
import com.example.jobfinder.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.material.floatingactionbutton.FloatingActionButton;



public class JobMarketFragment extends Fragment implements OnMapReadyCallback {

    private MapView jobSearchMap;
    private GoogleMap map;

    public JobMarketFragment() {
        // Required empty public constructor
    }


    public static JobMarketFragment newInstance() {
        return new JobMarketFragment();

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @SuppressLint("UseCompatTextViewDrawableApis")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View v = inflater.inflate(R.layout.job_explore_layout, container, false);

        jobSearchMap = v.findViewById(R.id.jobSearchMap);
        jobSearchMap.onCreate(savedInstanceState);
        jobSearchMap.getMapAsync(this);

        ImageButton previousJob = v.findViewById(R.id.previousJobB), nextJobB = v.findViewById(R.id.nextJobB);

        previousJob.setOnClickListener(v12 -> {
            previousJob.setImageTintList(ColorStateList.valueOf(Color.RED));
            new Handler().postDelayed(() -> previousJob.setImageTintList(ColorStateList.valueOf(Color.BLACK)), 300);
        });
        nextJobB.setOnClickListener(v13 -> {
            nextJobB.setImageTintList(ColorStateList.valueOf(Color.GREEN));
            new Handler().postDelayed(() -> nextJobB.setImageTintList(ColorStateList.valueOf(Color.BLACK)), 300);
        });

        FloatingActionButton newJobFab = v.findViewById(R.id.newJobFab);
        newJobFab.setOnClickListener(v1 -> requireActivity().startActivity(new Intent(requireContext(), NewJobActivity.class)));

        return v;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 2);
            return;
        }
        map.setMyLocationEnabled(true);
    }

    @Override
    public void onStart() {
        super.onStart();
        jobSearchMap.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        jobSearchMap.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
        jobSearchMap.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();
        jobSearchMap.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        jobSearchMap.onDestroy();
    }


}