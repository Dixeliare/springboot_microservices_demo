package com.example.AuthenticationService.dtos;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequestDTO {
    @NotBlank(message = "Email can't be empty")
    @Email(message = "Invalid email format")
    private  String email;
    @NotBlank(message = "Password can't be empty")
    private String password;
}
