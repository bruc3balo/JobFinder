package com.example.jobfinder.fragments.profile;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.jobfinder.R;
import com.example.jobfinder.login.AdditionalLogin;
import com.example.jobfinder.login.ListRvAdapter;
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
import com.makeramen.roundedimageview.RoundedImageView;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;

import static com.example.jobfinder.MainActivity.updateUi;
import static com.example.jobfinder.login.AdditionalLogin.GET_FROM_GALLERY;
import static com.example.jobfinder.login.AdditionalLogin.LOCATION_PERMISSION_CODE;
import static com.example.jobfinder.login.AdditionalLogin.STORAGE_PERMISSION_CODE;
import static com.example.jobfinder.login.AdditionalLogin.getOutputMediaFile;
import static com.example.jobfinder.models.Models.User.AVAILABLE;
import static com.example.jobfinder.models.Models.User.FIRST_NAME;
import static com.example.jobfinder.models.Models.User.LAST_NAME;
import static com.example.jobfinder.models.Models.User.LOCATED_AT;
import static com.example.jobfinder.models.Models.User.NO_PROFILE_IMAGE;
import static com.example.jobfinder.models.Models.User.PHONE_NUMBER;
import static com.example.jobfinder.models.Models.User.POSITION_LOCATED_AT;
import static com.example.jobfinder.models.Models.User.PROFILE_IMAGE;
import static com.example.jobfinder.models.Models.User.SPECIALIZATION;
import static com.example.jobfinder.models.Models.User.USER_DB;


public class ProfileFragment extends Fragment {

    TextView profileNameTv, emailNameTv, locationProfileTv, phoneNumberTv, averageRatingTv;
    SwitchCompat availabilitySwitch;
    private RoundedImageView profileImage;
    private Uri file;
    private ArrayAdapter<String> specAdapter;
    private LinkedList<String> specializationList = new LinkedList<>();
    private String fName = "", sName = "";
    private Bundle saveinsState;
    private LatLng currentLocation;

    public ProfileFragment() {
        // Required empty public constructor
    }

    public static ProfileFragment newInstance() {
        return new ProfileFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        saveinsState = savedInstanceState;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        final View v = inflater.inflate(R.layout.my_profile_layout, container, false);

        profileNameTv = v.findViewById(R.id.profileNameTv);
        emailNameTv = v.findViewById(R.id.emailNameTv);
        emailNameTv.setText(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getEmail());

        locationProfileTv = v.findViewById(R.id.locationProfileTv);
        phoneNumberTv = v.findViewById(R.id.phoneNumberTv);
        averageRatingTv = v.findViewById(R.id.averageRatingTv);

        profileImage = v.findViewById(R.id.profileImage);
        ImageButton changePicture = v.findViewById(R.id.changePicture);
        changePicture.setOnClickListener(v12 -> galleryPermission());

        availabilitySwitch = v.findViewById(R.id.availabilitySwitch);
        availabilitySwitch.setOnCheckedChangeListener((buttonView, isChecked) -> updateAvailability(isChecked));

        Button signOutB = v.findViewById(R.id.signOutB);
        signOutB.setOnClickListener(v1 -> {
            FirebaseAuth.getInstance().signOut();
            updateUi(FirebaseAuth.getInstance().getCurrentUser(), requireContext());
        });

        ListView specializationListProfile = v.findViewById(R.id.specializationListProfile);
        specAdapter = new ArrayAdapter<>(requireContext(), android.R.layout.simple_list_item_1, specializationList);
        specializationListProfile.setAdapter(specAdapter);
        specializationListProfile.setOnItemClickListener((parent, view, position, id) -> Toast.makeText(requireContext(), specializationList.get(position), Toast.LENGTH_SHORT).show());

        //Edits
        ImageButton nameEdit, locationEdit, phoneEdit, specializationEdit;

        nameEdit = v.findViewById(R.id.nameEdit);
        specializationEdit = v.findViewById(R.id.specializationEdit);
        phoneEdit = v.findViewById(R.id.phoneEdit);
        locationEdit = v.findViewById(R.id.locationEdit);

        //Click
        nameEdit.setOnClickListener(v14 -> {
            Dialog d = new Dialog(requireContext());
            d.setContentView(R.layout.name_dialog);
            EditText fNameField = d.findViewById(R.id.first_name_dialog);
            EditText sNameField = d.findViewById(R.id.second_name_dialog);
            Button saveNameButton = d.findViewById(R.id.saveNameButton);

            d.show();

            fNameField.setText(fName);
            sNameField.setText(sName);

            saveNameButton.setOnClickListener(v13 -> {
                if (fNameField.getText().toString().equals(fName) && sNameField.getText().toString().equals(sName)) {
                    Toast.makeText(requireContext(), "No changes made", Toast.LENGTH_SHORT).show();
                    d.dismiss();
                } else if (fNameField.getText().toString().isEmpty() || sNameField.getText().toString().isEmpty()) {
                    Toast.makeText(requireContext(), "Check fields", Toast.LENGTH_SHORT).show();
                } else {
                    changeName(fNameField.getText().toString(), sNameField.getText().toString());
                    d.dismiss();
                }
            });
        });
        locationEdit.setOnClickListener(v111 -> {
            requireActivity().startActivity(new Intent(requireContext(), ChangeLocationActivity.class).putExtra(LOCATED_AT, locationProfileTv.getText().toString()).putExtra(POSITION_LOCATED_AT, currentLocation));
        });
        phoneEdit.setOnClickListener(v16 -> {
            Dialog d = new Dialog(requireContext());
            d.setContentView(R.layout.number_dialog);

            EditText number = d.findViewById(R.id.phone_number_dialog);
            Button saveNumberB = d.findViewById(R.id.saveNumberButton);

            d.show();
            number.setText(phoneNumberTv.getText().toString());
            saveNumberB.setOnClickListener(v15 -> {
                if (number.getText().toString().equals(phoneNumberTv.getText().toString())) {
                    Toast.makeText(requireContext(), "No changes made", Toast.LENGTH_SHORT).show();
                    d.dismiss();
                } else if (number.getText().toString().length() < 10) {
                    number.setError("Number too short");
                } else {
                    changeNumber(number.getText().toString());
                    d.dismiss();
                }
            });
        });
        specializationEdit.setOnClickListener(v19 -> {

            Dialog d = new Dialog(requireContext());
            d.setContentView(R.layout.dialog_list);
            EditText specializationDialogField = d.findViewById(R.id.specializationDialogField);
            ImageButton addSpecializationDialogButton = d.findViewById(R.id.addSpecializationDialogButton);
            RecyclerView specializationDialogRv = d.findViewById(R.id.specializationDialogRv);
            Button saveSpec = d.findViewById(R.id.saveSpecializationButton);
            LinkedList<String> specList = new LinkedList<>();
            specList.addAll(specializationList);
            d.show();

            specializationDialogRv.setLayoutManager(new LinearLayoutManager(requireContext(), RecyclerView.VERTICAL, false));
            specializationDialogRv.setAdapter(new ListRvAdapter(requireContext(), specList));
            Objects.requireNonNull(specializationDialogRv.getAdapter()).notifyDataSetChanged();

            addSpecializationDialogButton.setOnClickListener(v17 -> {
                if (specList.contains(specializationDialogField.getText().toString())) {
                    Toast.makeText(requireContext(), "Already added", Toast.LENGTH_SHORT).show();
                } else {
                    specList.add(specializationDialogField.getText().toString());
                    specializationDialogField.setText("");
                    specializationDialogRv.getAdapter().notifyDataSetChanged();
                }
            });

            saveSpec.setOnClickListener(v18 -> {
                if (saveSpec.length() <= 0) {
                    Toast.makeText(requireContext(), "Cannot be empty", Toast.LENGTH_SHORT).show();
                    specializationDialogField.setError("Cannot be empty");
                } else if (specList.equals(specializationList)) {
                    Toast.makeText(requireContext(), "No changes made", Toast.LENGTH_SHORT).show();
                    d.dismiss();
                } else {
                    changeSpecialization(specList);
                    d.dismiss();
                }
            });

        });

        return v;
    }


    private void changeName(String fN, String sN) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> objectsMap = new HashMap<>();
        objectsMap.put(FIRST_NAME, fN);
        objectsMap.put(LAST_NAME, sN);
        db.collection(USER_DB).document(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).update(objectsMap).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(requireContext(), "Name changed successfully", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(requireContext(), "Failed to change name", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void changeNumber(String pN) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(USER_DB).document(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).update(PHONE_NUMBER, pN).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(requireContext(), "Number changed", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(requireContext(), "Failed to change number", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void changeSpecialization(List<String> specialization) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(USER_DB).document(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).update(SPECIALIZATION, specialization).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Toast.makeText(requireContext(), "Specialization updated", Toast.LENGTH_SHORT).show();
                System.out.println("Specialization updated");
            } else {
                Toast.makeText(requireContext(), "Failed to specialize", Toast.LENGTH_SHORT).show();
                System.out.println("Specialization failed to update");
            }
        });
    }

    private void galleryPermission() {
        // checkSelfPermission(Manifest.permission_group.STORAGE);
        ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(requireContext(), "Not granted", Toast.LENGTH_SHORT).show();
            ActivityCompat.requestPermissions(requireActivity(), new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        } else {
            Toast.makeText(requireContext(), "Granted", Toast.LENGTH_SHORT).show();
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
            file = Uri.fromFile(getOutputMediaFile());
            startActivityForResult(new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI).putExtra(MediaStore.EXTRA_OUTPUT, file), GET_FROM_GALLERY);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GET_FROM_GALLERY) {
            new Handler().postDelayed(() -> {
                if (resultCode == Activity.RESULT_OK) {
                    try {
                        assert data != null;
                        profileImage.setImageURI(data.getData());
                        file = data.getData();
                        new UpdateImageAsync().execute(file);
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
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        ProfileViewModel profileViewModel = new ViewModelProvider(this).get(ProfileViewModel.class);
        profileViewModel.getMyProfileData().observe(getViewLifecycleOwner(), user -> {
            fName = user.getFirstName();
            sName = user.getLastName();
            profileNameTv.setText(fName.concat(" ").concat(sName));
            emailNameTv.setText(user.getEmailAddress());
            phoneNumberTv.setText(String.valueOf(user.getPhoneNumber()));
            locationProfileTv.setText(user.getLocatedAt());
            Glide.with(this).load(user.getProfileImageUrl()).into(profileImage);
            toggleAvailability(user.isAvailable());
            currentLocation = user.getPositionLocatedAt();
            specializationList.clear();
            specializationList.addAll(user.getSpecialization());
            specAdapter.notifyDataSetChanged();
        });
        super.onActivityCreated(savedInstanceState);
    }

    private void updateAvailability(boolean availability) {
        availabilitySwitch.setEnabled(false);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(USER_DB).document(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).update(AVAILABLE, availability).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                availabilitySwitch.setEnabled(true);
                toggleAvailability(availability);
            } else {
                availabilitySwitch.setEnabled(true);
                Toast.makeText(requireContext(), "Failed to update status", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void toggleAvailability(boolean availability) {
        if (availability) {
            Toast.makeText(requireContext(), "Available", Toast.LENGTH_SHORT).show();
            availabilitySwitch.setCompoundDrawablesWithIntrinsicBounds(R.drawable.online_status, 0, 0, 0);
            availabilitySwitch.setText("Available");
            availabilitySwitch.setChecked(true);
        } else {
            Toast.makeText(requireContext(), "Busy", Toast.LENGTH_SHORT).show();
            availabilitySwitch.setCompoundDrawablesWithIntrinsicBounds(R.drawable.offline_status, 0, 0, 0);
            availabilitySwitch.setText("Busy");
            availabilitySwitch.setChecked(false);
        }
    }


    @SuppressLint("StaticFieldLeak")
    public class UpdateImageAsync extends AsyncTask<Uri, Void, Void> {
        private String imageUri = "";

        @Override
        protected Void doInBackground(Uri... uris) {
            uploadUserImage(uris[0]);
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
                            updateImage(imageUri);
                            System.out.println("Stored link uri" + imageUri);
                        });
                        System.out.println("Uploaded " + imageFile.toString());
                    } else {
                        System.out.println("Failed to upload " + imageFile.toString());
                    }
                });
            } catch (Exception e) {
                Toast.makeText(requireContext().getApplicationContext(), "Failed to upload picture", Toast.LENGTH_SHORT).show();
            }
        }

        private void updateImage(String imageUrl) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection(USER_DB).document(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).update(PROFILE_IMAGE, imageUrl).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    Toast.makeText(requireContext().getApplicationContext(), "Picture uploaded", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(requireContext().getApplicationContext(), "Failed to upload picture", Toast.LENGTH_SHORT).show();
                }
            });
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }

}