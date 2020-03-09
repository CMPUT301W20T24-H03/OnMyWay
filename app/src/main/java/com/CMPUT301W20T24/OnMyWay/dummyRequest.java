package com.CMPUT301W20T24.OnMyWay;

// dummy Request class , will remove after when figure out what final request class will be

public class dummyRequest{
    private String username;
    private double lat;
    private double lon;


    public dummyRequest(String username, double lat, double lon) {
        this.username = username;
        this.lat = lat;
        this.lon = lon;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public double getLon() {
        return lon;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }
}