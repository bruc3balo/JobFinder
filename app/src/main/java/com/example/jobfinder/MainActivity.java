package com.example.jobfinder;

import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager.widget.ViewPager;

import com.example.jobfinder.adapter.MainPageAdapter;
import com.example.jobfinder.login.LoginActivity;
import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseUser;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    ViewPager viewPager;
    TabLayout tabLayout;

    private boolean backPressed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        viewPager = findViewById(R.id.viewPager);
        tabLayout = findViewById(R.id.tabLayout);

        //ViewPager
        tabLayout = findViewById(R.id.tabLayout);

        MainPageAdapter pageAdapter = new MainPageAdapter(getSupportFragmentManager());
        viewPager.setAdapter(pageAdapter);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {

            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
                switch (tab.getPosition()) {
                    default:
                    case 0:
                        //toolbar.setBackgroundColor(ContextCompat.getColor(Home.this, R.color.colorAccent));
                        tabLayout.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.semiBlack));
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            getWindow().setStatusBarColor(getColor(R.color.semiBlack));

                        }
                        tab.setText(" ");

                        break;
                    case 1:
                        // toolbar.setBackgroundColor(ContextCompat.getColor(Home.this, R.color.colorHover));
                        tabLayout.setBackgroundColor(ContextCompat.getColor(MainActivity.this, android.R.color.darker_gray));
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            getWindow().setStatusBarColor(getColor(android.R.color.darker_gray));
                        }
                        tab.setText(" ");

                        break;

                    case 2:
                        tabLayout.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.semiGray));
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            getWindow().setStatusBarColor(getColor(R.color.semiGray));
                        }
                        tab.setText(" ");

                        break;

                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {
                tab.setText("");
            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        //sync tabs and viewpager
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setupWithViewPager(viewPager, true);

        Objects.requireNonNull(tabLayout.getTabAt(0)).setIcon(R.drawable.ic_user_profile_).setText("Profile");
        Objects.requireNonNull(tabLayout.getTabAt(1)).setIcon(R.drawable.ic_looking_for_job).setText("Job Market");
        Objects.requireNonNull(tabLayout.getTabAt(2)).setIcon(R.drawable.ic_bell_).setText("Notifications");

        backPressed = false;

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 2) {
            if (grantResults[0] < 0) {
                Toast.makeText(this, "Not granted", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Granted", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public static void updateUi(FirebaseUser user, Context context) {
        if (user != null) {
            System.out.println("Signed in");
            Toast.makeText(context, "Signed in", Toast.LENGTH_SHORT).show();
        } else {
            System.out.println("Signed out");
            Toast.makeText(context, "Signed out", Toast.LENGTH_SHORT).show();
            context.startActivity(new Intent(context, LoginActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_SINGLE_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK));
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @Override
    protected void onRestart() {
        super.onRestart();

    }


    @Override
    protected void onPause() {
        super.onPause();

    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

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
}