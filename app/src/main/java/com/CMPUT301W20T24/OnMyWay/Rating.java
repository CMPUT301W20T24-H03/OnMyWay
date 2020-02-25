package com.CMPUT301W20T24.OnMyWay;

public class Rating {

    private int userID;
    private int driverID;
    private int requestID;
    private boolean rating;

    public Rating(int userID, int driverID, int requestID, boolean rating) {
        this.userID = userID;
        this.driverID = driverID;
        this.requestID = requestID;
        this.rating = rating;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getDriverID() {
        return driverID;
    }

    public void setDriverID(int driverID) {
        this.driverID = driverID;
    }

    public int getRequestID() {
        return requestID;
    }

    public void setRequestID(int requestID) {
        this.requestID = requestID;
    }

    public boolean isRating() {
        return rating;
    }

    public void setRating(boolean rating) {
        this.rating = rating;
    }
}
