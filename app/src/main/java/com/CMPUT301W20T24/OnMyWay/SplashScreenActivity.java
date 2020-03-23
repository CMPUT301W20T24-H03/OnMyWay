package com.CMPUT301W20T24.OnMyWay;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.Toast;
import java.util.Date;


/**
 * This is the first screen the user sees. SplashScreenActivity if the user is logged in already.
 * If so, it goes the either RiderMapActivity or DriverMapActivity.
 * Otherwise, it goes to LoginActivity
 * @author John
 */
public class SplashScreenActivity extends AppCompatActivity {
    // The tag can't be longer than 23 characters so it is cut off
    private static final String TAG = "OMW/SplashScreenActi...";   // Use this tag for calling Log.d()
    private Date startTime;
    private DBManager dbManager;


    // Disable back button for this activity
    @Override
    public void onBackPressed() {
        // Literally nothing
    }


    // LONGPRESS BACK BUTTON TO GO BACK TO THE MAIN ACTIVITY FOR TESTING. REMOVE THIS LATER

    /// StackOverflow post by oemel09
    /// Author: https://stackoverflow.com/users/10827064/oemel09
    /// Answer: https://stackoverflow.com/questions/56913053/android-long-press-system-back-button-listener
    @Override
    public boolean onKeyLongPress(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Log.d(TAG, "Switching to MainActivity");
            Intent intent = new Intent(SplashScreenActivity.this, MainActivity.class);
            startActivity(intent);
            return true;
        }
        return super.onKeyLongPress(keyCode, event);
    }


    /// Android Open Source Project, Get Started with Firebase Authentication on Android
    /// https://firebase.google.com/docs/auth/android/start?authuser=1
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        // Get intent from parentActivity if it exists
        String toastMessage = getIntent().getStringExtra("toastMessage");

        // If the parent activity sent a message, display it in a toast to the user
        if (toastMessage != null && toastMessage != "") {
            Log.w(TAG, toastMessage);
            Toast.makeText(SplashScreenActivity.this, toastMessage, Toast.LENGTH_SHORT).show();
        }
        else {
            Log.d(TAG, "No message passed to SplashScreen");
        }

        startTime = new Date(); // Record the start time of the activity
        dbManager = new DBManager();
    }


    @Override
    public void onStart() {
        Log.d(TAG, "Before");
        super.onStart();
        Log.d(TAG, "After");

        // If user is logged in, fetch additional user info and go to a map activity
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
