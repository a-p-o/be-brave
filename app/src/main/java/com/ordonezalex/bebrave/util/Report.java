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
    public static final School DEFAULT_SCHOOL = null;
//    public static final Location DEFAULT_LOCATION = null;
//    public static final List<Location> DEFAULT_LOCATIONS = null;

    private long ID;
    private User User;
    private Alert Alert;
    private Status Status;
    private School School;
    private List<Location> locations = new ArrayList<Location>();

    public Report() {

        this(DEFAULT_USER, DEFAULT_ALERT, DEFAULT_STATUS, DEFAULT_SCHOOL);
    }

    public Report(User user) {

        this(user, DEFAULT_ALERT, DEFAULT_STATUS, DEFAULT_SCHOOL);
    }

    public Report(User user, Alert alert) {

        this(user, alert, DEFAULT_STATUS, DEFAULT_SCHOOL);
    }

    public Report(User user, Alert alert, Status status) {

        this(user, alert, status, DEFAULT_SCHOOL);
    }

    public Report(User user, Alert alert, Status status, School school) {

        this.setId(DEFAULT_ID);
        this.setUser(user);
        this.setAlert(alert);
        this.setStatus(status);
        this.setSchool(school);
//        this.addLocation(location);
    }

    public long getId() {

        return ID;
    }

    public void setId(long id) {

        this.ID = id;
    }

    public User getUser() {

        return User;
    }

    public void setUser(User user) {

        this.User = user;
    }

    public Alert getAlert() {

        return Alert;
    }

    public void setAlert(Alert alert) {

        this.Alert = alert;
    }

    public Status getStatus() {

        return Status;
    }

    public void setStatus(Status status) {

        this.Status = status;
    }

    public School getSchool() {

        return School;
    }

    public void setSchool(School school) {

        this.School = school;
    }

//    public Location[] getLocations() {
//        Location[] emptyLocations = new Location[]{};
//
//        return emptyLocations;
//    }
//
//    public void addLocation(Location location) {
//
//        locations.add(location);
//    }
//
//    public void setLocations(List<Location> locations) {
//
//        this.locations = locations;
//    }
}
