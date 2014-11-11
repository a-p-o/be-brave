package com.ordonezalex.bebrave.util;

public class Role {
    public static final long DEFAULT_ID = -1;
    public static final School DEFAULT_SCHOOL = null;
    public static final String DEFAULT_NAME = null;

    private long id;
    private School school;
    private String name;

    public Role() {

        this(DEFAULT_SCHOOL, DEFAULT_NAME);
    }

    public Role(School school) {

        this(school, DEFAULT_NAME);
    }

    public Role(School school, String name) {

        this.setId(id);
        this.setSchool(school);
        this.setName(name);
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

    public String getName() {

        return name;
    }

    public void setName(String name) {

        this.name = name;
    }
}
