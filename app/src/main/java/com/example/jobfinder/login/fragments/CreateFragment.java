package com.example.jobfinder.login.fragments;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.jobfinder.R;
import com.example.jobfinder.login.AdditionalLogin;
import com.example.jobfinder.models.Models;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Calendar;
import java.util.Objects;

import static com.example.jobfinder.models.Models.User.USER_DB;


public class CreateFragment extends Fragment {

    private final Models.User userModel = new Models.User();

    public CreateFragment() {
        // Required empty public constructor
    }


    public static CreateFragment newInstance() {
        return new CreateFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View v = inflater.inflate(R.layout.fragment_create, container, false);

        EditText fNameField = v.findViewById(R.id.first_name_field);
        EditText sNameField = v.findViewById(R.id.second_name_field);
        EditText emailField = v.findViewById(R.id.email_address_field);

        EditText phone_number_field = v.findViewById(R.id.phone_number_field);
        EditText password_field = v.findViewById(R.id.password_field);
        EditText confirm_password_field = v.findViewById(R.id.confirm_password_field);

        Button createButton = v.findViewById(R.id.createAccountButton);
        createButton.setOnClickListener(v1 -> {
            if (validateForm(fNameField, sNameField, emailField, phone_number_field, password_field, confirm_password_field)) {
                createUserWithFireBase(emailField.getText().toString(), confirm_password_field.getText().toString(), createButton);
            } else {
                Toast.makeText(requireContext(), "Check details", Toast.LENGTH_SHORT).show();
            }
        });

        return v;
    }

    private boolean validateForm(EditText fName, EditText sName, EditText email, EditText phone, EditText password, EditText confirmPassword) {
        boolean valid = false;
        if (fName.getText().toString().isEmpty()) {
            fName.setError("Required");
            fName.requestFocus();
        } else if (sName.getText().toString().isEmpty()) {
            sName.setError("Required");
            sName.requestFocus();
        } else if (!email.getText().toString().contains("@")) {
            email.setError("Wrong format");
            email.requestFocus();
        } else if (phone.getText().toString().length() < 10) {
            phone.setError("Too short");
            phone.requestFocus();
        } else if (!confirmPassword.getText().toString().equals(password.getText().toString())) {
            confirmPassword.setError("Passwords don't match");
            confirmPassword.requestFocus();
        } else if (confirmPassword.getText().toString().length() < 6) {
            confirmPassword.setError("Password is less than 6");
            confirmPassword.requestFocus();
        } else {
            userModel.setEmailAddress(email.getText().toString());
            userModel.setFirstName(fName.getText().toString());
            userModel.setLastName(sName.getText().toString());
            userModel.setCreatedAt(Calendar.getInstance().getTime().toString());
            userModel.setPhoneNumber(phone.getText().toString());
            valid = true;
        }
        return valid;
    }

    private void createUserWithFireBase(String email, String password, Button create) {
        create.setEnabled(false);
        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                assert user != null;
                userModel.setUid(user.getUid());
                new saveDetailsAsyncTask().execute(userModel);
                updateUi(user);
                Toast.makeText(requireContext(), "Created account with " + user.getEmail(), Toast.LENGTH_SHORT).show();
            } else {
                create.setEnabled(true);
                Toast.makeText(requireContext(), Objects.requireNonNull(task.getException()).toString(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void updateUi(FirebaseUser user) {
        if (user != null) {
            Toast.makeText(requireContext(), "We require additional info to continue", Toast.LENGTH_SHORT).show();
            requireActivity().startActivity(new Intent(requireContext(), AdditionalLogin.class));
            requireActivity().finish();
        } else {
            Toast.makeText(requireContext(), "Create Account or sign in to continue", Toast.LENGTH_SHORT).show();
        }
    }

    public static class saveDetailsAsyncTask extends AsyncTask<Models.User, Void, Void> {

        public saveDetailsAsyncTask() {

        }

        @Override
        protected void onPreExecute() {
            System.out.println("Saving info");
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Models.User... users) {
            saveUserDetails(users[0]);
            return null;
        }

        private void saveUserDetails(Models.User user) {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection(USER_DB).document(user.getUid()).set(user).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    System.out.println("Details saved");
                } else {
                    System.out.println("Details failed to save");
                    Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).delete().addOnCompleteListener(task1 -> {
                        if (task1.isSuccessful()) {
                            System.out.println("Create an account again");
                        } else {
                            System.out.println("Failed to clear account. Use another");
                        }
                    });
                }
            });
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            System.out.println("Saved info");
            super.onPostExecute(aVoid);
        }
    }
}