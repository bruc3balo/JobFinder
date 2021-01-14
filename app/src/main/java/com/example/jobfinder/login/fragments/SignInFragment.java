package com.example.jobfinder.login.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.jobfinder.MainActivity;
import com.example.jobfinder.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignInFragment extends Fragment {

    public SignInFragment() {
        // Required empty public constructor
    }


    public static SignInFragment newInstance() {
        return new SignInFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View v = inflater.inflate(R.layout.fragment_sign_in, container, false);

        EditText email_field = v.findViewById(R.id.email_field);
        EditText pass_field = v.findViewById(R.id.pass_field);
        Button signInB = v.findViewById(R.id.signInB);

        signInB.setOnClickListener(v1 -> {
            if (validateForm(email_field, pass_field)) {
                signInUser(email_field.getText().toString(), pass_field.getText().toString(), signInB);
            } else {
                Toast.makeText(requireContext(), "Check details", Toast.LENGTH_SHORT).show();
            }
        });

        return v;
    }

    private boolean validateForm(EditText email, EditText pass) {
        boolean valid = false;

        if (!email.getText().toString().contains("@")) {
            email.setError("Wrong email format");
            email.requestFocus();
        } else if (pass.getText().toString().length() < 6) {
            pass.setError("Password is short");
            pass.requestFocus();
        } else {
            valid = true;
        }

        return valid;
    }

    private void signInUser(String email, String pass, Button signInB) {
        signInB.setEnabled(false);
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, pass).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                updateUi(user);
                Toast.makeText(requireContext(), "Welcome " + user.getEmail(), Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(requireContext(), task.getException().toString(), Toast.LENGTH_SHORT).show();
                signInB.setEnabled(true);
            }
        });
    }

    private void updateUi(FirebaseUser user) {
        if (user != null) {
            Toast.makeText(requireContext(), "Welcome " + user.getEmail(), Toast.LENGTH_SHORT).show();
            requireActivity().startActivity(new Intent(requireContext(), MainActivity.class));
            requireActivity().finish();
        } else {
            Toast.makeText(requireContext(), "Sign in to continue", Toast.LENGTH_SHORT).show();
        }
    }

}