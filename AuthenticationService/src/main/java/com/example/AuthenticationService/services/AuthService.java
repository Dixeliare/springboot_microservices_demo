package com.example.AuthenticationService.services;

import com.example.AuthenticationService.dtos.JwtResponseDTO;
import com.example.AuthenticationService.dtos.LoginRequestDTO;
import com.example.AuthenticationService.dtos.RegisterRequestDTO;
import com.example.AuthenticationService.models.Role;
import com.example.AuthenticationService.models.User;

public interface AuthService {
    Integer registerUser(RegisterRequestDTO request);
    JwtResponseDTO authenticateUser(LoginRequestDTO request);
    User createUser(String email, String password, Role role);
}
