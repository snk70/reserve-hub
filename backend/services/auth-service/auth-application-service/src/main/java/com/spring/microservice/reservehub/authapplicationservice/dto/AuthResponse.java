package com.spring.microservice.reservehub.authapplicationservice.dto;

import com.spring.microservice.reservehub.authapplicationservice.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponse {
    private String token;
    private String email;
    private Role role;
    private String fullName;

}