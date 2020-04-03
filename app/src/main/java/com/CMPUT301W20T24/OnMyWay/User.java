package com.CMPUT301W20T24.OnMyWay;

import android.util.Log;

import java.util.Locale;


/**
 * An class used to represent users of the app (either drivers or riders)
 * @author John
 */
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
        if (newFirstName == "" || newFirstName == null) {
            throw new IllegalArgumentException("First name can't be empty or null");
        }
        else {
            this.firstName = newFirstName;
        }
    }

    public void setLastName(String newLastName) {
        if (newLastName == "" || newLastName == null) {
            throw new IllegalArgumentException("Last name can't be empty or null");
        }
        else {
            this.lastName = newLastName;
        }
    }

    public void setEmail(String newEmail) {
        if (newEmail == "" || newEmail == null) {
            throw new IllegalArgumentException("Email address can't be empty or null");
        }
        else {
            this.email = newEmail;

            // When we get a new email address, also generate a new profile photo url
            setProfilePhotoUrl(newEmail);
        }
    }

    public void setPhone(String newPhoneNumber) {
        if (newPhoneNumber == "" || newPhoneNumber == null) {
            throw new IllegalArgumentException("Last name can't be empty or null");
        }
        else {
            this.phone = newPhoneNumber;
        }
    }

    // Set both ratings at once
    private void setRatings(int newUpRatings, int newTotalRatings) {
        this.upRatings = newUpRatings;
        this.totalRatings = newTotalRatings;
    }

    // Generate a unique profile photo for each user using Gravatar
    /**
     * Generates the link to a unique profile photo from a user's email address,
     * using the Gravatar service
     * @param emailAddress The email address of the user
     * @author John
     */
    private void setProfilePhotoUrl(String emailAddress) {
        this.profilePhotoUrl = "https://www.gravatar.com/avatar/" + Utilities.md5(emailAddress) + "?d=identicon&s=512";
        Log.d(TAG, "User profile photo url set to " + profilePhotoUrl);
    }

    /**
     * Adds a rating to the users profile. If isPositive is true, it adds a positive rating.
     * Otherwise it adds a negative rating. This way totalRatings are always updated along with
     * upRatings to prevent errors
     * @param isPositive A boolean indicating whether the rating is positive or negative
     * @author John
     */
    public void addRating(boolean isPositive) {
        upRatings += (isPositive) ? 1 : 0;
        ++totalRatings;
    }

    public String getUserId() {
        return userId;
    }

    /**
     * Returns true if the user is a driver and false otherwise
     * @return A boolean indicating whether the user is a driver or not
     * @author John
     */
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

    /**
     * Calculates and returns the user's 2-digit rating out of 5, using the number of upRatings
     * and totalRatings
     * @return A 2-digit string representing the user's rating out of 5
     * @author John
     */
    /// StackOverflow post by John P.
    /// Author: https://stackoverflow.com/users/1309401/john-p
    /// Answer: https://stackoverflow.com/questions/2538787/how-to-display-an-output-of-float-data-with-2-decimal-places-in-java
    public String getRating() {
        if (getTotalRatings() == 0) {
            return "0.0";
        }
        else {
            return String.format(Locale.US, "%.1f", (float) (getUpRatings() * 5) / (float) getTotalRatings());
        }
    }

    public int getUpRatings() {
        return upRatings;
    }

    public int getTotalRatings() {
        return totalRatings;
    }
}
