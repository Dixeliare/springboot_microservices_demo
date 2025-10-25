package com.example.AuthenticationService.services.impl;

import com.example.AuthenticationService.dtos.JwtResponseDTO;
import com.example.AuthenticationService.dtos.LoginRequestDTO;
import com.example.AuthenticationService.dtos.RegisterRequestDTO;
import com.example.AuthenticationService.models.Role;
import com.example.AuthenticationService.models.User;
import com.example.AuthenticationService.repositories.UserRepository;
import com.example.AuthenticationService.security.JwtTokenProvider;
import com.example.AuthenticationService.services.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public Integer registerUser(RegisterRequestDTO request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new IllegalArgumentException("Email already exists");
        }

        User user = new User(
                null,
                request.getEmail(),
                passwordEncoder.encode(request.getPassword()),
                request.getRole() != null ? Role.valueOf(request.getRole().toUpperCase()) : Role.CUSTOMER,
                User.Status.ACTIVE
        );

        User saved = userRepository.save(user);

        return saved.getId();
    }

    @Override
    public JwtResponseDTO authenticateUser(LoginRequestDTO request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        String jwt = jwtTokenProvider.generateToken(authentication);
        User user = (User) authentication.getPrincipal();

        return new JwtResponseDTO(jwt, user.getId(), user.getEmail(), user.getRole().name());
    }

    @Override
    public User createUser(String email, String password, Role role) {
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email already exists");
        }

        User user = new User(null, email, passwordEncoder.encode(password), role, User.Status.ACTIVE);

        return userRepository.save(user);
    }
}
