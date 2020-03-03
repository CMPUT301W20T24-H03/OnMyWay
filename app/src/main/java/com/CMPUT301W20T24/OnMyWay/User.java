package com.CMPUT301W20T24.OnMyWay;

import com.google.firebase.auth.FirebaseUser;


public class User {
    private static final String TAG = "OMW/User";
    private FirebaseUser firebaseUser;
    private String firstName;
    private String lastName;
    private boolean driver;
    private String email;
    private String phone;
    private int upRatings;
    private int totalRatings;
    private String profilePhotoUrl;


    public User(FirebaseUser firebaseUser, String firstName, String lastName, boolean driver, String email, String phone, int upRatings, int totalRatings) {
        setFirebaseUser(firebaseUser);
        setFirstName(firstName);
        setLastName(lastName);
        setDriver(driver);
        setEmail(email);
        setPhone(phone);
        setRatings(upRatings, totalRatings);
    }

    private void setFirebaseUser(FirebaseUser firebaseUser) {
        this.firebaseUser = firebaseUser;
    }


    private void setDriver(boolean driver) {
        this.driver = driver;
    }


    public void setFirstName(String newFirstName) {
        if (newFirstName != null) {
            this.firstName = newFirstName;
        }
    }

    public void setLastName(String newLastName) {
        if (newLastName != null) {
            this.lastName = newLastName;
        }
    }

    public void setEmail(String newEmail) {
        if (newEmail != null) {
            this.email = newEmail;
            setProfilePhotoUrl(newEmail);
        }
    }

    public void setPhone(String newPhoneNumber) {
        if (newPhoneNumber != null) {
            this.phone = newPhoneNumber;
        }
    }

    private void setRatings(int newUpRatings, int newTotalRatings) {
        this.upRatings = newUpRatings;
        this.totalRatings = newTotalRatings;
    }

    private void setProfilePhotoUrl(String emailAddress) {
        this.profilePhotoUrl = "https://www.gravatar.com/avatar/" + Utilities.md5(emailAddress) + "?d=identicon&s=512";
    }

    public void addRating(boolean isPositive) {
        upRatings += (isPositive) ? 1 : 0;
        ++totalRatings;
    }

    public FirebaseUser getFirebaseUser() {
        return firebaseUser;
    }

    public boolean isDriver() {
        return driver;
    }

    public String getUserID() {
        return firebaseUser.getUid();
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNumber() {
        return phone;
    }

    public String getProfilePhotoUrl() {
        return profilePhotoUrl;
    }

    /// StackOverflow post by John P.
    /// Author: https://stackoverflow.com/users/1309401/john-p
    /// Answer: https://stackoverflow.com/questions/2538787/how-to-display-an-output-of-float-data-with-2-decimal-places-in-java
    public String getRating() {
        return String.format("%.1f", (float) (upRatings * 5) / (float) totalRatings);
    }

    public int getUpRatings() {
        return upRatings;
    }

    public int getTotalRatings() {
        return totalRatings;
    }
}
