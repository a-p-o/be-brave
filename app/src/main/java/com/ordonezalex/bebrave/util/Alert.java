package com.ordonezalex.bebrave.util;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Alert {

    public static final long DEFAULT_ID = -1;
    public static final School DEFAULT_SCHOOL = null;
    public static final String DEFAULT_TITLE = null;
    public static final int DEFAULT_COLOR = -1;
    public static final int DEFAULT_PRIORITY = -1;
    public static final boolean DEFAULT_ENABLED = false;

    @JsonProperty("ID")
    private long ID;
    @JsonProperty("School")
    private School School;
    @JsonProperty("Title")
    private String Title;
    @JsonProperty("Color")
    private int Color;
    @JsonProperty("Priority")
    private int Priority;
    @JsonProperty("Enabled")
    private boolean Enabled;

    public Alert() {

        this(DEFAULT_SCHOOL, DEFAULT_TITLE, DEFAULT_COLOR, DEFAULT_PRIORITY, DEFAULT_ENABLED);
    }

    public Alert(School school) {

        this(school, DEFAULT_TITLE, DEFAULT_COLOR, DEFAULT_PRIORITY, DEFAULT_ENABLED);
    }

    public Alert(School school, String title) {

        this(school, title, DEFAULT_COLOR, DEFAULT_PRIORITY, DEFAULT_ENABLED);
    }

    public Alert(School school, String title, int color) {

        this(school, title, color, DEFAULT_PRIORITY, DEFAULT_ENABLED);
    }

    public Alert(School school, String title, int color, int priority) {

        this(school, title, color, priority, DEFAULT_ENABLED);
    }

    public Alert(School school, String title, int color, int priority, boolean enabled) {

        this.setID(DEFAULT_ID);
        this.setSchool(school);
        this.setTitle(title);
        this.setColor(color);
        this.setPriority(priority);
        this.setEnabled(enabled);
    }

    public long getID() {

        return ID;
    }

    public void setID(long ID) {

        this.ID = ID;
    }

    public School getSchool() {

        return School;
    }

    public void setSchool(School school) {

        this.School = school;
    }

    public String getTitle() {

        return Title;
    }

    public void setTitle(String title) {

        this.Title = title;
    }

    public int getColor() {

        return Color;
    }

    public void setColor(int color) {

        this.Color = color;
    }

    public int getPriority() {

        return Priority;
    }

    public void setPriority(int priority) {

        this.Priority = priority;
    }

    public boolean isEnabled() {

        return Enabled;
    }

    public void setEnabled(boolean enabled) {

        this.Enabled = enabled;
    }

    public void enable() {

        this.Enabled = true;
    }

    public void disable() {

        this.Enabled = false;
    }

    @Override
    public String toString() {

        String s;

        s = "{" + "\n"
                + "id: " + getID() + "\n"
                + "school: " + getSchool() + "\n"
                + "title: " + getTitle() + "\n"
                + "color: " + getColor() + "\n"
                + "priority: " + getPriority() + "\n"
                + "enabled: " + isEnabled() + "\n"
                + "}" + "\n";

        return s;
    }
}
