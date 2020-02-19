package com.CMPUT301W20T24.OnMyWay;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;
import java.util.Map;

public class SplashScreenActivity extends AppCompatActivity {
    private static final String TAG = "DEBUG";
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

        /// Google Firebase Docs, Get Started with Firebase Authentication on Android
        /// https://firebase.google.com/docs/auth/android/start
        FirebaseUser currentUser = mAuth.getCurrentUser(); // Check if user is signed in (non-null)

        int splashDuration = 1000; // How long the screen stays open, in ms
        long timeDifference = new Date().getTime() - startTime.getTime();

        /// StackOverflow post by Dullahan
        /// Author: https://stackoverflow.com/users/2509341/dullahan
        /// Answer: https://stackoverflow.com/questions/17237287/how-can-i-wait-for-10-second-without-locking-application-ui-in-android
        if (timeDifference < splashDuration) {
            Log.d(TAG, "Waiting for " + String.valueOf(2000 - timeDifference) + " more milliseconds");

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    if (currentUser == null) {
                        Log.d(TAG, "Go to login page");
                        Intent intent = new Intent(SplashScreenActivity.this, LoginActivity.class);
                        startActivity(intent);
                    }
                    else {
                        Log.d(TAG, "Check if driver or rider and go to map page");

                        FirebaseFirestore db = FirebaseFirestore.getInstance(); // Access a Cloud Firestore instance from your Activity

                        // COMBINE THIS WITH SAME FUNCTION IN LOGIN ACTIVITY

                        /// Google Firebase, Get data with Cloud Firestore
                        /// https://firebase.google.com/docs/firestore/query-data/get-data
                        db.collection("users").document(currentUser.getUid())
                                .get()
                                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                                        if (task.isSuccessful()) {
                                            DocumentSnapshot document = task.getResult();
                                            if (document.exists()) {
                                                Log.d(TAG, "User information fetched from database");
                                                Map<String, Object> userData = document.getData(); // SHOULD SAVE THIS TO STATE HERE WHILE WE HAVE THE INFORMATION

                                                if (document.getBoolean("driver")) {
                                                    // GO TO DRIVER MAP ACTIVITY
                                                    Log.d(TAG, "Switching to MainDriverMapActivity");
                                                }
                                                else {
                                                    // GO TO RIDER MAP ACTIVITY
                                                    Log.d(TAG, "Switching to MainRiderMapActivity");
                                                }
                                            }
                                            else {
                                                Log.d(TAG, "User not found in database");
                                            }
                                        }
                                        else {
                                            Log.d(TAG, "Get failed with ", task.getException());
                                        }
                                    }
                                });
                    }
                }
            }, splashDuration - timeDifference);
        }
    }
}
