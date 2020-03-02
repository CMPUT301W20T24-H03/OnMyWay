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
//    private static volatile DBManager instance;
    private static final String TAG = "OMW/DBManager";
    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private LoginListener loginListener;
    private CurrentUserInfoPulledListener currentUserInfoPulledListener;
    private UserInfoPulledListener userInfoPulledListener;


    public DBManager() {
        auth = FirebaseAuth.getInstance();     // Initialize Firebase Auth
        db = FirebaseFirestore.getInstance();  // Access a Cloud Firestore instance from your Activity
    }


    public void setLoginListener(LoginListener loginListener) {
        this.loginListener = loginListener;
    }


    public void setCurrentUserInfoPulledListener(CurrentUserInfoPulledListener currentUserInfoPulledListener) {
        this.currentUserInfoPulledListener = currentUserInfoPulledListener;
    }


    public void setUserInfoPulledListener(UserInfoPulledListener userInfoPulledListener) {
        this.userInfoPulledListener = userInfoPulledListener;
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
                                // TODO: Probably shouldn't call this because then it won't call
                                //  fetchCurrentUserInfo(). Somehow still working?
                                loginListener.onLoginSuccess();

                                fetchCurrentUserInfo();
                            }
                            else {
                                loginListener.onLoginFailure(task.getException());
                            }
                        }
                    }
                });
    }


    public FirebaseUser getFirebaseUser() {
        return auth.getCurrentUser();
    }


//    public void logoutUser() {
//        FirebaseAuth.getInstance().signOut();
//    }


    public void fetchCurrentUserInfo() {
        setUserInfoPulledListener(new UserInfoPulledListener() {
            @Override
            public void onUserInfoPulled(User currentUser) {
                State.setCurrentUser(currentUser);

                if (currentUserInfoPulledListener == null) {
                    Log.d(TAG, "No listeners are assigned for currentUserInfoPulledListener");
                }
                else {
                    currentUserInfoPulledListener.onCurrentUserInfoPulled();    // Call listener once user data is stored
                }
            }
        });

        fetchUserInfo(auth.getCurrentUser());
    }


    /// Google Firebase, Get data with Cloud Firestore
    /// https://firebase.google.com/docs/firestore/query-data/get-data
    public void fetchUserInfo(FirebaseUser firebaseUser) {
        db.collection("users").document(firebaseUser.getUid())
                .get()
                .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if (task.isSuccessful()) {
                            DocumentSnapshot document = task.getResult();

                            if (document.exists()) {
                                Log.d(TAG, "User information fetched from database");

                                User newUser = new User(
                                        firebaseUser,
                                        Utilities.checkStringNotNull(document.getString("firstName")),
                                        Utilities.checkStringNotNull(document.getString("lastName")),
                                        Utilities.checkBooleanNotNull(document.getBoolean("isDriver")),
                                        Utilities.checkStringNotNull(document.getString("email")),
                                        Utilities.checkStringNotNull(document.getString("phone")),
                                        Utilities.checkLongNotNull(document.getLong("upRatings")),
                                        Utilities.checkLongNotNull(document.getLong("totalRatings"))
                                );

                                if (userInfoPulledListener == null) {
                                    Log.d(TAG, "No listeners are assigned for userInfoPulledListener");
                                }
                                else {
                                    userInfoPulledListener.onUserInfoPulled(newUser);  // Call listener when we are finished
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


    /// Google Firebase Docs, Add data to Cloud Firestore
    /// https://firebase.google.com/docs/firestore/manage-data/add-data
    public void pushUserInfo(User updatedUser) {
        Map<String, Object> updatedUserObj = new HashMap<>();
        updatedUserObj.put("firstName", updatedUser.getFirstName());
        updatedUserObj.put("lastName", updatedUser.getLastName());
        updatedUserObj.put("isDriver", updatedUser.isDriver());
        updatedUserObj.put("email", updatedUser.getEmail());
        updatedUserObj.put("phone", updatedUser.getPhoneNumber());
        updatedUserObj.put("upRatings", updatedUser.getUpRatings());
        updatedUserObj.put("totalRatings", updatedUser.getTotalRatings());

        db.collection("users").document(updatedUser.getUserID())
                .set(updatedUserObj)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "User profile updated successfully");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error updating user profile", e);
                    }
                });
    }
}
