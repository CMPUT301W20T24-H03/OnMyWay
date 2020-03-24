package com.CMPUT301W20T24.OnMyWay;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;



/**
 * Manages communication with Firebase Auth and Firestore so other classes don't have to. This should probably be a singleton
 * @author John
 */

/// Keval Patel via Medium, How to make the perfect Singleton?
/// https://medium.com/@kevalpatel2106/how-to-make-the-perfect-singleton-de6b951dfdb0
public class OfflineManager {
    private static volatile OfflineManager instance;
    private Activity activity;    // Application activity so we can use SharedPrefs
    private String appId;

    // Initialize these here to save memory
//    private SharedPreferences currentUserSP;
//    private SharedPreferences currentRequestSP;


    // Private constructor
    private OfflineManager(Activity activity) {
        if (instance != null) {
            throw new RuntimeException("Use getInstance() method to get the single instance of this class.");
        }
        else {
            // Do initialization stuff here
            this.activity = activity;
            this.appId = activity.getPackageName();
        }
    }


    public static OfflineManager getInstance(Activity activity) {
        if (instance == null) {
            // If there is no instance available, create new one
            synchronized (OfflineManager.class) {
                // Check for the second time. If there is no instance available, create new one
                if (instance == null) {
                    instance = new OfflineManager(activity);
                }
            }
        }
        return instance;
    }


    // TODO: REWRITE DESCRIPTION
    // Generate file names
    private String getFileName(String id) {
        return appId + "." + id;
    }


    // TODO: REWRITE DESCRIPTION
    /**
     * Gets the info of the currently logged in user and saves it to State (phone, rating, etc).
     * This should be called after the user is logged in using Firebase Auth to get the user's
     * profile information
     *
     * @author John
     */
    public void fetchCurrentUserInfo() {
        // Fetch the user info of the current user and save to State
        // TODO: Should not save directly to State here because it will create a stack overflow
        State.setCurrentUser(fetchUserInfo(State.getCurrentUser().getUserId()));
    }


    // TODO: REWRITE DESCRIPTION
    /**
     * Gets the info of a user given their userId. onUserInfoPulled() listener is called with a
     * new User object after the method finishes
     *
     * @param userId A String. The userId of the user who's profile information we want to fetch
     * @author John
     */

    // Fetch the current user info from SharedPrefs
    public User fetchUserInfo(String userId) {
        SharedPreferences currentUserSP = activity.getSharedPreferences(
                appId + "." + userId,
                Context.MODE_PRIVATE
        );

        // Build a new User object with the information we fetched
        // Check each one to make sure its not null
        // Everything from SharedPrefs should be okay if we store it correctly
        User newUser = new User(
                userId,
                Utilities.checkStringNotNull(currentUserSP.getString("firstName", null)),
                Utilities.checkStringNotNull(currentUserSP.getString("lastName", null)),
                // No way to check if this boolean is null :(
                currentUserSP.getBoolean("isDriver", false),
                Utilities.checkStringNotNull(currentUserSP.getString("email", null)),
                Utilities.checkStringNotNull(currentUserSP.getString("phone", null)),
                Utilities.checkLongNotNull(currentUserSP.getLong("upRatings", -1)),
                Utilities.checkLongNotNull(currentUserSP.getLong("totalRatings", -1))
        );

        // TODO: Get profile photo from disk

        return newUser;
    }


    // TODO: REWRITE DESCRIPTION
    /**
     * Takes a User object and stores it in the Firestore database
     *
     * @param updatedUser The User we want to push to the database
     * @author John
     */
    // Upload the profile of the given user to FireStore. This does not affect Firebase Auth.

    /// Android Open Source Project, Save key-value data
    /// https://developer.android.com/training/data-storage/shared-preferences
    public void pushUserInfo(User updatedUser) {
        String userId = updatedUser.getUserId();
        SharedPreferences sharedPref = activity.getSharedPreferences(
                getFileName(userId),
                Context.MODE_PRIVATE
        );
        SharedPreferences.Editor editor = sharedPref.edit();

        editor.putString("userId", userId);
        editor.putString("firstName", updatedUser.getFirstName());
        editor.putString("lastName", updatedUser.getLastName());
        editor.putBoolean("isDriver", updatedUser.isDriver());
        editor.putString("email", updatedUser.getEmail());
        editor.putString("phone", updatedUser.getPhoneNumber());

        // We store ratings as integers here and fetch them as longs.
        // This should not be an issue
        editor.putInt("upRatings", updatedUser.getUpRatings());
        editor.putInt("totalRatings", updatedUser.getTotalRatings());

        editor.apply();
    }


    // Remove a specific document from SharedPreferences
    private void removeSharedPref(SharedPreferences sharedPrefs) {
        sharedPrefs.edit().clear().apply();
    }


    // TODO: REWRITE DESCRIPTION
    /**
     * Delete the given user from the Firestore database
     *
     * @author John
     */
    // Upload the profile of the given user to FireStore. This does not affect Firebase Auth.

    /// Google Firebase Docs, Delete data from Cloud Firestore
    /// https://firebase.google.com/docs/firestore/manage-data/delete-data
    public void deleteCurrentUser() {
//        removeSharedPref();
    }


    // TODO: REMOVE CURRENTUSER FROM SHAREDPREFERENCES
    public void logoutUser() {
        // TODO: Remove current user from SharedPrefs
    }
}