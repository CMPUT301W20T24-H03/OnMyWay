package com.CMPUT301W20T24.OnMyWay;

import java.util.UUID;

public class Request {
    private String requestId;
    private String riderUserName;

    private double startLongitude;
    private double startLatitude;

    private double endLongitude;
    private double endLatitude;

    private String paymentAmount;
    private String driverUserName;
    private String status;

    public Request(double startLongitude, double startLatitude, double endLongitude, double endLatitude) {
        this.requestId = generateUUID();
        this.riderUserName = riderUserName;
        this.startLongitude = startLongitude;
        this.startLatitude = startLatitude;
        this.endLongitude = endLongitude;
        this.endLatitude = endLatitude;
        this.paymentAmount = "0";
        this.driverUserName = driverUserName;
        this.status = "INCOMPLETE";
    }

    private String generateUUID() {
        UUID requestUUID = UUID.randomUUID();
        return this.requestId = requestUUID.toString();
    }

    public String getRequestId() { return requestId; }

    public String getRiderUserName() { return riderUserName; }

    public void setRiderUserName(String riderUserName) { this.riderUserName = riderUserName; }

    public double getStartLongitude() { return startLongitude; }

    public void setStartLongitude(double startLongitude) { this.startLongitude = startLongitude; }

    public double getStartLatitude() { return startLatitude; }

    public void setStartLatitude(double startLatitude) { this.startLatitude = startLatitude; }

    public double getEndLongitude() { return endLongitude; }

    public void setEndLongitude(double endLongitude) { this.endLongitude = endLongitude; }

    public double getEndLatitude() { return endLatitude; }

    public void setEndLatitude(double endLatitude) { this.endLatitude = endLatitude; }

    public String getPaymentAmount() { return paymentAmount; }

    public String setPaymentAmount(String paymentAmount) { return this.paymentAmount; }

    public String getDriverUserName() { return driverUserName; }

    public void setDriverUserName(String driverUserName) { this.driverUserName = driverUserName; }

    public String getStatus() { return status; }

    public void setStatus(String status) { this.status = status; }
}
