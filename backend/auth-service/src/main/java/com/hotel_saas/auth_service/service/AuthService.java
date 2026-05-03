package com.hotel_saas.auth_service.service;

import lombok.RequiredArgsConstructor;

import java.util.List;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.hotel_saas.auth_service.dto.LoginRequest;
import com.hotel_saas.auth_service.dto.RegisterRequest;
import com.hotel_saas.auth_service.exception.InvalidCredentialsException;
import com.hotel_saas.auth_service.exception.UserAlreadyExistsException;
import com.hotel_saas.auth_service.exception.UserNotFoundException;
import com.hotel_saas.auth_service.model.Role;
import com.hotel_saas.auth_service.model.User;
import com.hotel_saas.auth_service.repository.RoleRepository;
import com.hotel_saas.auth_service.repository.UserRepository;
import com.hotel_saas.auth_service.security.JwtService;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RoleRepository roleRepository;

    public void register(RegisterRequest request) {

        userRepository.findByEmail(request.getEmail())
                .ifPresent(user -> {
                    throw new UserAlreadyExistsException("El usuario ya existe");
                });

        //Buscar rol CLIENT en BD
        Role clientRole = roleRepository.findByName("CLIENT")
                .orElseThrow(() -> new RuntimeException("Rol CLIENT no encontrado"));

        User user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(List.of(clientRole))
                .build();

        userRepository.save(user);
    }

    public String login(LoginRequest request) {

        var user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new UserNotFoundException("Usuario no encontrado"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new InvalidCredentialsException("Contraseña incorrecta");
        }

        List<String> roles = user.getRoles()
                .stream()
                .map(Role::getName)
                .toList();

        return jwtService.generateToken(user.getEmail(), roles);
    }
}