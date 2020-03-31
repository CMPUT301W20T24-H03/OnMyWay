package com.CMPUT301W20T24.OnMyWay;

public class MarkerStoreObject {

        private String documentId;
        private String driverUsername;
        private Double endLatitude;
        private Double endLongitude;
        private float paymentAmount;
        private String requestId;
        private String riderUsername;
        private Double startLatitude;
        private Double startLongitude;
        private String status;
        private String startAddressName;
        private String endAddressName;

    public MarkerStoreObject(String documentId, String driverUsername, Double endLatitude, Double endLongitude, float paymentAmount, String requestId, String riderUsername, Double startLatitude, Double startLongitude, String status, String startAddressName, String endAddressName) {
        this.documentId = documentId;
        this.driverUsername = driverUsername;
        this.endLatitude = endLatitude;
        this.endLongitude = endLongitude;
        this.paymentAmount = paymentAmount;
        this.requestId = requestId;
        this.riderUsername = riderUsername;
        this.startLatitude = startLatitude;
        this.startLongitude = startLongitude;
        this.status = status;
        this.startAddressName = startAddressName;
        this.endAddressName = endAddressName;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getDriverUsername() {
        return driverUsername;
    }

    public void setDriverUsername(String driverUsername) {
        this.driverUsername = driverUsername;
    }

    public Double getEndLatitude() {
        return endLatitude;
    }

    public void setEndLatitude(Double endLatitude) {
        this.endLatitude = endLatitude;
    }

    public Double getEndLongitude() {
        return endLongitude;
    }

    public void setEndLongitude(Double endLongitude) {
        this.endLongitude = endLongitude;
    }

    public float getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(float paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getRiderUsername() {
        return riderUsername;
    }

    public void setRiderUsername(String riderUsername) {
        this.riderUsername = riderUsername;
    }

    public Double getStartLatitude() {
        return startLatitude;
    }

    public void setStartLatitude(Double startLatitude) {
        this.startLatitude = startLatitude;
    }

    public Double getStartLongitude() {
        return startLongitude;
    }

    public void setStartLongitude(Double startLongitude) {
        this.startLongitude = startLongitude;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStartAddressName() {
        return startAddressName;
    }

    public void setStartAddressName(String startAddressName) {
        this.startAddressName = startAddressName;
    }

    public String getEndAddressName() {
        return endAddressName;
    }

    public void setEndAddressName(String endAddressName) {
        this.endAddressName = endAddressName;
    }
}
