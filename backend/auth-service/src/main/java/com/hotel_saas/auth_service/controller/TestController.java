package com.hotel_saas.auth_service.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.hotel_saas.auth_service.security.UserContext;
import com.hotel_saas.auth_service.security.UserContextResolver;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/test")
@RequiredArgsConstructor
public class TestController {

    private final UserContextResolver userContextResolver;

    @GetMapping("/private")
    public String privateEndpoint(HttpServletRequest request) {
        UserContext user = userContextResolver.resolve(request);

        return "Acceso autorizado para Usuario: " + user.getEmail() +
               " | Roles: " + user.getRoles();
    }
}