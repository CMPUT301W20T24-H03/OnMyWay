package com.CMPUT301W20T24.OnMyWay;

import android.location.Geocoder;

import java.time.LocalTime;


public class Request {
    private int requestId;
    private String riderUserName;
    private float[] startLocation;
    private float[] endLocation;
    private float paymentAmount;
    private String driverUserName;
    private String status;
    private LocalTime timeReceived;
    private LocalTime timeAccepted;

    public Request(int requestId, String riderUserName, float[] startLocation, float[] endLocation, float paymentAmount, String driverUserName, String status, LocalTime timeReceived, LocalTime timeAccepted) {
        this.requestId = requestId;
        this.riderUserName = riderUserName;
        this.startLocation = startLocation;
        this.endLocation = endLocation;
        this.paymentAmount = paymentAmount;
        this.driverUserName = driverUserName;
        this.status = status;
        this.timeReceived = timeReceived;
        this.timeAccepted = timeAccepted;
    }

    public int getRequestId() {
        return requestId;
    }

    public void setRequestId(int requestId) {
        this.requestId = requestId;
    }

    public String getRiderUserName() {
        return riderUserName;
    }

    public void setRiderUserName(String riderUserName) {
        this.riderUserName = riderUserName;
    }

    public float[] getStartLocation() {
        return startLocation;
    }

    public void setStartLocation(float[] startLocation) {
        this.startLocation = startLocation;
    }

    public float[] getEndLocation() {
        return endLocation;
    }

    public void setEndLocation(float[] endLocation) {
        this.endLocation = endLocation;
    }

    public float getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(float paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public String getDriverUserName() {
        return driverUserName;
    }

    public void setDriverUserName(String driverUserName) {
        this.driverUserName = driverUserName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public LocalTime getTimeReceived() {
        return timeReceived;
    }

    public void setTimeReceived(LocalTime timeReceived) {
        this.timeReceived = timeReceived;
    }

    public LocalTime getTimeAccepted() {
        return timeAccepted;
    }

    public void setTimeAccepted(LocalTime timeAccepted) {
        this.timeAccepted = timeAccepted;
    }
}
