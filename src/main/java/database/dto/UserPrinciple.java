package database.dto;

import java.util.ArrayList;
import java.util.List;

/**
 * This class will be returned when user info is requested.
 * User information like Roles, permissions etc will be returned
 */
public class UserPrinciple {

    public UserPrinciple(){

    }

    private String userFirstName;
    private String userLastName;
    private List<String> roles = new ArrayList<>();
    private List<String> permissions = new ArrayList<>();

    public String getUserFirstName() {
        return userFirstName;
    }

    public void setUserFirstName(String userFirstName) {
        this.userFirstName = userFirstName;
    }

    public String getUserLastName() {
        return userLastName;
    }

    public void setUserLastName(String userLastName) {
        this.userLastName = userLastName;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }

    public List<String> getPermissions() {
        return permissions;
    }

    public void setPermissions(List<String> permissions) {
        this.permissions = permissions;
    }
}
