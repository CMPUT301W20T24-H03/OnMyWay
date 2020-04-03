package com.CMPUT301W20T24.OnMyWay;

import java.util.UUID;


/**
 * Request model that holds the blueprint for this object type.
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
     * @param riderId The id of the rider in a request
     * @param driverId The id of the rider in a request. Can be null if the driver is not assigned yet
     * @param startLocationName The human-friendly name of the start location for a request
     * @param startLongitude The longitude of the start location, stored as a long
     * @param startLatitude The latitude of the start location, stored as a long
     * @param endLocationName The human-friendly name of the end location for a request
     * @param endLongitude The longitude of the end location, stored as a long
     * @param endLatitude The latitude of the end location, stored as a long
     * @param paymentAmount The amount that will be charged for this request, stored as a string
     * @param status The status of the current request. A string that is one of:
     *               "INCOMPLETE, "ACTIVE", "COMPLETE", OR "CANCELLED"
     * @param timeCreated The time a request was first created, given as a long representing
     *                    seconds since epoch
     * @param timeAccepted The time a request was first accepted by a driver,
     *                     given as a long representing seconds since epoch
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
     * Logic to generate a unique UUID string that will be used for the requestID value
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

    /**
     * Sets the driverId of the request and records the time that the request was accepted
     * @param driverId The ID of the driver accepting a request
     * @param timeAccepted The time the request was accepted. If this is null the current time is used
     * @author John
     */
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
        else if (timeAccepted != null) {
            throw new IllegalArgumentException("You can't have a timeAccepted value without a driver");
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

    /**
     * Set the status of a ride
     * @param status The status of the current request. A string that is one of:
     *               "INCOMPLETE, "ACTIVE", "COMPLETE", OR "CANCELLED".
     *               Lowercase values are also accepted
     */
    public void setStatus(String status) {
        if (status == null) {
            throw new IllegalArgumentException("Request status can't be null");
        }
        else {
            status = status.toUpperCase();

            if (status.equals("INCOMPLETE") || status.equals("ACTIVE") || status.equals("COMPLETE") || status.equals("CANCELLED")) {
                this.status = status;
            }
            else {
                throw new IllegalArgumentException("An illegal value has been entered for request status");
            }
        }
    }

    // Set creation time to the current time
    private void setTimeCreated() {
        this.timeCreated = new RequestTime();
    }

    /**
     * Sets the time a ride was created
     * @param timeCreated The time a request was created, stored as a long representing
     *                    seconds since epoch. If this value is null the current time will
     *                    be used instead
     * @author John
     */
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

    /**
     * Sets the time a ride was accepted by a driver
     * @param timeAccepted The time a request was accepted, stored as a long representing
     *                    seconds since epoch. If this value is null the current time will
     *                    be used instead
     * @author John
     */
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

    /**
     * Gets the elapsed time since a ride was accepted by a driver
     * @return A string representation of the time elapsed in the format, "mm:ss"
     * @author John
     */
    public String getElapsedTime() {
        if (timeAccepted == null) {
            throw new NullPointerException("The ride hasn't been accepted yet. You can't call getElapsedTime() on it");
        }
        else {
            return getTimeAccepted().getTimeElapsed();
        }
    }
}
