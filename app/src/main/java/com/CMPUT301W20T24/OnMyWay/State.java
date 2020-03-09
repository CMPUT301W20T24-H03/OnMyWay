package com.CMPUT301W20T24.OnMyWay;


// Keeps track of the main application state. It holds the current user for now
// Should probably save this offline
public class State {
    private static final String TAG = "OMW/State";   // Use this tag for call Log.d()
    static private User currentUser;
    static private DBManager dbManager = new DBManager();


    // This will be null if user doesn't exist
    static public User getCurrentUser() {
        return currentUser;
    }


    // Set current user only if there isn't one already.
    // May have to change this if we save data offline
    static public void setCurrentUser(User newUser) {
        if (currentUser == null) {
            currentUser = newUser;
        }
        // TODO: Throw an error here if we try to override a user
    }


    // Calls DBManager to push new user info to FireStore
    static public void updateCurrentUser() {
        dbManager.pushUserInfo(getCurrentUser());
    }


    // Checks if there is a user logged in
    static public boolean isLoggedIn() {
        return dbManager.getFirebaseUser() != null;
    }


    static private void removeCurrentUser() {
        currentUser = null;
    }


    // This function works. Don't know if this should be here or in State
    static public void logoutUser() {
        removeCurrentUser();
        dbManager.logoutUser();
    }
}