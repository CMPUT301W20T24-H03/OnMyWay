package com.CMPUT301W20T24.OnMyWay;

import java.util.UUID;

/**
 * //Request model that holds the blueprint for this object type.
 * @author Manpreet Grewal, Bard Samimi
 */

public class Request {
    private String requestId;
    private String riderUserName;

    private String startLocationName;
    private double startLongitude;
    private double startLatitude;

    private String endLocationName;
    private double endLongitude;
    private double endLatitude;

    private String paymentAmount;
    private String driverUserName;
    private String status;

    /**
     * Constructor method required to instantiate an instance of the Request class.
     * @param startLongitude
     * @param startLatitude
     * @param endLongitude
     * @param endLatitude
     * @author Bard Samimi, Manpreet Grewal
     */
    public Request(double startLongitude, double startLatitude, double endLongitude, double endLatitude) {
        this.requestId = generateUUID();
        this.riderUserName = UserRequestState.getCurrentUser().getUserId();
        this.startLongitude = startLongitude;
        this.startLatitude = startLatitude;
        this.endLongitude = endLongitude;
        this.endLatitude = endLatitude;
        this.paymentAmount = "0";
        this.driverUserName = "NONE";
        this.status = "INCOMPLETE";
    }

    // TODO: THIS IS A CONSTRUCTOR FOR TESTING. SHOULD ADD LOCATION NAMES TO THE ORIGINAL CONSTRUCTOR SO WE CAN SHOW THEM TO THE USER
    /**
     * Constructor method required to instantiate an instance of the Request class.
     * @param riderId TODO
     * @param driverId TODO
     * @param startLocationName TODO
     * @param startLongitude TODO
     * @param startLatitude TODO
     * @param endLocationName TODO
     * @param endLongitude TODO
     * @param endLatitude TODO
     * @author Bard Samimi, Manpreet Grewal, John
     */
    public Request(
            String riderId,
            String driverId,
            String startLocationName,
            double startLongitude,
            double startLatitude,
            String endLocationName,
            double endLongitude,
            double endLatitude
    ) {
        this.requestId = generateUUID();
        this.riderUserName = riderId;
        this.driverUserName = driverId;

        setStartLocationName(startLocationName);
        this.startLocationName = startLocationName;
        this.startLongitude = startLongitude;
        this.startLatitude = startLatitude;

        setEndLocationName(endLocationName);
        this.endLocationName = endLocationName;
        this.endLongitude = endLongitude;
        this.endLatitude = endLatitude;

        this.paymentAmount = "0";
        this.status = "INCOMPLETE";
    }

    /**
     * Logic to generate a unique UUID string that will be used for the requestID value.
     * @return String
     * @author Manpreet Grewal
     */
    //https://www.baeldung.com/java-uuid
    //https://towardsdatascience.com/are-uuids-really-unique-57eb80fc2a87
    private String generateUUID() {
        UUID requestUUID = UUID.randomUUID();
        return this.requestId = requestUUID.toString();
    }

    // The remainder of the methods are all 'getter' and 'setter' method(s). Standard, and require no documentation.
    // Added by Bard Samimi
    public String getRequestId() { return requestId; }

    public String getRiderUserName() { return riderUserName; }

    public void setRiderUserName(String riderUserName) { this.riderUserName = riderUserName; }

    public String getStartLocationName() { return startLocationName; }

    private void setStartLocationName(String startLocationName) {
        this.startLocationName = startLocationName;
    }

    public double getStartLongitude() { return startLongitude; }

    public void setStartLongitude(double startLongitude) { this.startLongitude = startLongitude; }

    public double getStartLatitude() { return startLatitude; }

    public void setStartLatitude(double startLatitude) { this.startLatitude = startLatitude; }

    public String getEndLocationName() { return endLocationName; }

    private void setEndLocationName(String endLocationName) {
        this.endLocationName = endLocationName;
    }

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
