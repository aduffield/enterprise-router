package com.hastings.router.user.model;

import java.util.List;

public class CreateUserRequest {

    private String firstName;
    private String lastName;
    private String emailAddress;
    private String userName;

    private List<UserRole> roles;

    /**
     * A RouterUser creation object.
     *
     * @param userName
     * @param firstName
     * @param lastName
     * @param emailAddress
     */
    public CreateUserRequest(String userName, String firstName, String lastName,
                             String emailAddress, List<UserRole> roles) {
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
        this.emailAddress = emailAddress;
        this.roles = roles;
    }

    public CreateUserRequest() {
    }


    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    public List<UserRole> getRoles() {
        return roles;
    }

    public void setRoles(List<UserRole> roles) {
        this.roles = roles;
    }

    @Override
    public String toString() {
        return "CreateUserRequest{" +
                "firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", emailAddress='" + emailAddress + '\'' +
                ", userName='" + userName + '\'' +
                ", userRole=" + roles +
                '}';
    }
}
