package com.ordonezalex.bebrave.util;

public class Alert {

    public static final boolean DEFAULT_ENABLED = false;
    public static final String DEFAULT_SCHOOL_ID = null;
    public static final String DEFAULT_COLOR = null;

    private boolean enabled = DEFAULT_ENABLED;
    private String schoolId = DEFAULT_SCHOOL_ID;
    private String color = DEFAULT_COLOR;

    public Alert(String schoolId) {

        this(schoolId, DEFAULT_COLOR, DEFAULT_ENABLED);
    }

    public Alert(String schoolId, String color) {

        this(schoolId, color, DEFAULT_ENABLED);
    }

    public Alert(String schoolId, String color, boolean enabled) {

        this.setSchoolId(schoolId);
        this.setColor(color);

        if (enabled) {
            this.enable();
        }
    }

    public void setSchoolId(String schoolId) {

        this.schoolId = schoolId;
    }

    public void setColor(String color) {

        this.color = color;
    }

    public void enable() {

        this.enabled = true;
    }

    public void disable() {

        this.enabled = false;
    }

    public String getSchoolId() {

        return this.schoolId;
    }

    public String getColor() {

        return this.color;
    }

    public boolean isEnabled() {

        return this.enabled;
    }
}
