package controller;

/**
 * This class will simply hold the user name and password coming from login rest end point
 */

public class UserCredentials {

    //The username of the user
    private String username;

    //The password of the user
    private String password;

    public UserCredentials(){

    }
    //A constructor with username and password parameters
    public UserCredentials(String username, String password) {
        this.username = username;
        this.password = password;
    }

    //A getter method for the username
    public String getUsername() {
        return username;
    }

    //A setter method for the username
    public void setUsername(String username) {
        this.username = username;
    }

    //A getter method for the password
    public String getPassword() {
        return password;
    }

    //A setter method for the password
    public void setPassword(String password) {
        this.password = password;
    }

}
