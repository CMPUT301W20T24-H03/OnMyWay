package com.CMPUT301W20T24.OnMyWay;

import android.util.Log;


public class User {
    private static final String TAG = "OMW/User";   // Use this tag for call Log.d()
    private String userId;
    private String firstName;
    private String lastName;
    private boolean driver;
    private String email;
    private String phone;
    private int upRatings;      // The number of positive ratings for a user
    private int totalRatings;   // The total number of ratings for a user
    private String profilePhotoUrl;


    public User(String userId, String firstName, String lastName, boolean driver, String email, String phone, int upRatings, int totalRatings) {
        setUserId(userId);
        setFirstName(firstName);
        setLastName(lastName);
        setDriver(driver);
        setEmail(email);
        setPhone(phone);
        setRatings(upRatings, totalRatings);
    }


    private void setUserId(String newUserId) {
        this.userId = newUserId;
    }

    private void setDriver(boolean driver) {
        this.driver = driver;
    }

    public void setFirstName(String newFirstName) {
        // Don't update if the input is null. We may want to keep the old values
        if (newFirstName != null) {
            this.firstName = newFirstName;
        }
    }

    public void setLastName(String newLastName) {
        // Don't update if the input is null. We may want to keep the old values
        if (newLastName != null) {
            this.lastName = newLastName;
        }
    }

    public void setEmail(String newEmail) {
        // Don't update if the input is null. We may want to keep the old values
        if (newEmail != null) {
            this.email = newEmail;

            // When we get a new email address, also generate a new profile photo url
            setProfilePhotoUrl(newEmail);
        }
    }

    public void setPhone(String newPhoneNumber) {
        // Don't update if the input is null. We may want to keep the old values
        if (newPhoneNumber != null) {
            this.phone = newPhoneNumber;
        }
    }

    // Set both ratings at once
    private void setRatings(int newUpRatings, int newTotalRatings) {
        this.upRatings = newUpRatings;
        this.totalRatings = newTotalRatings;
    }

    // Generate a unique profile photo for each user using Gravatar
    private void setProfilePhotoUrl(String emailAddress) {
        this.profilePhotoUrl = "https://www.gravatar.com/avatar/" + Utilities.md5(emailAddress) + "?d=identicon&s=512";
        Log.d(TAG, "User profile photo url set to " + profilePhotoUrl);
    }

    // Call to add a rating to the user. If isPositive is true, it adds a positive rating.
    // Otherwise it adds a negative rating
    public void addRating(boolean isPositive) {
        upRatings += (isPositive) ? 1 : 0;
        ++totalRatings;
    }

    public String getUserId() {
        return userId;
    }

    public boolean isDriver() {
        return driver;
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

    // Use upRatings and totalRatings to calculate the users rating out of 5

    /// StackOverflow post by John P.
    /// Author: https://stackoverflow.com/users/1309401/john-p
    /// Answer: https://stackoverflow.com/questions/2538787/how-to-display-an-output-of-float-data-with-2-decimal-places-in-java
    public String getRating() {
        if (getTotalRatings() == 0) {
            return "0.0";
        }
        else {
            return String.format("%.1f", (float) (getUpRatings() * 5) / (float) getTotalRatings());
        }
    }

    public int getUpRatings() {
        return upRatings;
    }

    public int getTotalRatings() {
        return totalRatings;
    }
}
