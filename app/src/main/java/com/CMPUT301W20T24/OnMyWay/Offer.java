package com.CMPUT301W20T24.OnMyWay;

public class Offer {

    private int requestID;
    private int driverID;

    public Offer(int requestID, int driverID) {
        this.requestID = requestID;
        this.driverID = driverID;
    }

    public int getRequestID() {
        return requestID;
    }

    public void setRequestID(int requestID) {
        this.requestID = requestID;
    }

    public int getDriverID() {
        return driverID;
    }

    public void setDriverID(int driverID) {
        this.driverID = driverID;
    }

}
