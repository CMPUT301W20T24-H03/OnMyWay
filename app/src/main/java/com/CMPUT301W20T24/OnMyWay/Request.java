package com.CMPUT301W20T24.OnMyWay;

import java.util.UUID;

/**
 * //Request model that holds the blueprint for this object type.
 * @author Manpreet Grewal, Bard Samimi
 */

public class Request {
    private static final String TAG = "OMW/Request";  // Use this tag for calling Log.d()

    private String requestId;
    private String riderId;
    private String driverId;

    private String startLocationName;
    private double startLongitude;
    private double startLatitude;

    private String endLocationName;
    private double endLongitude;
    private double endLatitude;

    private String paymentAmount;
    private String status;

    private RequestTime timeCreated;  // The time when the request was created by a rider
    private RequestTime timeAccepted;  // The time when the request was accepted by a driver


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
     * @param paymentAmount TODO
     * @param status TODO
     * @param timeCreated TODO
     * @param timeAccepted TODO
     * @author Bard Samimi, Manpreet Grewal, John
     */
    // The most complete constructor
    public Request(
            String riderId,
            String driverId,
            String startLocationName,
            double startLongitude,
            double startLatitude,
            String endLocationName,
            double endLongitude,
            double endLatitude,
            String paymentAmount,
            String status,
            Long timeCreated,
            Long timeAccepted
    ) {
        generateUUID();
        setRiderId(riderId);
        setDriverId(driverId, timeAccepted);

        setStartLocationName(startLocationName);
        setStartLongitude(startLongitude);
        setStartLatitude(startLatitude);

        setEndLocationName(endLocationName);
        setEndLongitude(endLongitude);
        setEndLatitude(endLatitude);

        setPaymentAmount(paymentAmount);
        setStatus(status);

        setTimeCreated(timeCreated);
    }

    /**
     * Logic to generate a unique UUID string that will be used for the requestID value.
     * @return String
     * @author Manpreet Grewal
     */
    /// https://www.baeldung.com/java-uuid

    /// https://towardsdatascience.com/are-uuids-really-unique-57eb80fc2a87
    private void generateUUID() {
        this.requestId = UUID.randomUUID().toString();
    }

    // The remainder of the methods are all 'getter' and 'setter' method(s).
    // These are standard, and require no documentation.
    // Added by Bard Samimi
    public String getRequestId() {
        return requestId;
    }

    public String getRiderId() {
        return riderId;
    }

    public void setRiderId(String riderId) {
        if (riderId == null) {
            throw new IllegalArgumentException("riderId can't be null");
        }
        else {
            this.riderId = riderId;
        }
    }

    public String getDriverId() {
        return driverId;
    }

    public void setDriverId(String driverId, Long timeAccepted) {
        // Record the time at which the request was accepted as well
        if (driverId != null) {
            if (timeAccepted == null) {
                setTimeAccepted();  // Use current time
            }
            else {
                setTimeAccepted(timeAccepted);
            }

            this.driverId = driverId;
        }
    }

    public String getStartLocationName() {
        return startLocationName;
    }

    private void setStartLocationName(String startLocationName) {
        if (startLocationName == null) {
            throw new IllegalArgumentException("startLocationName can't be null");
        }
        else {
            this.startLocationName = startLocationName;
        }
    }

    public double getStartLongitude() {
        return startLongitude;
    }

    public void setStartLongitude(double startLongitude) {
        this.startLongitude = startLongitude;
    }

    public double getStartLatitude() {
        return startLatitude;
    }

    public void setStartLatitude(double startLatitude) {
        this.startLatitude = startLatitude;
    }

    public String getEndLocationName() {
        return endLocationName;
    }

    private void setEndLocationName(String endLocationName) {
        if (endLocationName == null) {
            throw new IllegalArgumentException("endLocationName can't be null");
        }
        else {
            this.endLocationName = endLocationName;
        }
    }

    public double getEndLongitude() {
        return endLongitude;
    }

    public void setEndLongitude(double endLongitude) {
        this.endLongitude = endLongitude;
    }

    public double getEndLatitude() {
        return endLatitude;
    }

    public void setEndLatitude(double endLatitude) {
        this.endLatitude = endLatitude;
    }

    public String getPaymentAmount() {
        return paymentAmount;
    }

    public void setPaymentAmount(String paymentAmount) {
        this.paymentAmount = paymentAmount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        status = status.toUpperCase();

        if (status.equals("INCOMPLETE") || status.equals("ACTIVE") || status.equals("COMPLETE") || status.equals("CANCELLED")) {
            this.status = status;
        }
        else {
            throw new IllegalArgumentException("An illegal value has been entered for request status");
        }
    }

    // Set creation time to the current time
    private void setTimeCreated() {
        this.timeCreated = new RequestTime();
    }

    // Set creation time to a specific time
    private void setTimeCreated(Long timeCreated) {
        if (timeCreated == null) {
            setTimeCreated();
        }
        else {
            this.timeCreated = new RequestTime(timeCreated);
        }
    }

    public RequestTime getTimeCreated() {
        return this.timeCreated;
    }

    // Set accepted time to the current time
    private void setTimeAccepted() {
        this.timeAccepted = new RequestTime();
    }

    // Set accepted time to a specific time
    private void setTimeAccepted(Long timeAccepted) {
        if (timeAccepted == null) {
            setTimeAccepted();
        }
        else {
            this.timeAccepted = new RequestTime(timeAccepted);
        }
    }

    public RequestTime getTimeAccepted() {
        return this.timeAccepted;
    }

    public String getElapsedTime() {
        if (timeAccepted == null) {
            throw new NullPointerException("The ride hasn't been accepted yet. You can't call getElapsedTime() on it");
        }
        else {
            return getTimeAccepted().getTimeElapsed();
        }
    }
}
