package com.example.jobfinder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;

public class NewJobActivity extends AppCompatActivity implements OnMapReadyCallback {

    private MapView newJobMap;
    private GoogleMap map;
    private boolean backPressed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_job);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            getWindow().setStatusBarColor(getColor(R.color.semiBlack));
        }


        newJobMap = findViewById(R.id.newJobMap);
        newJobMap.onCreate(savedInstanceState);
        newJobMap.getMapAsync(this);

        backPressed = false;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, 2);
            return;
        }
        map.setMyLocationEnabled(true);
    }

    @Override
    public void onStart() {
        super.onStart();
        newJobMap.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        backPressed = false;
        newJobMap.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
        newJobMap.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();
        newJobMap.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        newJobMap.onDestroy();
    }


    @Override
    public void onBackPressed() {
        if (!backPressed) {
            backPressed = true;
            Toast.makeText(this, "Press again to exit", Toast.LENGTH_SHORT).show();
        } else {
            super.onBackPressed();
        }
    }


}