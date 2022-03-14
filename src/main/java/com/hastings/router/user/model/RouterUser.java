package com.hastings.router.user.model;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.List;


/**
 *
 */
public class RouterUser {

    private String userName;
    private String firstName;
    private String lastName;
    //    private String emailAddress;
    private UserStatus userStatus;
    private String token;
    private String password;
    private Long organisationId;
    private String organisationName;
    private List<UserRole> roles;

    public RouterUser() {
    }

    public RouterUser(String userName, String firstName, String lastName, List<UserRole> roles) {
        this.userName = userName;
        this.firstName = firstName;
        this.lastName = lastName;
//        this.emailAddress = emailAddress;
        this.userStatus = UserStatus.PENDING;
        this.roles = roles;
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

//    public String getEmailAddress() {
//        return emailAddress;
//    }
//
//    public void setEmailAddress(String emailAddress) {
//        this.emailAddress = emailAddress;
//    }

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;

    }

    public String getOrganisationName() {
        return organisationName;
    }

    public void setOrganisationName(String organisationName) {
        this.organisationName = organisationName;
    }

    public List<UserRole> getRoles() {
        return roles;
    }

    public void setRoles(List<UserRole> roles) {
        this.roles = roles;
    }

    public Long getOrganisationId() {
        return organisationId;
    }

    public void setOrganisationId(Long organisationId) {
        this.organisationId = organisationId;
    }


    public String asJsonString() {
        try {
            return new ObjectMapper().writeValueAsString(this);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String toString() {
        return "RouterUser{" +
                ", userName='" + userName + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
//                ", emailAddress='" + emailAddress + '\'' +
                ", userStatus=" + userStatus +
                ", token='" + token + '\'' +
                ", password='" + password + '\'' +
                ", organisationId=" + organisationId +
                ", organisationName='" + organisationName + '\'' +
                ", roles=" + roles +
                '}';
    }

    public String[] getRolesAsStringArray() {
        return roles.stream().map(Enum::name).toArray(String[]::new);

    }

    /**
     * Convert to and from String to RouterUser type
     */
    public static class UserStatusConverter {

        public String convert(UserStatus userStatus) {
            return userStatus.name();
        }

        public UserStatus unconvert(String userStatus) {
            return UserStatus.valueOf(userStatus);
        }
    }

}
