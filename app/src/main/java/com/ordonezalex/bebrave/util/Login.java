package com.ordonezalex.bebrave.util;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Login {
    public static final String DEFAULT_USERNAME = null;
    public static final String DEFAULT_PASSWORD = null;

    @JsonProperty("Username")
    private String username;
    @JsonProperty("Password")
    private String password;

    public Login() {

        this(DEFAULT_USERNAME, DEFAULT_PASSWORD);
    }

    public Login(String username, String password) {

        this.setUsername(username);
        this.setPassword(password);
    }

    public String getUsername() {

        return username;
    }

    public void setUsername(String username) {

        this.username = username;
    }

    public String getPassword() {

        return password;
    }

    public void setPassword(String password) {

        this.password = password;
    }   

    @Override
    public String toString() {

        String s;

        s = "{" + "\n"
                + "username: " + getUsername() + "\n"
                + "password: " + getPassword() + "\n"
                + "}" + "\n";

        return s;
    }
}
