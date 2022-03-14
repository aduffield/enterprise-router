package com.hastings.router.authentication.model;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 *
 */
public class AuthenticationRequest implements Serializable {

    @NotBlank(message = "RouterUser Name cannot be empty")
    @Email
    private String username;
    private String password;

    // need default constructor for JSON Parsing
    public AuthenticationRequest() {
    }

    /**
     * @param username
     * @param password
     */
    public AuthenticationRequest(String username, String password) {
        this.setUsername(username);
        this.setPassword(password);
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
