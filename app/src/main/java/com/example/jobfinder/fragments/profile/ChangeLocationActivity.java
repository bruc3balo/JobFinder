package com.example.jobfinder.fragments.profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.jobfinder.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import static com.example.jobfinder.login.AdditionalLogin.LOCATION_PERMISSION_CODE;
import static com.example.jobfinder.models.Models.User.LOCATED_AT;
import static com.example.jobfinder.models.Models.User.POSITION_LOCATED_AT;
import static com.example.jobfinder.models.Models.User.USER_DB;

public class ChangeLocationActivity extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener, GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnMarkerDragListener, GoogleMap.OnInfoWindowClickListener, GoogleMap.OnMarkerClickListener, GoogleMap.OnMyLocationClickListener, GoogleMap.OnMapClickListener {

    private GoogleMap mMap;
    private EditText activityLocationTv;
    private Button saveLocationButton;
    private String residentLocation = "";
    private LatLng residentPosition;

    private String oldResidentLocation = "";
    private LatLng oldResidentPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_location);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.

        Bundle extra = getIntent().getExtras();
        if (extra != null) {
            oldResidentLocation = extra.get(LOCATED_AT).toString();
            oldResidentPosition = (LatLng) extra.get(POSITION_LOCATED_AT);
        }

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.activityMap);
        Objects.requireNonNull(mapFragment).getMapAsync(this);

        Toolbar changeLocationToolbar = findViewById(R.id.changeLocationToolbar);
        setSupportActionBar(changeLocationToolbar);
        changeLocationToolbar.setNavigationOnClickListener(v -> noChanges());

        activityLocationTv = findViewById(R.id.activityLocationTv);
        saveLocationButton = findViewById(R.id.saveLocationButton);
        saveLocationButton.setOnClickListener(v -> {
            if (oldResidentPosition.equals(residentPosition)) {
                noChanges();
            } else if (residentLocation.equals("")) {
                noChanges();
            } else {
                changeLocation(residentPosition, residentLocation);
            }
        });
    }

    private void noChanges() {
        Toast.makeText(ChangeLocationActivity.this, "No changes made", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void changeLocation(LatLng position, String location) {
        saveLocationButton.setEnabled(false);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> objectsMap = new HashMap<>();
        objectsMap.put(POSITION_LOCATED_AT, position);
        objectsMap.put(LOCATED_AT, location);
        db.collection(USER_DB).document(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).update(objectsMap).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(ChangeLocationActivity.this, "Location changed", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                saveLocationButton.setEnabled(true);
                Toast.makeText(ChangeLocationActivity.this, "Failed to change location", Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        LatLng sydney = new LatLng(-1.4, 34.1);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Kenya"));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_PERMISSION_CODE);
            return;
        }
        mMap.setMyLocationEnabled(true);
        mMap.setOnMapLongClickListener(this);
        mMap.setOnMyLocationButtonClickListener(this);
        mMap.setOnMyLocationClickListener(this);
        mMap.setOnMapClickListener(this);
        mMap.setOnMapLongClickListener(this);
        mMap.setOnMarkerClickListener(this);
        mMap.setOnMarkerDragListener(this);
        mMap.setOnInfoWindowClickListener(this);
        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.getUiSettings().setZoomGesturesEnabled(true);
    }


    @Override
    public void onMapLongClick(LatLng latLng) {
        mMap.addMarker(new MarkerOptions().position(latLng).draggable(true).title(getFromLocation(latLng)).snippet("Click to confirm location"));
        Toast.makeText(this, "Location : " + residentLocation + " Position : " + residentPosition, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onMyLocationButtonClick() {
        return false;
    }

    @Override
    public void onInfoWindowClick(Marker marker) {
        getFromMarker(marker);
    }

    @Override
    public void onMarkerDragStart(Marker marker) {

    }

    @Override
    public void onMarkerDrag(Marker marker) {

    }

    @Override
    public void onMarkerDragEnd(Marker marker) {
        getFromMarker(marker);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        return false;
    }

    @Override
    public void onMapClick(LatLng latLng) {

    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {

    }


    private String getFromMarker(Marker marker) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses;
        String errorMessage = "";
        try {
            addresses = geocoder.getFromLocation(marker.getPosition().latitude, marker.getPosition().longitude, 1);
            Address address = addresses.get(0);
            // ArrayList<String> addressFragments = new ArrayList<>();
            for (int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
                //    addressFragments.add(address.getAddressLine(i));
                residentLocation = address.getAddressLine(i);
                activityLocationTv.setText(residentLocation);
                residentPosition = new LatLng(marker.getPosition().latitude, marker.getPosition().longitude);
                Toast.makeText(this, String.valueOf(address.getAddressLine(i)), Toast.LENGTH_SHORT).show();
                Snackbar.make(findViewById(android.R.id.content), address.getAddressLine(i), Snackbar.LENGTH_LONG).show();

            }
        } catch (IOException ioException) {
            errorMessage = "Service not available";
            Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
        } catch (IllegalArgumentException illegalArgumentException) {
            errorMessage = "Invalid latLng";
            Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
        }

        return residentLocation;
    }

    private String getFromLocation(LatLng latLng) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses = null;
        String errorMessage = "";
        try {
            addresses = geocoder.getFromLocation(latLng.latitude, latLng.longitude, 1);
            Address address = addresses.get(0);
            //    ArrayList<String> addressFragments = new ArrayList<>();
            for (int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
                //    addressFragments.add(address.getAddressLine(i));
                residentLocation = address.getAddressLine(i);
                residentPosition = latLng;
                activityLocationTv.setText(residentLocation);
                Toast.makeText(this, residentLocation, Toast.LENGTH_SHORT).show();
            }
        } catch (IOException ioException) {
            errorMessage = "Service not available";
            Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
        } catch (IllegalArgumentException illegalArgumentException) {
            errorMessage = "Invalid latLng";
            Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show();
        }

        return residentLocation;
    }
}