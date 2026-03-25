package com.spring.microservice.reservehub.authapplicationservice.controller;

import com.spring.microservice.reservehub.authapplicationservice.dto.AuthResponse;
import com.spring.microservice.reservehub.authapplicationservice.dto.LoginRequest;
import com.spring.microservice.reservehub.authapplicationservice.dto.RegisterRequest;
import com.spring.microservice.reservehub.authapplicationservice.entity.User;
import com.spring.microservice.reservehub.authapplicationservice.service.JwtService;
import com.spring.microservice.reservehub.authapplicationservice.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final UserService userService;
    private final JwtService jwtService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterRequest request) {
        User user = userService.register(request);
        String token = jwtService.generateToken(user);

        return ResponseEntity.ok(new AuthResponse(
                token,
                user.getEmail(),
                user.getRole(),
                user.getFullName()
        ));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        User user = (User) authentication.getPrincipal();
        String token = jwtService.generateToken(user);

        return ResponseEntity.ok(new AuthResponse(
                token,
                user.getEmail(),
                user.getRole(),
                user.getFullName()
        ));
    }
}
