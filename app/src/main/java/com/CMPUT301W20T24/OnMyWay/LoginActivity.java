package com.CMPUT301W20T24.OnMyWay;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Map;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "DEBUG";
    private FirebaseAuth mAuth;
    private EditText emailField;
    private EditText passwordField;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance(); // Initialize Firebase Auth

        emailField = findViewById(R.id.emailField);
        passwordField = findViewById(R.id.passwordField);
    }


    // Ignore presses of the back button
    @Override
    public void onBackPressed() {
       // Literally nothing
    }


    /// StackOverflow post by mindriot
    /// Author: https://stackoverflow.com/users/1011746/mindriot
    /// Answer: https://stackoverflow.com/questions/1819142/how-should-i-validate-an-e-mail-address
    private boolean validateEmail(CharSequence emailAddressChars) {
        return !TextUtils.isEmpty(emailAddressChars) && android.util.Patterns.EMAIL_ADDRESS.matcher(emailAddressChars).matches();
    }


    private boolean validatePassword(CharSequence passwordChars) {
        return true; // Implement this later
    }


    // Check if user is driver or rider here
    private void checkUserType(FirebaseUser currentUser) {
        currentUser.getUid();
        Log.d(TAG, "Logged in successfully. Checking user type");
        Log.d(TAG, currentUser.toString());
        Log.d(TAG, currentUser.getUid());

        FirebaseFirestore db = FirebaseFirestore.getInstance(); // Access a Cloud Firestore instance from your Activity

        // COMBINE THIS WITH SAME FUNCTION IN SPLASH SCREEN ACTIVITY

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
                                     Log.d(TAG, "Switching to DriverMapActivity");
                                     Intent intent = new Intent(LoginActivity.this, DriverMapActivity.class);
                                     startActivity(intent);
                                 }
                                 else {
                                     // GO TO RIDER MAP ACTIVITY
                                     Log.d(TAG, "Switching to RiderMapActivity");
                                     Intent intent = new Intent(LoginActivity.this, RiderMapActivity.class);
                                     startActivity(intent);
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


    private void showLoginError(Exception exception) {
        Log.w(TAG, "signInWithEmail:failure", exception);
        Toast.makeText(LoginActivity.this, "Authentication failed.", Toast.LENGTH_SHORT).show();
    }



    private void loginUser(String emailAddress, String password) {
        Log.d(TAG, "Logging in user");

        /// Google Firebase Docs, Get Started with Firebase Authentication on Android
        /// https://firebase.google.com/docs/auth/android/start
        mAuth.signInWithEmailAndPassword(emailAddress, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            checkUserType(mAuth.getCurrentUser());
                        }
                        else {
                            showLoginError(task.getException());
                        }
                    }
                });
    }


    public void onLoginButtonPressed(View view) {
        CharSequence emailAddressChars = emailField.getText();
        CharSequence passwordChars = passwordField.getText();

        if (validateEmail(emailAddressChars)) {
            if (validatePassword(passwordChars)) {
                loginUser(emailAddressChars.toString(), passwordChars.toString());
            }
            else {
                Log.d(TAG, "Password is not valid");
            }
        }
        else {
            Log.d(TAG, "Email address is not valid");
        }
    }
}
