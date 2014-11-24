package com.ordonezalex.bebrave.util;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Location {
    public static final double DEFAULT_LATITUDE = -1;
    public static final double DEFAULT_LONGITUDE = -1;
    public static final String DEFAULT_TIME = "00:00:00 00-00-0000";

    @JsonProperty("Latitude")
    private double latitude;
    @JsonProperty("Longitude")
    private double longitude;
    @JsonProperty("Time")
    private String time;

    public Location() {

        this(DEFAULT_LATITUDE, DEFAULT_LONGITUDE, DEFAULT_TIME);
    }

    public Location(double latitude, double longitude) {

        this(latitude, longitude, DEFAULT_TIME);
    }

    public Location(double latitude, double longitude, String time) {

        this.setLatitude(latitude);
        this.setLongitude(longitude);
        this.setTime(time);
    }

    public double getLatitude() {

        return latitude;
    }

    public void setLatitude(double latitude) {

        this.latitude = latitude;
    }

    public double getLongitude() {

        return longitude;
    }

    public void setLongitude(double longitude) {

        this.longitude = longitude;
    }

    public String getTime() {

        return time;
    }

    public void setTime(String time) {

        this.time = time;
    }

    @Override
    public String toString() {

        String s;

        s = "{" + "\n"
                + "latitude: " + getLatitude() + "\n"
                + "longitude: " + getLongitude() + "\n"
                + "time: " + getTime() + "\n"
                + "}" + "\n";

        return s;
    }
}
