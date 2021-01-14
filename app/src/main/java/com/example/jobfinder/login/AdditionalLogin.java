package com.example.jobfinder.login;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.jobfinder.MainActivity;
import com.example.jobfinder.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import static com.example.jobfinder.models.Models.User.LOCATED_AT;
import static com.example.jobfinder.models.Models.User.NO_PROFILE_IMAGE;
import static com.example.jobfinder.models.Models.User.POSITION_LOCATED_AT;
import static com.example.jobfinder.models.Models.User.PROFILE_IMAGE;
import static com.example.jobfinder.models.Models.User.SPECIALIZATION;
import static com.example.jobfinder.models.Models.User.USER_DB;

public class AdditionalLogin extends AppCompatActivity implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener, GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnInfoWindowClickListener, GoogleMap.OnMarkerDragListener, GoogleMap.OnMarkerClickListener, GoogleMap.OnMapClickListener, GoogleMap.OnMyLocationClickListener {

    public static final int GET_FROM_GALLERY = 1;
    public static final int LOCATION_PERMISSION_CODE = 2;
    public static final int STORAGE_PERMISSION_CODE = 3;
    private boolean backPressed;

    private ImageView additionalImage;
    private MapView additionalMap;
    private GoogleMap map;
    private EditText additionalLocationTv;
    private String residentLocation = "No Location";
    private LatLng residentPosition = new LatLng(0, 0); // 0,0 assumed to be null

    private final LinkedList<String> specializationList = new LinkedList<>();
    private Uri file = null;
    private String imageUri = "";

    private Map<String, Object> additionalDetails;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_additional_login);

        Button laterButton = findViewById(R.id.laterButton);
        laterButton.setOnClickListener(v -> later());

        Button getImageAdditional = findViewById(R.id.getImageAdditional);
        getImageAdditional.setOnClickListener(v -> galleryPermission());

        EditText specializationAdditionalField = findViewById(R.id.specializationAdditionalField);
        ImageButton addSpecializationButton = findViewById(R.id.addSpecializationButton);

        RecyclerView specializationAdditionalRv = findViewById(R.id.specializationAdditionalRv);
        specializationAdditionalRv.setLayoutManager(new LinearLayoutManager(this, RecyclerView.VERTICAL, false));
        ListRvAdapter listRvAdapter = new ListRvAdapter(this, specializationList);
        specializationAdditionalRv.setAdapter(listRvAdapter);

        addSpecializationButton.setOnClickListener(v -> {
            if (specializationList.contains(specializationAdditionalField.getText().toString())) {
                Toast.makeText(AdditionalLogin.this, "Already added", Toast.LENGTH_SHORT).show();
            } else {
                specializationList.add(specializationAdditionalField.getText().toString());
                specializationAdditionalField.setText("");
                listRvAdapter.notifyDataSetChanged();
            }
        });

        additionalImage = findViewById(R.id.additionalImage);
        additionalLocationTv = findViewById(R.id.additionalLocationTv);

        additionalMap = findViewById(R.id.additionalMap);
        additionalMap.onCreate(savedInstanceState);
        additionalMap.getMapAsync(this);

        Button saveButton = findViewById(R.id.saveButton);
        saveButton.setOnClickListener(v -> {
            saveButton.setEnabled(false);
            assembleMap();
        });

        backPressed = false;
    }

    private void assembleMap() {
        Toast.makeText(this, "Your details will be uploaded", Toast.LENGTH_SHORT).show();

        try {
            additionalDetails = new HashMap<>();
            additionalDetails.put(SPECIALIZATION, specializationList);
            additionalDetails.put(LOCATED_AT, residentLocation);
            additionalDetails.put(POSITION_LOCATED_AT, residentPosition);

            Toast.makeText(this, "" + additionalDetails, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        }

        new UploadAdditionalDetails().execute();
        later();
    }

    private void galleryPermission() {
        // checkSelfPermission(Manifest.permission_group.STORAGE);
        ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Not granted", Toast.LENGTH_SHORT).show();
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        } else {
            Toast.makeText(this, "Granted", Toast.LENGTH_SHORT).show();
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
            file = Uri.fromFile(getOutputMediaFile());
            startActivityForResult(new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI).putExtra(MediaStore.EXTRA_OUTPUT, file), GET_FROM_GALLERY);
        }
    }

    @Override
    public void onBackPressed() {
        if (!backPressed) {
            backPressed = true;
            Toast.makeText(this, "Are you sure you want to skip ?", Toast.LENGTH_SHORT).show();
        } else {
            later();
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        additionalMap.onStart();
    }

    @Override
    public void onResume() {
        super.onResume();
        backPressed = false;
        additionalMap.onResume();
    }

    @Override
    public void onStop() {
        super.onStop();
        additionalMap.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();
        additionalMap.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        additionalMap.onDestroy();
    }

    private void later() {
        Toast.makeText(AdditionalLogin.this.getApplicationContext(), "You can still fill the details in your profile page", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(AdditionalLogin.this, MainActivity.class));
        finish();
    }

    public static File getOutputMediaFile() {
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES), "CameraDemo");

        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }

        @SuppressLint("SimpleDateFormat") String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        return new File(mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + ".jpg");
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, LOCATION_PERMISSION_CODE);
            return;
        }
        map.setMyLocationEnabled(true);
        map.setOnMapLongClickListener(this);
        map.setOnMyLocationButtonClickListener(this);
        map.setOnMyLocationClickListener(this);
        map.setOnMapClickListener(this);
        map.setOnMapLongClickListener(this);
        map.setOnMarkerClickListener(this);
        map.setOnMarkerDragListener(this);
        map.setOnInfoWindowClickListener(this);
        map.getUiSettings().setZoomControlsEnabled(true);
        map.getUiSettings().setZoomGesturesEnabled(true);
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
                additionalLocationTv.setText(residentLocation);
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
                additionalLocationTv.setText(residentLocation);
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GET_FROM_GALLERY) {
            new Handler().postDelayed(() -> {
                if (resultCode == Activity.RESULT_OK) {
                    try {
                        assert data != null;
                        additionalImage.setImageURI(data.getData());
                        file = data.getData();
                        System.out.println("File : " + file + " Uri : " + data.getData());
                        Toast.makeText(this, "File : " + file + " Uri : " + data.getData(), Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    System.out.println("Failed to get Image");
                }
            }, 1000);
        }
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        map.addMarker(new MarkerOptions().position(latLng).draggable(true).title(getFromLocation(latLng)).snippet("Click to confirm location"));
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

    @SuppressLint("StaticFieldLeak")
    public class UploadAdditionalDetails extends AsyncTask<Void, Void, Void> {

        private String imageUri = "";

        @Override
        protected Void doInBackground(Void... voids) {
            uploadUserImage(file);
            return null;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        private void uploadUserImage(Uri imageFile) {
            StorageReference carBucket = FirebaseStorage.getInstance().getReference().child(PROFILE_IMAGE).child(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid());
            //  Uri file = Uri.fromFile(new File(String.valueOf(imageFile)));
            try {
                carBucket.putFile(file).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        carBucket.getDownloadUrl().addOnSuccessListener(uri -> {
                            imageUri = uri.toString();
                            additionalDetails.put(PROFILE_IMAGE, imageUri);
                            System.out.println("Stored link uri" + imageUri);
                        });
                        System.out.println("Uploaded " + imageFile.toString());
                    } else {
                        System.out.println("Failed to upload " + imageFile.toString());
                        additionalDetails.put(PROFILE_IMAGE, NO_PROFILE_IMAGE);
                    }
                    uploadAdditionalDetails(additionalDetails);
                });
            } catch (Exception e) {
                uploadAdditionalDetails(additionalDetails);
            }
        }

        private void uploadAdditionalDetails(Map<String, Object> objectMap) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            objectMap.put(PROFILE_IMAGE, imageUri);
            db.collection(USER_DB).document(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).update(objectMap).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    System.out.println("Successfully uploaded details");
                } else {
                    System.out.println("Failed to upload additional details");
                }
            });
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }
}