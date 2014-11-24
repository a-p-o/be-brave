package com.ordonezalex.bebrave.util;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Alert {

    public static final long DEFAULT_ID = -1;
    public static final School DEFAULT_SCHOOL = null;
    public static final String DEFAULT_TITLE = null;
    public static final int DEFAULT_COLOR = 0x0000;
    public static final int DEFAULT_PRIORITY = -1;
    public static final boolean DEFAULT_ENABLED = false;

    @JsonProperty("ID")
    private long id;
    @JsonProperty("School")
    private School school;
    @JsonProperty("Title")
    private String title;
    @JsonProperty("Color")
    private int color;
    @JsonProperty("Priority")
    private int priority;
    @JsonProperty("Enabled")
    private boolean enabled;

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

        this.setId(DEFAULT_ID);
        this.setSchool(school);
        this.setTitle(title);
        this.setColor(color);
        this.setPriority(priority);
        this.setEnabled(enabled);
    }

    public long getId() {

        return id;
    }

    public void setId(long id) {

        this.id = id;
    }

    public School getSchool() {

        return school;
    }

    public void setSchool(School school) {

        this.school = school;
    }

    public String getTitle() {

        return title;
    }

    public void setTitle(String title) {

        this.title = title;
    }

    public int getColor() {

        return color;
    }

    public void setColor(int color) {

        this.color = color;
    }

    public int getPriority() {

        return priority;
    }

    public void setPriority(int priority) {

        this.priority = priority;
    }

    public boolean isEnabled() {

        return enabled;
    }

    public void setEnabled(boolean enabled) {

        this.enabled = enabled;
    }

    public void enable() {

        this.enabled = true;
    }

    public void disable() {

        this.enabled = false;
    }

    @Override
    public String toString() {

        String s;

        s = "{" + "\n"
                + "id: " + getId() + "\n"
                + "school: " + getSchool() + "\n"
                + "title: " + getTitle() + "\n"
                + "color: " + getColor() + "\n"
                + "priority: " + getPriority() + "\n"
                + "enabled: " + isEnabled() + "\n"
                + "}" + "\n";

        return s;
    }
}
