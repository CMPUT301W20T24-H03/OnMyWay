package com.CMPUT301W20T24.OnMyWay;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import java.util.Date;

public class SplashScreenActivity extends AppCompatActivity {
    private Date startTime;
    private FirebaseAuth mAuth;


    /// Android Open Source Project, Get Started with Firebase Authentication on Android
    /// https://firebase.google.com/docs/auth/android/start?authuser=1
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        startTime = new Date();

        mAuth = FirebaseAuth.getInstance(); // Initialize Firebase Auth
    }


    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();

        int splashDuration = 1000; // How long the screen stays open, in ms
        long timeDifference = new Date().getTime() - startTime.getTime();

        /// StackOverflow post by Dullahan
        /// Author: https://stackoverflow.com/users/2509341/dullahan
        /// Answer: https://stackoverflow.com/questions/17237287/how-can-i-wait-for-10-second-without-locking-application-ui-in-android
        if (timeDifference < splashDuration) {
            Log.d("DEBUG", "Waiting for " + String.valueOf(2000 - timeDifference));
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    if (currentUser == null) {
                        Log.d("DEBUG", "Go to login page");
                    }
                    else {
                        Log.d("DEBUG", "Check if driver or rider and go to map page");
                    }
                }
            }, splashDuration - timeDifference);
        }
    }
}
