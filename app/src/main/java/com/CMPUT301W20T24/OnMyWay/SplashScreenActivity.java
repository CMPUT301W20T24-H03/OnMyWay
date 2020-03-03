package com.CMPUT301W20T24.OnMyWay;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;
import java.util.Date;


public class SplashScreenActivity extends AppCompatActivity {
    private static final String TAG = "OMW/SplashScreenActi...";
    private Date startTime;
    private DBManager dbManager;


    /// Android Open Source Project, Get Started with Firebase Authentication on Android
    /// https://firebase.google.com/docs/auth/android/start?authuser=1
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        boolean isLoggedOut = getIntent().getBooleanExtra("isLoggedOut", false);
        Log.w(TAG, String.valueOf(isLoggedOut));

        if (isLoggedOut) {
            String message = "Logged out successfully";
            Log.w(TAG, message);
            Toast.makeText(SplashScreenActivity.this, message, Toast.LENGTH_SHORT).show();
        }

        startTime = new Date();
        dbManager = new DBManager();
    }


    @Override
    public void onStart() {
        super.onStart();

        int splashDuration = 200; // How long the screen stays open, in ms
        long timeDifference = new Date().getTime() - startTime.getTime();

        /// StackOverflow post by Dullahan
        /// Author: https://stackoverflow.com/users/2509341/dullahan
        /// Answer: https://stackoverflow.com/questions/17237287/how-can-i-wait-for-10-second-without-locking-application-ui-in-android
        if (timeDifference < splashDuration) {
            Log.d(TAG, "Waiting for " + String.valueOf(splashDuration - timeDifference) + " more milliseconds");

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    if (State.isLoggedIn()) {
                        dbManager.setCurrentUserInfoPulledListener(new CurrentUserInfoPulledListener() {
                            public void onCurrentUserInfoPulled() {
                                Log.d(TAG, "Info for current user pulled successfully");

                                // TODO: Check state and either go to rider map or driver map
                                if (State.getCurrentUser().isDriver()) {
                                    // Go to DriverMapActivity
                                    Log.d(TAG, "Switching to DriverMapActivity");
                                    Intent intent = new Intent(SplashScreenActivity.this, DriverMapActivity.class);
                                    startActivity(intent);
                                }
                                else {
                                    // Go to RiderMapActivity
                                    Log.d(TAG, "Switching to RiderMapActivity");
                                    Intent intent = new Intent(SplashScreenActivity.this, RiderMapActivity.class);
                                    startActivity(intent);
                                }
                            }
                        });

                        dbManager.fetchCurrentUserInfo();
                    }
                    else {
                        Log.d(TAG, "Go to login page");
                        Intent intent = new Intent(SplashScreenActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }
                }
            }, splashDuration - timeDifference);
        }
    }
}
