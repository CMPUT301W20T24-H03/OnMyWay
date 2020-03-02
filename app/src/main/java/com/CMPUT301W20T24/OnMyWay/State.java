package com.CMPUT301W20T24.OnMyWay;


/// Keval Patel via Medium, How to make the perfect Singleton?
/// https://medium.com/@kevalpatel2106/how-to-make-the-perfect-singleton-de6b951dfdb0
public class State {
    private static volatile State instance;
    static private User currentUser;
    static private DBManager dbManager = new DBManager();


    // Private constructor
    private State() {
        if (instance != null) {
            throw new RuntimeException("Use getInstance() method to get the single instance of this class.");
        }
    }


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