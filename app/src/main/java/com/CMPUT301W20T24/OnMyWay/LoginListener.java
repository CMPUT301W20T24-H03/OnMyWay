package com.CMPUT301W20T24.OnMyWay;


/**
 * Implement this interface and the methods will be called after login method finishes (before other user information is fetched)
 * @author John
 */
/// StackOverflow post by Rupesh
/// Author: https://stackoverflow.com/users/787438/rupesh
/// Answer: https://stackoverflow.com/questions/994840/how-to-create-our-own-listener-interface-in-android
public interface LoginListener {
    void onLoginSuccess();                      // Will be called if the login was successful
    void onLoginFailure(Exception exception);   // Will be called if the login fails
}
