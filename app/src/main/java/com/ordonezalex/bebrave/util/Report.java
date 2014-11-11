package com.ordonezalex.bebrave.util;

import android.location.Location;

import java.util.ArrayList;
import java.util.List;

// TODO: Actually match this to report according to API
public class Report {

    public static final long DEFAULT_ID = -1;
    public static final User DEFAULT_USER = null;
    public static final Alert DEFAULT_ALERT = null;
    public static final Status DEFAULT_STATUS = null;
    public static final Location DEFAULT_LOCATION = null;
    public static final List<Location> DEFAULT_LOCATIONS = null;

    private long id;
    private User user;
    private Alert alert;
    private Status status;
    private List<Location> locations = new ArrayList<Location>();

    public Report() {

        this(DEFAULT_USER, DEFAULT_ALERT, DEFAULT_STATUS, DEFAULT_LOCATION);
    }

    public Report(User user) {

        this(user, DEFAULT_ALERT, DEFAULT_STATUS, DEFAULT_LOCATION);
    }

    public Report(User user, Alert alert) {

        this(user, alert, DEFAULT_STATUS, DEFAULT_LOCATION);
    }

    public Report(User user, Alert alert, Status status) {

        this(user, alert, status, DEFAULT_LOCATION);
    }

    public Report(User user, Alert alert, Status status, Location location) {

        this.setId(DEFAULT_ID);
        this.setUser(user);
        this.setAlert(alert);
        this.setStatus(status);
        this.addLocation(location);
    }

    public long getId() {

        return id;
    }

    public void setId(long id) {

        this.id = id;
    }

    public User getUser() {

        return user;
    }

    public void setUser(User user) {

        this.user = user;
    }

    public Alert getAlert() {

        return alert;
    }

    public void setAlert(Alert alert) {

        this.alert = alert;
    }

    public Status getStatus() {

        return status;
    }

    public void setStatus(Status status) {

        this.status = status;
    }

    public List<Location> getLocations() {

        return locations;
    }

    public void addLocation(Location location) {

        locations.add(location);
    }

    public void setLocations(List<Location> locations) {

        this.locations = locations;
    }
}
