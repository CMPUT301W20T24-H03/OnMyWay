package com.CMPUT301W20T24.OnMyWay;

import android.net.Uri;
import android.util.Log;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import androidx.annotation.NonNull;


public class User {
    private static final String TAG = "DEBUG";
    private FirebaseUser firebaseUser;
//    private DBManager dbManager;
    private boolean driver;
    private float rating;
    private int ratingcount;


    public User(FirebaseUser firebaseUser) {
        setFirebaseUser(firebaseUser);

//        dbManager = DBManager.getInstance();
        Log.d(TAG, "User created");
    }


    public User(FirebaseUser firebaseUser, boolean isDriver) {
        setFirebaseUser(firebaseUser);
        setDriver(isDriver);

//        dbManager = DBManager.getInstance();
    }


    public boolean isDriver() {
        return driver;
    }


    public void setDriver(boolean isDriver) {
        driver = isDriver;
        DBManager.setUserIsDriver(getUserID(), isDriver);
    }


    public String getUserID() {
        return firebaseUser.getUid();
    }


    public float getRating() {
        return rating;
    }


    // TODO: Implement setRating
    public void setRating() {

    }


    public int getRatingCount() {
        return ratingcount;
    }


    // TODO: Implement setRating
    public void setRatingCount() {

    }


    public String getName() {
        return firebaseUser.getDisplayName();
    }


    public void setName(String fullName) {
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(fullName).build();

        firebaseUser.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User name updated.");
                        }
                    }
                });
    }


    public void setProfilePhoto(String photoUrl) {
        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setPhotoUri(Uri.parse(photoUrl)).build();

        firebaseUser.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User profile photo updated");
                        }
                    }
                });
    }

    public String getProfilePhoto() {
        return firebaseUser.getPhotoUrl().toString();
    }


    public String getEmail() {
        return firebaseUser.getEmail();
    }


    // TODO: Send verification email
    public void setEmail(String email) {
        firebaseUser.updateEmail(email);
    }


    public String getPhoneNumber() {
        return firebaseUser.getPhoneNumber();
    }


    // TODO: Implement this later. A pain because we have to verify the phone number first
//    public void setPhoneNumber(String phoneNumber) {
//        firebaseUser.updatePhoneNumber(/* Phone number goes here */);
//    }


    public FirebaseUser getFirebaseUser() {
        return firebaseUser;
    }


    public void setFirebaseUser(FirebaseUser firebaseUser) {
        this.firebaseUser = firebaseUser;
    }


    /// https://firebase.google.com/docs/auth/android/manage-users
    private void pullUser() {
        DBManager.setUserInfoPulledListener(new UserInfoPulledListener() {
            public void onUserInfoPulled(FirebaseUser newFirebaseUser, boolean isDriver) {
                Log.d(TAG, "User info fetched");
                firebaseUser = newFirebaseUser;
                setDriver(isDriver);
            }
        });

//        dbManager.loginUser(emailAddress, password, this);
        DBManager.fetchUserInfo(getFirebaseUser());
    }
}
