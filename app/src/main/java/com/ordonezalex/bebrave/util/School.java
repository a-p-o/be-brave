package com.ordonezalex.bebrave.util;

import com.fasterxml.jackson.annotation.JsonProperty;

public class School {
    public static final long DEFAULT_ID = -1;
    public static final String DEFAULT_EMAIL_DOMAIN = null;
    public static final String DEFAULT_ADDRESS = null;
    public static final String DEFAULT_PHONE_NUMBER = null;

    @JsonProperty("ID")
    private long ID;
    @JsonProperty("EmailDomain")
    private String EmailDomain;
    @JsonProperty("Address")
    private String Address;
    @JsonProperty("PhoneNumber")
    private String PhoneNumber;

    public School() {

        this(DEFAULT_EMAIL_DOMAIN, DEFAULT_ADDRESS, DEFAULT_PHONE_NUMBER);
    }

    public School(String emailDomain) {

        this(emailDomain, DEFAULT_ADDRESS, DEFAULT_PHONE_NUMBER);
    }

    public School(String emailDomain, String address) {

        this(emailDomain, address, DEFAULT_PHONE_NUMBER);
    }

    public School(String emailDomain, String address, String phoneNumber) {

        this.setId(DEFAULT_ID);
        this.setEmailDomain(emailDomain);
        this.setAddress(address);
        this.setPhoneNumber(phoneNumber);
    }

    public long getId() {

        return ID;
    }

    public void setId(long id) {

        this.ID = id;
    }

    public String getEmailDomain() {

        return EmailDomain;
    }

    public void setEmailDomain(String emailDomain) {

        this.EmailDomain = emailDomain;
    }

    public String getAddress() {

        return Address;
    }

    public void setAddress(String address) {

        this.Address = address;
    }

    public String getPhoneNumber() {

        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {

        this.PhoneNumber = phoneNumber;
    }

    @Override
    public String toString() {

        String s;

        s = "{" + "\n"
                + "id: " + getId() + "\n "
                + "emailDomain: " + getEmailDomain() + "\n"
                + "address: " + getAddress() + "\n"
                + "phoneNumber: " + getPhoneNumber() + "\n"
                + "}" + "\n";

        return s;
    }
}
