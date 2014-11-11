package com.ordonezalex.bebrave.util;

public class Picture {
    public static final long DEFAULT_ID = -1;
    public static final String DEFAULT_PATH = null;

    private long id;
    private String path;

    public Picture() {

        this(DEFAULT_PATH);
    }

    public Picture(String path) {

        this.setId(id);
        this.setPath(path);
    }

    public long getId() {

        return id;
    }

    public void setId(long id) {

        this.id = id;
    }

    public String getPath() {

        return path;
    }

    public void setPath(String path) {

        this.path = path;
    }
}
