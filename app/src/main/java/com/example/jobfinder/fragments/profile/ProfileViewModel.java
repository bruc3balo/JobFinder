package com.example.jobfinder.fragments.profile;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.jobfinder.models.Models;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.example.jobfinder.models.Models.User.AVAILABLE;
import static com.example.jobfinder.models.Models.User.CREATED_AT;
import static com.example.jobfinder.models.Models.User.EMAIL_ADDRESS;
import static com.example.jobfinder.models.Models.User.FIRST_NAME;
import static com.example.jobfinder.models.Models.User.LAST_NAME;
import static com.example.jobfinder.models.Models.User.LATITUDE;
import static com.example.jobfinder.models.Models.User.LOCATED_AT;
import static com.example.jobfinder.models.Models.User.LONGITUDE;
import static com.example.jobfinder.models.Models.User.PHONE_NUMBER;
import static com.example.jobfinder.models.Models.User.POSITION_LOCATED_AT;
import static com.example.jobfinder.models.Models.User.PROFILE_IMAGE;
import static com.example.jobfinder.models.Models.User.SPECIALIZATION;
import static com.example.jobfinder.models.Models.User.UID;
import static com.example.jobfinder.models.Models.User.USER_DB;

public class ProfileViewModel extends ViewModel {

    public ProfileViewModel() {
    }

    private MutableLiveData<Models.User> getMyProfileInfo() {
        MutableLiveData<Models.User> profileMutable = new MutableLiveData<>();
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(USER_DB).document(Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid()).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot ds = task.getResult();
                Models.User user = new Models.User();
                String not_found = "-";


                try {
                    String uid = Objects.requireNonNull(Objects.requireNonNull(ds).get(UID)).toString();
                    user.setUid(uid);
                } catch (Exception e) {
                    e.printStackTrace();
                    user.setUid(not_found);
                }

                try {
                    String fName = Objects.requireNonNull(Objects.requireNonNull(ds).get(FIRST_NAME)).toString();
                    user.setFirstName(fName);
                } catch (Exception e) {
                    e.printStackTrace();
                    user.setFirstName(not_found);
                }
                try {
                    String sName = Objects.requireNonNull(Objects.requireNonNull(ds).get(LAST_NAME)).toString();
                    user.setLastName(sName);
                } catch (Exception e) {
                    e.printStackTrace();
                    user.setLastName(not_found);
                }

                try {
                    String pNumber = Objects.requireNonNull(Objects.requireNonNull(ds).get(PHONE_NUMBER)).toString();
                    user.setPhoneNumber(pNumber);
                } catch (Exception e) {
                    e.printStackTrace();
                    user.setPhoneNumber(not_found);
                }
                try {
                    String email = Objects.requireNonNull(Objects.requireNonNull(ds).get(EMAIL_ADDRESS)).toString();
                    user.setEmailAddress(email);
                } catch (Exception e) {
                    e.printStackTrace();
                    user.setEmailAddress(not_found);
                }

                try {
                    String createdAt = Objects.requireNonNull(Objects.requireNonNull(ds).get(CREATED_AT)).toString();
                    user.setCreatedAt(createdAt);
                } catch (Exception e) {
                    e.printStackTrace();
                    user.setCreatedAt(not_found);
                }

                try {
                    boolean available = Boolean.parseBoolean(Objects.requireNonNull(Objects.requireNonNull(ds).get(AVAILABLE)).toString());
                    user.setAvailable(available);
                } catch (Exception e) {
                    user.setAvailable(true);
                    e.printStackTrace();
                }

                try {
                    String locatedAt = Objects.requireNonNull(Objects.requireNonNull(ds).get(LOCATED_AT)).toString();
                    user.setLocatedAt(locatedAt);
                } catch (Exception e) {
                    e.printStackTrace();
                    user.setLocatedAt(not_found);
                }

                try {
                    String profileImage = Objects.requireNonNull(Objects.requireNonNull(ds).get(PROFILE_IMAGE)).toString();
                    user.setProfileImageUrl(profileImage);
                } catch (Exception e) {
                    e.printStackTrace();
                    user.setProfileImageUrl(not_found);
                }

                try {
                    ArrayList<String> specialization = (ArrayList<String>) ds.get(SPECIALIZATION);
                    user.setSpecialization(specialization);
                } catch (Exception e) {
                    e.printStackTrace();
                    user.setSpecialization(Collections.singletonList(not_found));
                }

                try {
                    HashMap<String, Double> position = (HashMap<String, Double>) ds.get(POSITION_LOCATED_AT);
                    assert position != null;
                    LatLng positionLocatedAt = new LatLng(Double.parseDouble(Objects.requireNonNull(position.get(LATITUDE)).toString()), Double.parseDouble(Objects.requireNonNull(position.get(LONGITUDE)).toString()));
                    user.setPositionLocatedAt(positionLocatedAt);
                } catch (Exception e) {
                    e.printStackTrace();
                    user.setPositionLocatedAt(new LatLng(0, 0));
                }

                profileMutable.setValue(user);
            } else {
                profileMutable.setValue(new Models.User("Failed to get data"));
            }
        });
        return profileMutable;
    }

    public LiveData<Models.User> getMyProfileData() {
        return getMyProfileInfo();
    }
}
