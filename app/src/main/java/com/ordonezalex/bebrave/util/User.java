package com.ordonezalex.bebrave.util;

public class User {

    public static final long DEFAULT_ID = -1;
    public static final String DEFAULT_USERNAME = null;
    public static final String DEFAULT_PASSWORD = null;
    public static final String DEFAULT_FIRST_NAME = null;
    public static final String DEFAULT_LAST_NAME = null;
    public static final String DEFAULT_ORGANIZATION_IDENTIFIER = null;
    public static final Role DEFAULT_ROLE = null;
    public static final Picture DEFAULT_PICTURE = null;
    public static final School DEFAULT_SCHOOL = null;

    private long id;
    private String username, password, firstName, lastName, organizationIdentifier;
    private Role role;
    private Picture picture;
    private School school;

    /**
     * Perhaps you just want a default `User`. Use this constructor.
     */
    public User() {

        this(DEFAULT_USERNAME, DEFAULT_PASSWORD, DEFAULT_FIRST_NAME, DEFAULT_LAST_NAME, DEFAULT_SCHOOL, DEFAULT_ROLE, DEFAULT_ORGANIZATION_IDENTIFIER);
    }

    /**
     * Perhaps your `User` does not have a password. Use this constructor.
     *
     * @param username The `User`'s username which is used to log in.
     */
    public User(String username) {

        this(username, DEFAULT_PASSWORD, DEFAULT_FIRST_NAME, DEFAULT_LAST_NAME, DEFAULT_SCHOOL, DEFAULT_ROLE, DEFAULT_ORGANIZATION_IDENTIFIER);
    }

    /**
     * Perhaps your `User` does not have a first name or last name. Use this constructor.
     *
     * @param username The `User`'s username which is used to log in.
     * @param password The `User`'s password which is used to log in.
     */
    public User(String username, String password) {

        this(username, password, DEFAULT_FIRST_NAME, DEFAULT_LAST_NAME, DEFAULT_SCHOOL, DEFAULT_ROLE, DEFAULT_ORGANIZATION_IDENTIFIER);
    }

    /**
     * Perhaps your `User` does not belong to a `School`. Use this constructor.
     *
     * @param username  The `User`'s username which is used to log in.
     * @param password  The `User`'s password which is used to log in.
     * @param firstName The `User`'s first name.
     * @param lastName  The `User`'s last name.
     */
    public User(String username, String password, String firstName, String lastName) {

        this(username, password, firstName, lastName, DEFAULT_SCHOOL, DEFAULT_ROLE, DEFAULT_ORGANIZATION_IDENTIFIER);
    }

    /**
     * Perhaps your `User`'s `School` does not assign roles or identifiers (such as student ID numbers). Use this constructor.
     *
     * @param username  The `User`'s username which is used to log in.
     * @param password  The `User`'s password which is used to log in.
     * @param firstName The `User`'s first name.
     * @param lastName  The `User`'s last name.
     * @param school    The `User`'s `School`
     */
    public User(String username, String password, String firstName, String lastName, School school) {

        this(username, password, firstName, lastName, school, DEFAULT_ROLE, DEFAULT_ORGANIZATION_IDENTIFIER);
    }

    /**
     * Perhaps your `User`'s `School` does not assign identifiers (such as student ID numbers). Use this constructor.
     *
     * @param username  The `User`'s username which is used to log in.
     * @param password  The `User`'s password which is used to log in.
     * @param firstName The `User`'s first name.
     * @param lastName  The `User`'s last name.
     * @param school    The `User`'s `School`
     * @param role      The `User`'s `Role` at their `School`.
     */

    public User(String username, String password, String firstName, String lastName, School school, Role role) {

        this(username, password, firstName, lastName, school, role, DEFAULT_ORGANIZATION_IDENTIFIER);
    }

    /**
     * Add everything to your `User` all at once.
     *
     * @param username               The `User`'s username which is used to log in.
     * @param password               The `User`'s password which is used to log in.
     * @param firstName              The `User`'s first name.
     * @param lastName               The `User`'s last name.
     * @param school                 The `User`'s `School`
     * @param role                   The `User`'s `Role` at their `School`.
     * @param organizationIdentifier The `User`'s identifier at their `School`.
     */
    public User(String username, String password, String firstName, String lastName, School school, Role role, String organizationIdentifier) {

        this.setId(DEFAULT_ID);

        this.setUsername(username);
        this.setPassword(password);

        this.setFirstName(firstName);
        this.setLastName(lastName);

        this.setSchool(school);
        this.setRole(role);
        this.setOrganizationIdentifier(organizationIdentifier);

        this.setPicture(DEFAULT_PICTURE);
    }

    public long getId() {

        return id;
    }

    public void setId(long id) {

        this.id = id;
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

    public String getFirstName() {

        return firstName;
    }

    public void setFirstName(String firstName) {

        this.firstName = firstName;
    }

    public String getLastName() {

        return lastName;
    }

    public void setLastName(String lastName) {

        this.lastName = lastName;
    }

    public String getOrganizationIdentifier() {

        return organizationIdentifier;
    }

    public void setOrganizationIdentifier(String organizationIdentifier) {

        this.organizationIdentifier = organizationIdentifier;
    }

    public Role getRole() {

        return role;
    }

    public void setRole(Role role) {

        this.role = role;
    }

    public Picture getPicture() {

        return picture;
    }

    public void setPicture(Picture picture) {

        this.picture = picture;
    }

    public School getSchool() {

        return school;
    }

    public void setSchool(School school) {

        this.school = school;
    }
}
