package com.CMPUT301W20T24.OnMyWay;


public class State {
    private static final String TAG = "OMW/State";
    static private User currentUser;
    static private DBManager dbManager = new DBManager();


    static public User getCurrentUser() {
        return currentUser;
    }


    static public void setCurrentUser(User newUser) {
        if (currentUser == null) {
            currentUser = newUser;
        }
        // TODO: Throw an error here if we try to override a user
    }


    static public void updateCurrentUser() {
        dbManager.pushUserInfo(getCurrentUser());
    }


    static public boolean isLoggedIn() {
        return dbManager.getFirebaseUser() != null;
    }
}