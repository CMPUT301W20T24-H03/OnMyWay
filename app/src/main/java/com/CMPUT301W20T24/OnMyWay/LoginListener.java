package com.CMPUT301W20T24.OnMyWay;


/// StackOverflow post by Rupesh
/// Author: https://stackoverflow.com/users/787438/rupesh
/// Answer: https://stackoverflow.com/questions/994840/how-to-create-our-own-listener-interface-in-android
public interface LoginListener {
    void onLoginSuccess();
    void onLoginFailure(Exception exception);
}
