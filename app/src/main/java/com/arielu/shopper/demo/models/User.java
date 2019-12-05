package com.arielu.shopper.demo.models;

/**
 * User class for describing a user entity with Login details and
 * User's data in the system.
 */
public class User {

    //// private variables.
    private String username;
    private String name;


    //// Constructors
    public User(String username, String name)
    {
        setUsername(username);
        setName(name);
    }

    //// Getters and Setters

    // username
    public void setUsername(String newUsername)
    {
        this.username = newUsername;
    }

    public String getUsername()
    {
        return this.username;
    }

    // name
    public void setName(String newName)
    {
        this.name = newName;
    }

    public String getName()
    {
        return this.name;
    }
}
