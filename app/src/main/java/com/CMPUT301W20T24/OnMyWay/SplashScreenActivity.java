package com.CMPUT301W20T24.OnMyWay;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;
import java.util.Date;


// This first screen the user sees. Checks if the user is logged in already.
// If so, go the either RiderMapActivity or DriverMapActivity
// Otherwise, go to LoginActivity
public class SplashScreenActivity extends AppCompatActivity {
    // The tag can't be longer than 23 characters so it is cut off
    private static final String TAG = "OMW/SplashScreenActi...";   // Use this tag for call Log.d()
    private Date startTime;
    private DBManager dbManager;


    /// Android Open Source Project, Get Started with Firebase Authentication on Android
    /// https://firebase.google.com/docs/auth/android/start?authuser=1
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        // Get intent from parentActivity if it exists
        boolean isLoggedOut = getIntent().getBooleanExtra("isLoggedOut", false);
        Log.w(TAG, String.valueOf(isLoggedOut));

        // If the parent activity says that the user just logged out, display a toast to the user
        // saying this
        if (isLoggedOut) {
            String message = "Logged out successfully";
            Log.w(TAG, message);
            Toast.makeText(SplashScreenActivity.this, message, Toast.LENGTH_SHORT).show();
        }

        startTime = new Date(); // Record the start time of the activity
        dbManager = new DBManager();
    }


    @Override
    public void onStart() {
        super.onStart();

        // If user is logged in, fetch addional user info and go to a map activity
        if (State.isLoggedIn()) {
            dbManager.setCurrentUserInfoPulledListener(new CurrentUserInfoPulledListener() {
                // This is called after fetchCurrentUserInfo() finishes
                public void onCurrentUserInfoPulled() {
                    Log.d(TAG, "Info for current user pulled successfully");

                    // Check state and either go to rider map or driver map
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

            dbManager.fetchCurrentUserInfo();   // Fetch additional info for the current user
        }
        else {
            Log.d(TAG, "Go to login page"); // Go to login page if user is not logged in
            Intent intent = new Intent(SplashScreenActivity.this, LoginActivity.class);
            startActivity(intent);
        }
    }
}
