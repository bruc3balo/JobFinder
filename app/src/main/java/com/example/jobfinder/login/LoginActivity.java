package com.example.jobfinder.login;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.jobfinder.MainActivity;
import com.example.jobfinder.R;
import com.example.jobfinder.adapter.MainPageAdapter;
import com.example.jobfinder.login.fragments.LoginPageAdapter;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class LoginActivity extends AppCompatActivity {

    ViewPager viewPager;
    TabLayout tabLayout;
    private boolean backPressed;


    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        viewPager = findViewById(R.id.loginPager);
        tabLayout = findViewById(R.id.loginTabs);

        //ViewPager
        LoginPageAdapter pageAdapter = new LoginPageAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pageAdapter);

        //sync tabs and viewpager
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setupWithViewPager(viewPager, true);

        Objects.requireNonNull(tabLayout.getTabAt(0)).setText("Login");
        Objects.requireNonNull(tabLayout.getTabAt(1)).setText("Create");

        getWindow().setStatusBarColor(getColor(android.R.color.darker_gray));

        backPressed = false;

    }

    FirebaseAuth.AuthStateListener authStateListener = firebaseAuth -> updateUi(firebaseAuth.getCurrentUser());

    @Override
    protected void onStart() {
        FirebaseAuth.getInstance().addAuthStateListener(authStateListener);
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        backPressed = false;
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

    @Override
    protected void onDestroy() {
        FirebaseAuth.getInstance().removeAuthStateListener(authStateListener);
        super.onDestroy();
    }

    private void updateUi(FirebaseUser user) {
        if (user != null) {
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        } else {
            Toast.makeText(this, "Sign in or Create Account to continue", Toast.LENGTH_SHORT).show();
        }
    }

    public static String truncate(String value, int length) {
        // Ensure String length is longer than requested size.
        if (value.length() > length) {
            return value.substring(0, length);
        } else {
            return value;
        }
    }

}