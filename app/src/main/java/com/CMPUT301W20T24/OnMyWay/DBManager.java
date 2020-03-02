package com.CMPUT301W20T24.OnMyWay;

import android.app.Activity;
import android.util.Log;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.HashMap;
import java.util.Map;
import androidx.annotation.NonNull;


public class DBManager {
    private static volatile DBManager instance;
    private static final String TAG = "DEBUG";
    private FirebaseAuth auth;
//    private FirebaseUser currentUser;
    private User currentUser;
    static private FirebaseFirestore db;
    private LoginListener loginListener;
    private UserTypeCheckListener userTypeCheckListener;
    static private UserInfoPulledListener userInfoPulledListener;


    // Private constructor
    private DBManager() {
        if (instance != null) {
            throw new RuntimeException("Use getInstance() method to get the single instance of this class.");
        }
        else {
            auth = FirebaseAuth.getInstance();     // Initialize Firebase Auth
            currentUser = new User(auth.getCurrentUser());
            db = FirebaseFirestore.getInstance();  // Access a Cloud Firestore instance from your Activity
        }
    }


    public static DBManager getInstance() {
        if (instance == null) {
            // If there is no instance available, create new one
            synchronized (State.class) {
                // Check for the second time. If there is no instance available, create new one
                if (instance == null) {
                    instance = new DBManager();
                }
            }
        }
        return instance;
    }


    public void setLoginListener(LoginListener loginListener) {
        this.loginListener = loginListener;
    }


    public void setUserTypeCheckListener(UserTypeCheckListener userTypeCheckListener) {
        this.userTypeCheckListener = userTypeCheckListener;
    }


    static public void setUserInfoPulledListener(UserInfoPulledListener userInfoPulledListener2) {
//        this.userTypeCheckListener = userTypeCheckListener;
        userInfoPulledListener = userInfoPulledListener2;
    }


    public User getCurrentUser() {
        return currentUser;
    }


    /// https://firebase.google.com/docs/firestore/manage-data/add-data
    static public void setUserIsDriver(String userId, boolean isDriver) {
        Map<String, Object> isDriverObj = new HashMap<>();
        isDriverObj.put("isDriver", isDriver);

        db.collection("users").document(userId)
                .set(isDriverObj)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "isDriver updated successfully");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error updating isDriver", e);
                    }
                });
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
                                currentUser.setFirebaseUser(auth.getCurrentUser());
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
                                    if (document.getBoolean("isDriver")) {
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


    public void fetchCurrentUserInfo() {

    }


    /// Google Firebase, Get data with Cloud Firestore
    /// https://firebase.google.com/docs/firestore/query-data/get-data
    static public void fetchUserInfo(FirebaseUser currentFirebaseUser) {
        db.collection("users").document(currentFirebaseUser.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();

                            if (document.exists()) {
                                Log.d(TAG, "User information fetched from database");
                                Map<String, Object> userData = document.getData(); // SHOULD SAVE THIS TO STATE HERE WHILE WE HAVE THE INFORMATION
//                                User newUser = new User()


                                userInfoPulledListener.onUserInfoPulled(
                                        currentFirebaseUser,
                                        document.getBoolean("isDriver")
                                );  // Call listener when we are finished
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
