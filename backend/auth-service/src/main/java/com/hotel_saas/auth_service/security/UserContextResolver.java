package com.hotel_saas.auth_service.security;

import org.springframework.stereotype.Component;

import jakarta.servlet.http.HttpServletRequest;

import java.util.Arrays;
import java.util.List;

@Component
public class UserContextResolver {

    public UserContext resolve(HttpServletRequest request) {

        String email = request.getHeader("X-User-Email");
        String rolesHeader = request.getHeader("X-User-Roles");

        List<String> roles = rolesHeader != null
                ? Arrays.asList(rolesHeader.split(","))
                : List.of();

        return new UserContext(email, roles);
    }
}