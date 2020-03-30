package com.CMPUT301W20T24.OnMyWay;


/**
 * Implement this interface and the methods will be called after login method finishes (before other user information is fetched)
 * @author John
 */
/// StackOverflow post by Rupesh
/// Author: https://stackoverflow.com/users/787438/rupesh
/// Answer: https://stackoverflow.com/questions/994840/how-to-create-our-own-listener-interface-in-android
public interface CancelRideButtonListener {
    void onCancelClick();
}
