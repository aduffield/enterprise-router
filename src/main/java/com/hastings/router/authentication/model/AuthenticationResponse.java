package com.hastings.router.authentication.model;

import com.hastings.router.user.model.UserStatus;

import java.io.Serializable;

/**
 *
 */
public class AuthenticationResponse implements Serializable {

    private static final long serialVersionUID = -8091879091924046844L;
    private String userName;
    private String firstName;
    private String lastName;
    private UserStatus userStatus;
    private String token;
    private Long organisationId;
    private String organisationName;

    public AuthenticationResponse() {
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

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public UserStatus getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(UserStatus userStatus) {
        this.userStatus = userStatus;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Long getOrganisationId() {
        return organisationId;
    }

    public void setOrganisationId(Long organisationId) {
        this.organisationId = organisationId;
    }

    public String getOrganisationName() {
        return organisationName;
    }

    public void setOrganisationName(String organisationName) {
        this.organisationName = organisationName;
    }

    @Override
    public String toString() {
        return "AuthenticationResponse{" +
                "userName='" + userName + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", userStatus=" + userStatus +
                ", token='" + "[REDACTED]" + '\'' +
                ", organisationId=" + organisationId +
                ", organisationName='" + organisationName + '\'' +
                '}';
    }
}
