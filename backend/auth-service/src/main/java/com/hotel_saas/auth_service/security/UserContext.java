package com.hotel_saas.auth_service.security;

import java.util.List;

public class UserContext {

    private String email;
    private List<String> roles;

    public UserContext(String email, List<String> roles) {
        this.email = email;
        this.roles = roles;
    }

    public String getEmail() {
        return email;
    }

    public List<String> getRoles() {
        return roles;
    }
}