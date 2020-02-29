package com.CMPUT301W20T24.OnMyWay;

import android.app.Activity;
import android.util.Log;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.Map;
import androidx.annotation.NonNull;


public class DatabaseManager {
    private static final String TAG = "DEBUG";
    private FirebaseAuth auth;
    private FirebaseUser currentUser;
    private FirebaseFirestore db;
    private LoginListener loginListener;
    private UserTypeCheckListener userTypeCheckListener;


    public DatabaseManager() {
        auth = FirebaseAuth.getInstance();     // Initialize Firebase Auth
        currentUser = auth.getCurrentUser();
        db = FirebaseFirestore.getInstance();  // Access a Cloud Firestore instance from your Activity
    }


    public void setLoginListener(LoginListener loginListener) {
        this.loginListener = loginListener;
    }


    public void setUserTypeCheckListener(UserTypeCheckListener userTypeCheckListener) {
        this.userTypeCheckListener = userTypeCheckListener;
    }


    public FirebaseUser getCurrentUser() {
        return currentUser;
    }


    public boolean isLoggedIn() {
        return currentUser != null;
    }


    public void loginUser(String emailAddress, String password, Activity parentActivity) {
        Log.d(TAG, "Logging in user");

        /// Google Firebase Docs, Get Started with Firebase Authentication on Android
        /// https://firebase.google.com/docs/auth/android/start
        auth.signInWithEmailAndPassword(emailAddress, password)
                .addOnCompleteListener(parentActivity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (loginListener != null) {
                            if (task.isSuccessful()) {
                                currentUser = auth.getCurrentUser();
                                loginListener.onLoginSuccess();
                            }
                            else {
                                loginListener.onLoginFailure(task.getException());
                            }
                        }
                    }
                });
    }


    public void logoutUser() {
        FirebaseAuth.getInstance().signOut();
    }


    /// Google Firebase, Get data with Cloud Firestore
    /// https://firebase.google.com/docs/firestore/query-data/get-data
    public void checkUserType(FirebaseUser currentUser) {
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

                                if (userTypeCheckListener != null) {
                                    if (document.getBoolean("driver")) {
                                        userTypeCheckListener.onDriverLoggedIn();
                                    }
                                    else {
                                        userTypeCheckListener.onRiderLoggedIn();
                                    }
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
