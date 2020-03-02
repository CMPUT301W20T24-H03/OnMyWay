package com.CMPUT301W20T24.OnMyWay;


// Keep track of global app state using a Singleton
// This is really terrible so replace with Room library or Shared Preferences later

/// Keval Patel via Medium, How to make the perfect Singleton?
/// https://medium.com/@kevalpatel2106/how-to-make-the-perfect-singleton-de6b951dfdb0
public class State {
    private static volatile State instance;
    private String userId;
    private boolean isDriver;


    // Private constructor
    private State() {
        if (instance != null) {
            throw new RuntimeException("Use getInstance() method to get the single instance of this class.");
        }
    }


    public static State getInstance() {
        if (instance == null) {
            // If there is no instance available, create new one
            synchronized (State.class) {
                // Check for the second time. If there is no instance available, create new one
                if (instance == null) {
                    instance = new State();
                }
            }
        }
        return instance;
    }


    public void setUserId(String userId) {
        this.userId = userId;
    }
    public String getUserId() {
        return userId;
    }


    public void setUserDriver() {
        this.isDriver = true;
    }
    public void setUserRider() {
        this.isDriver = false;
    }
    public boolean isUserDriver() {
        return isDriver;
    }
}