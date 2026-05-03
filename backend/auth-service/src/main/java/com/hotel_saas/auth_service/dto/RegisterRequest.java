package com.hotel_saas.auth_service.dto;

import lombok.Data;

@Data
public class RegisterRequest {
    private String email;
    private String password;
}