package com.arielu.shopper.demo.models;

/**
 * User class for describing a user entity with Login details and
 * User's data in the system.
 */
public class User {

    //// private variables.
    private String username,name,password,phoneNumber;
    private enum UserType{Customer,Worker};
    private UserType userType;
    //// Constructors
    public User(String username,String name){
        setUsername(username);
        setName(name);
    }
    public User(String name,String phoneNumber,int userType)
    {
        setName(name);
        setPhoneNumber(phoneNumber);
        setUserType(userType);
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public UserType getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = UserType.values()[userType];
    }
}
